package com.greenfish.rdfstarversioning;
import org.eclipse.rdf4j.query.MalformedQueryException;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler {


    public static void handleClientToServerRequests(Socket client, Socket tripleStoreServer) throws IOException {
        final byte[] request = new byte[4096];
        final int proxyPort = 7480;
        final InputStream streamFromClient = client.getInputStream();
        final OutputStream streamToServer = tripleStoreServer.getOutputStream();

        // a thread to read the client's requests and pass them
        // to the server. A separate thread for asynchronous.
        Thread t = new Thread(() -> {
            int baseContentLength;
            int size;
            try {
                while ((size = streamFromClient.read(request)) != -1) {
                    // read from client and write to byte array
                    String requestStr = new String(request, StandardCharsets.UTF_8);
                    Matcher mGetKeyword = Pattern.compile("^GET (.*?)[? ]").matcher(requestStr);
                    Matcher mPostKeyword = Pattern.compile("^POST (.*?)[? ]").matcher(requestStr);
                    Matcher mQueryKeyword = Pattern.compile("(?<=query=)([\\w.%+*]*?)(?=[-\u0000\\s&])((?:&.[^-\u0000\\s]+)*)(?=\u0000*)").matcher(requestStr);
                    Matcher mUpdateKeyword = Pattern.compile("\\bupdate=(.*)").matcher(requestStr);
                    Matcher mContentLength = Pattern.compile("\\bContent-Length: (\\d*)").matcher(requestStr);

                    try {
                        if((mPostKeyword.find(0) || mGetKeyword.find(0)) && mQueryKeyword.find(0)) {
                            System.out.println("Timestamp query");
                            String query = mQueryKeyword.group(1);
                            String decodedStmt = java.net.URLDecoder.decode(query, StandardCharsets.UTF_8.name());
                            try {
                                String timestampedQuery = QueryHandler.timestampQuery(decodedStmt);
                                String encodedQuery = java.net.URLEncoder.encode(timestampedQuery, StandardCharsets.UTF_8.name());

                                //Create connection
                                StringBuilder sb = new StringBuilder();
                                sb.append("GET").append(" ").append(mGetKeyword.group(1))
                                        .append("?query=").append(encodedQuery).append(" ").append("HTTP/1.1");
                                sb.append("\r\nContent-Type: application/x-www-form-urlencoded; charset=utf-8");
                                sb.append("\r\nAccept: ").append("text/csv;q=0.8," +
                                        " application/x-sparqlstar-results+json;q=0.8," +
                                        " application/sparql-results+json;q=0.8," +
                                        " application/json;q=0.8," +
                                        " application/sparql-results+xml," +
                                        " application/xml," +
                                        " text/tab-separated-values;q=0.8," +
                                        " application/x-sparqlstar-results+xml;q=0.8," +
                                        " text/x-tab-separated-values-star;q=0.8," +
                                        " application/x-sparqlstar-results+tsv;q=0.8," +
                                        " application/x-binary-rdf-results-table;q=0.8");
                                sb.append("\r\nHost: ").append(tripleStoreServer.getInetAddress().getHostName())
                                        .append(":").append(proxyPort);
                                sb.append("\r\nConnection: ").append("Keep-Alive");
                                sb.append("\r\nUser-Agent: ").append("java.net.socket application (Proxy Server)");
                                sb.append("\r\nAccept-Encoding: ").append("gzip,deflate");
                                sb.append("\r\n\r\n");

                                // read from byte array and write to server
                                streamToServer.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                            } catch (MalformedQueryException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                                streamToServer.write(request, 0, size);
                            }
                        }
                        else if(mPostKeyword.find(0) && mUpdateKeyword.find(0) && mContentLength.find(0))
                        {
                            System.out.println("Timestamp update");
                            baseContentLength = ("update=").length();
                            String updateUrlPropertyValue = mUpdateKeyword.group(1).substring(0, Integer.parseInt(mContentLength.group(1)) - baseContentLength);
                            String decodedStmt = java.net.URLDecoder.decode(updateUrlPropertyValue, StandardCharsets.UTF_8.name());
                            String timestampedUpdate = "";
                            try {
                                timestampedUpdate = QueryHandler.timestampUpdate(decodedStmt);
                                String encodedInsert = java.net.URLEncoder.encode(timestampedUpdate, StandardCharsets.UTF_8.name());

                                //Create connection
                                StringBuilder sb = new StringBuilder();
                                sb.append("POST").append(" ").append(mPostKeyword.group(1))
                                        .append(" ").append("HTTP/1.1");
                                sb.append("\r\nContent-Type: ").append("application/x-www-form-urlencoded; charset=utf-8");
                                sb.append("\r\nContent-Length: ").append(baseContentLength + encodedInsert.length());
                                sb.append("\r\nHost: ").append(tripleStoreServer.getInetAddress().getHostName())
                                        .append(":").append(proxyPort);
                                sb.append("\r\nConnection: ").append("Keep-Alive");
                                sb.append("\r\nUser-Agent: ").append("java.net.socket application (Proxy Server)\"");
                                sb.append("\r\nAccept-Encoding: ").append("gzip,deflate");
                                sb.append("\r\n\r\n");
                                sb.append("update=").append(encodedInsert);

                                // read from byte array and write to server
                                streamToServer.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                            } catch (MalformedQueryException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                                System.out.println("Exception for the malformed query is provided by the triple store engine.");
                                streamToServer.write(request, 0, size);
                            }

                        } else {
                            streamToServer.write(request, 0, size);
                            System.out.println("No GET with ?query or POST with ?update sent. Request was passed through unmodified.");
                            System.out.println(requestStr);
                        }
                    } catch (Exception e) {
                        System.out.println("Query or update cannot be handled by the proxy." +
                                " No request will be forwarded to the server.");
                        System.out.println(e.getMessage());
                        e.printStackTrace();

                        PrintWriter out = new PrintWriter(client.getOutputStream());
                        out.print("Query or update cannot be handled by the proxy." +
                                " No request will be forwarded to the server");
                        out.flush();
                        client.close();

                    } finally {
                        streamToServer.flush();
                    }

                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Socket closed temporarily. Will reopen with a new ping.");
            }

            // the client closed the connection to us, so close our
            // connection to the server.
            try {
                streamToServer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        });

        // Start the client-to-server request thread running
        t.start();
    }

    public static void handleServerToClientReplies(Socket client, Socket tripleStoreServer) throws IOException {
        // Get server streams.
        final InputStream streamFromServer = tripleStoreServer.getInputStream();
        final OutputStream streamToClient = client.getOutputStream();

        byte[] reply = new byte[4096];

        // Read the server's responses and pass them back to the client.
        int bytesRead;
        try {
            while ((bytesRead = streamFromServer.read(reply)) != -1) {
                streamToClient.write(reply, 0, bytesRead);
                streamToClient.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // The server closed its connection to us, so we close our connection to our client.
        streamToClient.close();

    }


}

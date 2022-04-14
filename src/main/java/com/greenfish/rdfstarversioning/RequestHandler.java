package com.greenfish.rdfstarversioning;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.eclipse.rdf4j.query.MalformedQueryException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler {

    private static String getUnicodeCharacterOfChar(char ch) {
        return String.format("\\u%04x", (int) ch);
    }

    public static void handleClientToServerRequests(Socket client, Socket server) throws IOException {
        final byte[] request = new byte[4096];
        final InputStream streamFromClient = client.getInputStream();
        final OutputStream streamToServer = server.getOutputStream();

        // a thread to read the client's requests and pass them
        // to the server. A separate thread for asynchronous.
        Thread t = new Thread(() -> {
            int baseContentLength;
            int size;
            try {
                while ((size = streamFromClient.read(request)) != -1)
                {
                    // read from client and write to byte array
                    String requestStr = new String(request, StandardCharsets.UTF_8);
                    Pattern postKeyword = Pattern.compile("\\bPOST\\b");
                    Matcher mPostKeyword = postKeyword.matcher(requestStr);

                    Pattern queryKeyword = Pattern.compile("(?<=query=)(.*?)(?=&)(.*[^\u0000])(?=\u0000*)");
                    Matcher mQueryKeyword = queryKeyword.matcher(requestStr);
                    Pattern updateKeyword = Pattern.compile("(?<=update=)(.*?)(?=&)(.*[^\\u0000])(?=\\u0000*)");
                    Matcher mUpdateKeyword = updateKeyword.matcher(requestStr);

                    try {
                        if(mPostKeyword.find())
                        {
                            if (mQueryKeyword.find() ) {
                                System.out.println("Modify query");
                                baseContentLength = ("query=" + mQueryKeyword.group(2)).length();
                                String query = mQueryKeyword.group(1);
                                String decodedStmt = java.net.URLDecoder.decode(query, StandardCharsets.UTF_8.name());
                                String timestampedQuery ="";
                                try {
                                    timestampedQuery = QueryHandler.timestampQuery(decodedStmt);
                                    String encodedQuery = java.net.URLEncoder.encode(timestampedQuery, StandardCharsets.UTF_8.name());
                                    String newRequest = mQueryKeyword.replaceFirst(encodedQuery + "$2");
                                    Pattern p2 = Pattern.compile("\\b(?<=Content-Length: ).*\\b");
                                    Matcher m2 = p2.matcher(newRequest);
                                    String newRequest2 = m2.replaceFirst(String.valueOf(baseContentLength + encodedQuery.length()));
                                    byte[] newRequestBytes = Utils.rtrim(newRequest2.getBytes(StandardCharsets.UTF_8));

                                    // read from byte array and write to server
                                    streamToServer.write(newRequestBytes);
                                } catch (MalformedQueryException e) {
                                    System.out.println(e.getMessage());
                                    e.printStackTrace();
                                    streamToServer.write(request, 0, size);
                                }

                            } else if (mUpdateKeyword.find()) {
                                System.out.println("Modify update");
                                baseContentLength = ("update=" + mUpdateKeyword.group(2)).length();
                                String update = mUpdateKeyword.group(1);
                                String decodedStmt = java.net.URLDecoder.decode(update, StandardCharsets.UTF_8.name());
                                String timestampedUpdate = "";
                                try {
                                    timestampedUpdate = QueryHandler.timestampUpdate(decodedStmt);
                                    String encodedInsert = java.net.URLEncoder.encode(timestampedUpdate, StandardCharsets.UTF_8.name());
                                    String newRequest = mUpdateKeyword.replaceFirst(encodedInsert + "$2");
                                    Pattern p2 = Pattern.compile("\\b(?<=Content-Length: ).*\\b");
                                    Matcher m2 = p2.matcher(newRequest);
                                    String newRequest2 = m2.replaceFirst(String.valueOf(baseContentLength + encodedInsert.length()));
                                    byte[] newRequestBytes = Utils.rtrim(newRequest2.getBytes(StandardCharsets.UTF_8));

                                    // read from byte array and write to server
                                    streamToServer.write(newRequestBytes);
                                } catch (MalformedQueryException e) {
                                    System.out.println(e.getMessage());
                                    e.printStackTrace();
                                    streamToServer.write(request, 0, size);
                                }

                            } else {
                                streamToServer.write(request, 0, size);
                                System.out.println("Parameters query= or update= were not found in the http request." +
                                        " Request was passed through unmodified.");
                            }

                        } else {
                            streamToServer.write(request, 0, size);
                            System.out.println("No query or update sent. Request was passed through unmodified.");
                        }

                    } catch (Exception e) {
                        //TODO: Send dummy request to server so that the malformed request does not block.
                        System.out.println("This should not happen. No exception handling yet for this case.");
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    } finally {
                        streamToServer.flush();
                    }

                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Socket closed temporarily. Will reopen with a new ping.");
                //e.printStackTrace();
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

    public static void handleServerToClientReplies(Socket client, Socket server) throws IOException {
        // Get server streams.
        final InputStream streamFromServer = server.getInputStream();
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

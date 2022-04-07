package com.greenfish.rdfstarversioning;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler {
    public static void handleClientToServerRequests(Socket client, Socket server) throws IOException {
        final byte[] request = new byte[4096];
        final InputStream streamFromClient = client.getInputStream();
        final OutputStream streamToServer = server.getOutputStream();
        int baseContentLength = 59;

        // a thread to read the client's requests and pass them
        // to the server. A separate thread for asynchronous.
        Thread t = new Thread(() -> {
            int size;
            try {
                while ((size = streamFromClient.read(request)) != -1)
                {
                    // read from client and write to byte array
                    String requestStr = new String(request, StandardCharsets.UTF_8);
                    Pattern postKeyword = Pattern.compile("\\bPOST\\b");
                    Matcher mPostKeyword = postKeyword.matcher(requestStr);
                    Pattern queryKeyword = Pattern.compile("(?<=query=).*(?=&infer)");
                    Matcher mQueryKeyword = queryKeyword.matcher(requestStr);
                    try {
                        if (mQueryKeyword.find() && mPostKeyword.find()) {
                            //TODO: Modify request which is sent to server
                            String userQuery = mQueryKeyword.group();
                            String timestampedQuery = QueryHandler.timestampQuery(userQuery);
                            String encodedQuery = java.net.URLEncoder.encode(timestampedQuery, StandardCharsets.UTF_8.name());
                            String newRequest = mQueryKeyword.replaceFirst(encodedQuery);
                            Pattern p2 = Pattern.compile("\\b(?<=Content-Length: ).*\\b");
                            Matcher m2 = p2.matcher(newRequest);
                            String newRequest2 = m2.replaceFirst(String.valueOf(baseContentLength + timestampedQuery.length()));
                            byte[] newRequestBytes = rtrim(newRequest2.getBytes(StandardCharsets.UTF_8));


                            System.out.println("Original request bytes: " + size + "; Modified request bytes: " + rtrim(newRequestBytes).length);
                            System.out.println("Original query: " + mQueryKeyword.group() + " ; Timestamped query: " + encodedQuery);
                            System.out.println("Original request \n" + new String(rtrim(request), StandardCharsets.UTF_8) + "\n\nNew request \n" + new String(rtrim(newRequestBytes), StandardCharsets.UTF_8));
                            System.out.println("\n\n\n\n\n");

                            // read from byte array and write to server
                            streamToServer.write(newRequestBytes);
                            //streamToServer.write(request, 0, size);

                        } else {
                            streamToServer.write(request, 0, size);
                            System.out.println("No query sent. Request was passed through unmodified.");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    } finally {
                        streamToServer.flush();
                    }

                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Socket closed temporarily. Will reopen with a new client accept.");
                e.printStackTrace();
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


        // Read the server's responses
        // and pass them back to the client.
        int bytesRead;
        try {
            while ((bytesRead = streamFromServer.read(reply)) != -1) {
                streamToClient.write(reply, 0, bytesRead);
                streamToClient.flush();

                //String str = new String(request, StandardCharsets.UTF_8);
                //System.out.println(str);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // The server closed its connection to us, so we close our
        // connection to our client.
        streamToClient.close();

    }

    /*//from w w w. ja v a  2 s .  co m
     * Copyright 1999-2101 Alibaba Group Holding Ltd.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */


    private static byte[] rtrim(byte[] array) {
        int notZeroLen = array.length;
        for (int i = array.length - 1; i >= 0; --i, notZeroLen--) {
            if (array[i] != 0) {
                break;
            }
        }

        if (notZeroLen != array.length) {
            array = Arrays.copyOf(array, notZeroLen);
        }

        return array;
    }





}

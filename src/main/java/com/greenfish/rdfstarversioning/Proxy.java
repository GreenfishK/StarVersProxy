package com.greenfish.rdfstarversioning;

import java.io.*;
import java.net.*;

public class Proxy {
    public static void main(String[] args) throws IOException {
        try {
            String host = "localhost";
            int remoteport = 7400;
            int localport = 7480;
            // Printing a start-up message
            System.out.println("Starting proxy for " + host + ":" + remoteport + " on port " + localport);
            // And start running the server
            runServer(host, remoteport, localport); // never returns
        }
        catch (Exception e) {
            System.err.println(e); //Prints the standard errors
        }
    }

    /**
     * It will run a single-threaded proxy server on
     * the provided local port.
     */
    public static void runServer(String host, int remoteport, int localport) throws IOException {
        // Creating a ServerSocket to listen for connections with
        ServerSocket s = new ServerSocket(localport);
        while (true) {
            Socket client = null, server = null;
            try {
                // It will wait for a connection on the local port
                client = s.accept();

                // Create a connection to the real server.
                // If we cannot connect to the server, send an error to the
                // client, disconnect, and continue waiting for connections.
                try {
                    server = new Socket(host, remoteport);
                } catch (IOException e) {
                    PrintWriter out = new PrintWriter(client.getOutputStream());
                    out.print("Proxy server cannot connect to " + host + ":"
                            + remoteport + ":\n" + e + "\n");
                    out.flush();
                    client.close();
                    continue;
                }

                RequestHandler.handleClientToServerRequests(client, server);
                RequestHandler.handleServerToClientReplies(client, server);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (server != null)
                        server.close();
                    if (client != null)
                        client.close();
                } catch (IOException e) {
                }
            }

        }
    }

    }

package com.greenfish.rdfstarversioning;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class Proxy {
    public static void main(String[] args) {
        String host = null;
        int serverport = -1;
        int proxyport = -1;

        try {
            try (InputStream input = Proxy.class.getClassLoader().getResourceAsStream("config.properties")) {

                Properties prop = new Properties();
    
                if (input == null) {
                    System.out.println("Sorry, unable to find config.properties");
                    return;
                }
    
                //load a properties file from class path, inside static method
                prop.load(input);

                host = prop.getProperty("host");
                serverport = Integer.parseInt(prop.getProperty("serverport"));
                proxyport = Integer.parseInt(prop.getProperty("proxyport"));
    
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            // Printing a start-up message
            System.out.println("Starting proxy for " + host + ":" + serverport + " on port " + proxyport);
            
            // And start running the server
            runServer(host, serverport, proxyport); // never returns
        }
        catch (Exception e) {
            System.err.println(e.getMessage()); //Prints the standard errors
            e.printStackTrace();
        }
    }

    /**
     * It will run a single-threaded proxy server on
     * the provided local port.
     */
    public static void runServer(String host, int serverport, int proxyport) throws IOException {
        ServerSocket proxy = new ServerSocket(proxyport);
        while (true) {
            Socket client = null, tripleStoreServer = null;
            try {
                // It will wait for a connection on the proxy port (e.g. when opening the browser and going to
                // localhost:7480
                client = proxy.accept();
                System.out.println("Connected to port 7480");

                // Create a connection to the real server.
                // If we cannot connect to the server, send an error to the
                // client, disconnect, and continue waiting for connections.
                try {
                    tripleStoreServer = new Socket(host, serverport);
                } catch (IOException e) {
                    PrintWriter out = new PrintWriter(client.getOutputStream());
                    out.print("Proxy server cannot connect to " + host + ":" + serverport + ":\n" + e + "\n");
                    out.flush();
                    client.close();
                    continue;
                }

                //TODO: Implement a http client which fixes the problem with overlapping byte streams from previous
                //  requests.
                RequestHandler.handleClientToServerRequests(client, tripleStoreServer);
                RequestHandler.handleServerToClientReplies(client, tripleStoreServer);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (tripleStoreServer != null)
                        tripleStoreServer.close();
                    if (client != null)
                        client.close();
                } catch (IOException e) {
                    System.out.println("Something happend while closing the client and server connections.");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }

        }
    }

    }

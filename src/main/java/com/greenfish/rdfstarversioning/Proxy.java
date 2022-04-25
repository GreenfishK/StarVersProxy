package com.greenfish.rdfstarversioning;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.rdf4j.http.client.SPARQLProtocolSession;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy {
    public static void main(String[] args) throws IOException {
        try {
            String host = "localhost";
            int serverport = 7400;
            int proxyport = 7480;
            // Printing a start-up message
            System.out.println("Starting proxy for " + host + ":" + serverport + " on port " + proxyport);
            // And start running the server
            runServer(host, serverport, proxyport); // never returns
        }
        catch (Exception e) {
            System.err.println(e); //Prints the standard errors
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

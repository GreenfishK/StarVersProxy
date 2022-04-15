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
        /*SPARQLRepository repo;
        RepositoryConnection sparqlRepoConnection;
        String repoId = "testTimestamping";
        String queryEndpoint = String.format("http://localhost:7400/repositories/%s", repoId);
        String updateEndpoint = String.format("http://localhost:7400/repositories/%s/statements", repoId);
        repo = new SPARQLRepository(queryEndpoint, updateEndpoint); */
        while (true) {
            Socket client = null, tripleStoreServer = null;
            try {
                // It will wait for a connection on the local port (e.g. when opening the browser and going to
                //localhost:7480
                client = s.accept();
                System.out.println("Connected to port 7480");

                // Create a connection to the real server.
                // If we cannot connect to the server, send an error to the
                // client, disconnect, and continue waiting for connections.
                try {
                    tripleStoreServer = new Socket(host, remoteport);
                } catch (IOException e) {
                    PrintWriter out = new PrintWriter(client.getOutputStream());
                    out.print("Proxy server cannot connect to " + host + ":" + remoteport + ":\n" + e + "\n");
                    out.flush();
                    client.close();
                    continue;
                }

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

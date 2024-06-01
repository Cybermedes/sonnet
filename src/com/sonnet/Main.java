package com.sonnet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) {
        // Read Shakespeare's Sonnets text file online
        URI sonnetsURI = URI.create("https://www.gutenberg.org/cache/epub/1041/pg1041.txt");
        HttpRequest request = HttpRequest.newBuilder(sonnetsURI).GET().build();
        try (HttpClient client = HttpClient.newBuilder().build()) {
            HttpResponse<InputStream> response = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
            InputStream inputStream = response.body();
            inputStream.close();
        } catch (IOException | InterruptedException e) {
            System.err.format("%s%n", e);
        }

    }
}

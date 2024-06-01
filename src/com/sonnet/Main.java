package com.sonnet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Read Shakespeare's Sonnets text file online
        URI sonnetsURI = URI.create("https://www.gutenberg.org/cache/epub/1041/pg1041.txt");
        HttpRequest request = HttpRequest.newBuilder(sonnetsURI).GET().build();

        try (HttpClient client = HttpClient.newBuilder().build()) {

            HttpResponse<InputStream> response = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());

            InputStream inputStream = response.body();
            readFileContent(inputStream);
            inputStream.close();
        } catch (IOException | InterruptedException e) {
            System.err.format("%s%n", e);
        }

    }

    private static void readFileContent(InputStream inputStream) throws IOException {
        // The text start at line 30, before this line there is only technical info
        int start = 30;

        List<Sonnet> sonnets = new ArrayList<>();

        var reader = new SonnetReader(inputStream);
        reader.skipLines(start);
        var sonnet = reader.readNextSonnet();
        while (sonnet != null) {
            sonnets.add(sonnet);
            sonnet = reader.readNextSonnet();
        }
        reader.close();

        System.out.println("Number of sonnets = " + sonnets.size());
    }
}

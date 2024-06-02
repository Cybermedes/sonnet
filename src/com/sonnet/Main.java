package com.sonnet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Sonnet> sonnets;

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

            writeAllTheSonnets();

        } catch (IOException | InterruptedException e) {
            System.err.format("%s%n", e);
        }

    }

    private static void readFileContent(InputStream inputStream) throws IOException {

        // The text start at line 30, before this line there is only technical info
        int start = 30;
        sonnets = new ArrayList<>();

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

    // Write the compressed sonnets into a binary file
    private static void writeAllTheSonnets() throws IOException {

        int numberOfSonnets = sonnets.size();
        Path sonnetsFile = Path.of("sonnets.bin");

        if (!Files.exists(sonnetsFile)) {
            Files.createFile(sonnetsFile);
        }

        try (var sonnetFile = Files.newOutputStream(sonnetsFile);
             var dos = new DataOutputStream(sonnetFile)) {

            List<Integer> offsets = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();
            byte[] encodedSonnetsByteArray;

            try (ByteArrayOutputStream encodedSonnets = new ByteArrayOutputStream()) {
                for (Sonnet sonnet : sonnets) {
                    byte[] sonnetCompressedBytes = sonnet.getCompressedBytes();

                    offsets.add(encodedSonnets.size());
                    lengths.add(sonnetCompressedBytes.length);
                    encodedSonnets.write(sonnetCompressedBytes);
                }

                dos.writeInt(numberOfSonnets);
                for (int i = 0; i < numberOfSonnets; i++) {
                    dos.writeInt(offsets.get(i));
                    dos.writeInt(lengths.get(i));
                }

                encodedSonnetsByteArray = encodedSonnets.toByteArray();
            }
            sonnetFile.write(encodedSonnetsByteArray);
        }
    }
}

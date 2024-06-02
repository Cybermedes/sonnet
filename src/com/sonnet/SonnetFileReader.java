package com.sonnet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class SonnetFileReader {

    public void readOneSonnetFromFile(Path sonnetFile) throws IOException {

        if (!Files.exists(sonnetFile)) {
            System.out.printf("File %s doesn't exist", sonnetFile.getFileName());
            System.exit(0);
        }

        try (var file = Files.newInputStream(sonnetFile);
             var bis = new BufferedInputStream(file);
             var dis = new DataInputStream(bis);
             var scanner = new Scanner(System.in)) {

            int numberOfSonnets = dis.readInt();
            System.out.println("number of sonnets in the binary: " + numberOfSonnets);

            List<Integer> offsets = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();
            for (int i = 0; i < numberOfSonnets; i++) {
                offsets.add(dis.readInt());
                lengths.add(dis.readInt());
            }

            System.out.print("The number of the sonnet you want to read [1-154]: ");
            int sonnet = scanner.nextInt();
            if (sonnet <= 0 || sonnet > 154) {
                System.out.printf("sonnet number %d doesn't exit!", sonnet);
                System.exit(0);
            }

            int offset = offsets.get(sonnet - 1);
            int length = lengths.get(sonnet - 1);

            skip(bis, offset);
            byte[] bytes = readBytes(bis, length);

            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                 GZIPInputStream gzbais = new GZIPInputStream(bais);
                 InputStreamReader isr = new InputStreamReader(gzbais);
                 BufferedReader reader = new BufferedReader(isr)) {

                List<String> sonnetLines = reader.lines().toList();
                System.out.printf("Sonnet number %d:%n%n", sonnet);
                sonnetLines.forEach(System.out::println);
            }
        }
    }

    private void skip(BufferedInputStream bis, int offset) throws IOException {
        long skip = 0L;
        while (skip < offset) {
            skip += bis.skip(offset - skip);
        }
    }

    private byte[] readBytes(BufferedInputStream bis, int length) throws IOException {
        byte[] bytes = new byte[length];
        byte[] buffer = new byte[length];
        int read = bis.read(buffer);
        int copied = 0;
        while (copied < length) {
            System.arraycopy(buffer, 0, bytes, copied, read);
            copied += read;
            read = bis.read(buffer);
        }
        return bytes;
    }
}

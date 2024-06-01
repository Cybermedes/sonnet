package com.sonnet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class Sonnet {

    // Store the sonnet lines in a list
    private List<String> lines = new ArrayList<>();

    public void add(String line) {
        lines.add(line);
    }

    // Compress the sonnet into a byte array to write to a file
    public byte[] getCompressedBytes() throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzos = new GZIPOutputStream(bos);
             PrintWriter printWriter = new PrintWriter(gzos)) {

            for (String line : lines) {
                printWriter.println(line);
            }
        }
        return bos.toByteArray();
    }
}

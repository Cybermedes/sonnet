package com.sonnet;

import java.io.*;

public class SonnetReader extends BufferedReader {

    public SonnetReader(Reader in) {
        super(in);
    }

    public SonnetReader(InputStream inputStream) {
        this(new InputStreamReader(inputStream));
    }

    // Skip a specified number of lines
    public void skipLines(int lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            readLine();
        }
    }

    // Read and throw away the blank lines and sonnet numbers
    private String skipSonnetHeader() throws IOException {
        // Sonnets starts with Roman numbers and some blank lines
        String line = readLine();
        while (line.isBlank()) {
            line = readLine();
        }

        // If the line has this text, end of the file
        if (line.equals("*** END OF THE PROJECT GUTENBERG EBOOK THE SONNETS ***")) {
            return null;
        }

        // If there are blanks lines at the end of the sonnet
        do {
            line = readLine();
        } while (line.isBlank());

        return line;
    }
}

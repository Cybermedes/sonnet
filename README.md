# Sonnet

This repository has a small Java program that reads an online text file (
the 154 sonnets of Shakespeare - https://www.gutenberg.org/cache/epub/1041/pg1041.txt),
filter its contents (remove blank lines, technical information, headers, etc.), write the filtered sonnets to a 
compressed binary file and, then read one sonnet from the binary file and display it on the screen.

This whole processes could've been made in a shorter and cleaner code, but this format was intentional so that most of
the core Java I/O API can be used. No third party libraries, no dependencies, no build automation tool,
only the built-in Java packages.
You just need at least JDK 21 and IDE, no need to download the file since it's online and the program will also use 
the built-in Http package to make the Http request.

The compressed file is composed of:

1. The total number of sonnets
2. For each sonnet: an offset and a length.
The length is the number of bytes you need to store each sonnet.
This number may vary from one sonnet to the other. The offset is the offset 
of the first byte of each sonnet in the file
3. And then the text of each sonnet, compressed with GZIP

This file format stores text files in a compressed form, and integer numbers.
It requires several elements of the Java I/O API that you can mix using the decorator pattern.
Although, the sonnets are not that big, the program stores the sonnets and can retrieve anyone in a very
efficient way for a single thread application.

Reading back a single sonnet consists in locating the right compressed array of bytes in the file, and decoding it.

Source: https://dev.java/learn/java-io/putting-it-all-together/#reading-one-sonnet

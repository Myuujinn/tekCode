package com.myujinn.tekcode.checker.major;

import com.myujinn.tekcode.MistakePrinter;
import com.myujinn.tekcode.parsing.SourceFileReader;

import java.io.File;
import java.util.List;

/**
 *  L1 --  A line should correspond to one statement.
 */
public class OneStatement {

    public static int patternCounter(String string, String pattern) {
        int counter = 0;
        String newString = string;

        while (newString.contains(pattern)) {
            if (newString.indexOf(pattern) + pattern.length() < newString.length()) {
                newString = newString.substring(string.indexOf(pattern) + pattern.length());
                System.out.println(newString);
                counter++;
            }
        }
        return counter;
    }

    private static int countCharOccurences(String string, char c) {
        int counter = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c)
                counter++;
        }
        return counter;
    }

    public static void check(File file) {
        List<String> fileContents = SourceFileReader.readFile(file);

        if (fileContents == null || fileContents.isEmpty())
            return;

        for (int i = 0; i < fileContents.size(); i++) {
            String line = fileContents.get(i);

            //checking for all statement errors
            if (patternCounter(line, " = ") > 1)
                MistakePrinter.major("L1 -- A line should correspond to only one statement.", file.getName(), i + 1);
            else if (line.contains("malloc") && line.contains("!= NULL")) //we all did it, now it's forbidden :^)
                MistakePrinter.major("L1 -- A line should correspond to only one statement.", file.getName(), i + 1);
            else if (countCharOccurences(line, ';') > 1)
                MistakePrinter.major("L1 -- A line should correspond to only one statement.", file.getName(), i + 1);
            else if (line.contains("if (") && line.contains("return "))
                MistakePrinter.major("L1 -- A line should correspond to only one statement.", file.getName(), i + 1);
        }
    }
}

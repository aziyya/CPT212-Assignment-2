package org.example;

import java.util.Scanner;
import java.util.Random;

public class BoyerMoore { //creating class for Boyer Moore algorithm
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static boolean caseSensitive = true;

    public static void main(String[] args) {
        while (true) {
            System.out.println("=======PATTERN SEARCHING=======");
            System.out.println("Menu:");
            System.out.println("1. Insert string");
            System.out.println("2. Toggle case sensitivity (current: " + (caseSensitive ? "On" : "Off") + ")");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1, 2 or 3): ");
            int choice = scanner.nextInt(); // accept user choice
            scanner.nextLine(); // consume newline
            System.out.println();
            if (choice == 1) {
                System.out.print("Enter your string: ");
                String text = scanner.nextLine(); // accept user string of input
                if (text.isEmpty()) { // checks whether the string is empty
                    System.out.println("Text cannot be empty. Please try again."); // displays error message
                    continue;
                }
                searchPatternsInText(text); // search up for patterns in the string of input
            } else if (choice == 2) {
                caseSensitive = !caseSensitive; // toggle case sensitivity flag
                System.out.println("Case sensitivity toggled " + (caseSensitive ? "On" : "Off"));
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break; // exits program
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private static void searchPatternsInText(String text) {
        while (true) {
            System.out.print("Enter the pattern to search (or '-1' to stop): ");
            String pattern = scanner.nextLine(); // accept pattern that wants to be looked up from user
            if (pattern.equalsIgnoreCase("-1")) { // checks whether user wants to stop looking for patterns
                break;
            }
            if (pattern.isEmpty()) { // checks whether the pattern is empty
                System.out.println("Pattern cannot be empty. Please try again."); // prompts error message
                continue;
            }
            if (pattern.length() > text.length()) { // checks whether the length of pattern entered exceeds the length of the input string itself
                System.out.println("Pattern cannot be longer than the text. Please try again."); // prompts error message
                continue;
            }
            searchPattern(text, pattern); // search for pattern in the string
        }
        System.out.println();
    }

    private static void searchPattern(String string, String pattern) {
        // converts string and pattern to lowercase if case sensitivity is off
        if (!caseSensitive) {
            string = string.toLowerCase();
            pattern = pattern.toLowerCase();
        }
        int[] badCharTable = badChar(pattern); // creates bad character table
        int[] goodSuffixTable = goodSuffix(pattern); // creates good suffix table

        int m = pattern.length(); // length of pattern
        int n = string.length(); // length of string
        int s = 0; // s is shift of the pattern with respect to string
        boolean found = false; // act as flag to track whether pattern is found

        while (s <= (n - m)) {
            int j = m - 1; // starts comparison from the end of the pattern

            // compares pattern with the sting starting from the right to left
            while (j >= 0 && pattern.charAt(j) == string.charAt(s + j)) {
                j--;
            }

            // pattern found
            if (j < 0) {
                System.out.println("Pattern found at index " + s);
                s += (s + m < n) ? m - badCharTable[string.charAt(s + m)] : 1;
                found = true; // found flag is set to true
            } else { // pattern is not found
                s += Math.max(1, j - badCharTable[string.charAt(s + j)]);
            }
        }

        if (!found) {
            System.out.println("Pattern not found"); // prompts error message if pattern is not found
        }
    }

    private static int[] badChar(String pattern) {
        final int NO_OF_CHARS = 256; // number of possible characters allowed
        int[] badCharTable = new int[NO_OF_CHARS]; // initializes bad character table

        // initializes all occurences as -1
        for (int i = 0; i < NO_OF_CHARS; i++) {
            badCharTable[i] = -1;
        }

        // fill the actual value of last occurence
        for (int i = 0; i < pattern.length(); i++) {
            badCharTable[pattern.charAt(i)] = i;
        }
        return badCharTable; // return the bad character table
    }

    private static int[] goodSuffix(String pattern) {
        int m = pattern.length(); // length of pattern
        int[] suffix = new int[m]; // initializes the suffix array
        int[] goodSuffixTable = new int[m]; // initializes good suffix table
        int f = 0, g;

        suffix[m - 1] = m; // sets last position of suffix to the pattern length
        g = m - 1;

        // preprocesses the suffix array
        for (int i = m - 2; i >= 0; --i) {
            if (i > g && suffix[i + m - 1 - f] < i - g) {
                suffix[i] = suffix[i + m - 1 - f];
            } else {
                if (i < g) g = i;
                f = i;
                while (g >= 0 && pattern.charAt(g) == pattern.charAt(g + m - 1 - f)) {
                    g--;
                }
                suffix[i] = f - g;
            }
        }

        // initializes good suffix table
        for (int i = 0; i < m; i++) {
            goodSuffixTable[i] = m;
        }
        for (int i = m - 1; i >= 0; --i) {
            if (suffix[i] == i + 1) {
                for (int j = 0; j < m - 1 - i; j++) {
                    if (goodSuffixTable[j] == m) {
                        goodSuffixTable[j] = m - 1 - i;
                    }
                }
            }
        }
        for (int i = 0; i <= m - 2; i++) {
            goodSuffixTable[m - 1 - suffix[i]] = m - 1 - i;
        }

        return goodSuffixTable; // return good suffix table
    }
}


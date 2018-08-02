package com.company;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
/*
 * A wordcount program that analyzes a document and returns the most common words in a document.
 * The amount of words returned is determined by the user.
 * @author Christopher Fetz
 */

public class Main {

    public static void main(String[] args) {
        String fileText = args[0];
        int count = Integer.parseInt(args[1]);
        try {
            File file = new File(fileText);
            wordCount(file, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Parses a body of text and find the most common words in it.
     * wordCount() then prints out the most common words.
     */
    private static void wordCount(File file, int stringCount) {
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            HashMap<String, Integer> stringHash = new HashMap<>();

            String[] topStrings = new String[stringCount];
            int[] ranks = new int[stringCount];

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                String[] split = line.split(" ");
                for (String s : split) {
                    int count = 1;
                    if (!stringHash.containsKey(s)) {
                        stringHash.put(s, count);
                    } else {
                        count = stringHash.get(s) + 1;
                        stringHash.put(s, count);
                    }
                    adjustRanks(s, count, topStrings, ranks);
                }
            }
            printResults(topStrings, ranks);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Figures out whether a given string is one of the X most common strings in the document.
     * If it is, then it determines whether the string is already present in topStrings[].
     * If it is, then it passes the index of that string to updateExistingWord().
     * If the string is not already present in topStrings[], then it passes the
     * string and index on to insertNewWord().
     *
     */
    private static void adjustRanks(String string, int count, String[] topStrings, int[] ranks) {

        int last = ranks.length - 1;
        boolean stringAlreadyPresent = false;
        int indexOfString = 0;

        if (count >= ranks[last]){
            for (int i = 0; i < ranks.length; i++){
                if (string.equals(topStrings[i])) {
                    stringAlreadyPresent = true;
                    indexOfString = i;
                }
            }
            if (stringAlreadyPresent){
                System.out.println("Updating " + string);
                updateExistingWord(indexOfString, count, topStrings, ranks);
                for (int i : ranks){
                    System.out.println(i);
                }
                for (String s : topStrings){
                    System.out.println(s);
                }
            }
            else {
                boolean add = false;
                for (int i = last; i > -1; i--){
                    if (count > ranks[i]) {
                        indexOfString = i;
                        add = true;
                    } else if {
                        
                    }
                }
                if (add) insertNewWord(indexOfString, string, count, topStrings, ranks);
            }
        }
    }

    /*
     * Once a top string has been found, the previous same-ranked strings are checked
     * to see if the strings are in lexicographical order. Then, the string and rank
     * are placed into ranks[] and topStrings[] and the rest of the following
     * array items are pushed forward.
     */
    private static void insertNewWord(int start, String string, int count, String[] topStrings, int[] ranks) {
        if (string != null && string != ""){
            System.out.println("adding " + string);
            for (int i = start; i > 0; i--){
                if (ranks[i] == ranks[i-1]){
                    if (topStrings[i-1] != null && topStrings[i].compareTo(topStrings[i-1]) < 0){
                        swap(i-1, i, topStrings, ranks);
                    }
                }
            }
            for (int i = start; i < ranks.length; i++){
                int tempInt = ranks[i];
                String tempString = topStrings[i];

                ranks[i] = count;
                topStrings[i] = string;

                count = tempInt;
                string = tempString;
            }
        }
    }

    /*
     * Updates topStrings[] and ranks[] and checks whether the ranks[] is in order.
     * If it is, then it checks if topStrings is in lexicographical order at that rank.
     * If either array is out of order, swap() is called to swap the arrays into order.
     */
    private static void updateExistingWord(int index, int count, String[] topStrings, int[] ranks) {
        ranks[index] = count;
        boolean swapNeeded = true;
        while (swapNeeded){
            if ((index > 0 ) && (ranks[index] > ranks[index-1])){
                swap(index-1, index, topStrings, ranks);
            }
            else if ((index > 0 ) && (ranks[index] == ranks[index-1])){
                if (topStrings[index].compareTo(topStrings[index-1]) < 0){
                    swap(index-1, index, topStrings, ranks);
                }
                else {
                    swapNeeded = false;
                }
            }
            else {
                swapNeeded = false;
            }

            if (index == 0){
                swapNeeded = false;
            }
            else {
                index--;
            }
        }
    }

    /*
     * Swaps the positions of index1 and index2 in topStrings[] and ranks[].
     */
    private static void swap(int index1, int index2, String[] topStrings, int[] ranks) {
        String tempString = topStrings[index1];
        int tempInt = ranks[index1];

        topStrings[index1] = topStrings[index2];
        ranks[index1] = ranks[index2];

        topStrings[index2] = tempString;
        ranks[index2] = tempInt;
    }

    /*
     * Prints the most common strings in the document.
     */
    private static void printResults(String[] topStrings, int[] ranks) {

        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < ranks.length; i++){
            if (topStrings[i] == null || topStrings[i].isEmpty()) {
                i = ranks.length;
            }
            else {
                stb.append(topStrings[i]);
                stb.append(" ");
                stb.append("(");
                stb.append(ranks[i]);
                stb.append(")");
                if (i < ranks.length-1 && topStrings[i+1] != null) stb.append(", ");
            }
        }

        for (int i = ranks.length-1; i == 0; i--){
            System.out.println(topStrings[i]);
            System.out.println(i);
        }
        System.out.println(stb);
    }
}





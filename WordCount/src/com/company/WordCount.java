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
public class WordCount {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        String fileText = "";
        int count = 0;

        try {
            fileText = args[0];
            count = Integer.parseInt(args[1]);
            if (count <= 0){
                System.out.println("Desired word count must be greater than 0.");
                System.exit(0);
            }
        } catch(Exception e) {
            System.out.println("Please give a path to the desired file followed by the number of top-ranked words desired.");
            System.exit(0);
        }

        try {
            File file = new File(fileText);
            wordCount(file, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime);
        double seconds = (double)elapsedTime / 1000000000.0;
        System.out.println("Task took " + seconds + " seconds.");
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
            int wordCount = 0;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                String[] split = line.split(" ");

                boolean notEmpty = !line.isEmpty() && !line.trim().equals("") && !line.trim().equals("\n");

                if (notEmpty) {
                    for (String s : split) {
                        if (!s.isEmpty()){
                            wordCount++;
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
                }
            }
            int uniqueWords = stringHash.size();
            printResults(wordCount, uniqueWords, topStrings, ranks);
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
                updateExistingWord(indexOfString, count, topStrings, ranks);
            }
            else {
                boolean add = false;
                for (int i = last; i > -1; i--){
                    if (count > ranks[i]) {
                        indexOfString = i;
                        add = true;
                    } else if (count == ranks[i] && string.compareTo(topStrings[i]) < 0){
                        add = true;
                        indexOfString = i;
                    }
                }
                if (add) {
                    insertNewWord(indexOfString, string, count, topStrings, ranks);
                }
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
    private static void printResults(int wordTotal, int uniqueWords, String[] topStrings, int[] ranks) {
        StringBuilder mostCommon = new StringBuilder();
        int cutoff = ranks.length;

        for (int i = 0; i < ranks.length; i++){
            if (topStrings[i] == null || topStrings[i].isEmpty()) {
                cutoff = i;
                i = ranks.length;
            }
            else {
                mostCommon.append(topStrings[i]);
                mostCommon.append(" ");
                mostCommon.append("(");
                mostCommon.append(ranks[i]);
                mostCommon.append(")");
                if (i < ranks.length-1 && topStrings[i+1] != null) {
                    mostCommon.append(", ");
                }
            }
        }

        StringBuilder total = new StringBuilder("Total words: ");
        total.append(wordTotal);
        total.append("\n");
        total.append("Unique words: ");
        total.append(uniqueWords);

        StringBuilder fullMostCommon = new StringBuilder("The ");
        fullMostCommon.append(cutoff);
        fullMostCommon.append(" most common words are:\n");
        fullMostCommon.append(mostCommon);

        System.out.println(total);
        System.out.println(fullMostCommon);
    }
}
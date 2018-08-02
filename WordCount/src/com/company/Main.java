package com.company;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Set;


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

    //ranks most common words in body of text:
    private static void wordCount(File file, int stringCount) {
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            HashMap<String, Integer> stringHash = new HashMap<>();

            String[] topStrings = new String[stringCount];
            int[] ranks = new int[stringCount];

            String line;

            while ((line = bufferedReader.readLine()) != null) {
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
            for (int i = 0; i < stringCount; i++){
                System.out.print(topStrings[i] + " ");
                System.out.print(ranks[i] + ", ");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Figures out whether a given string is one of the X most common strings in the document.
     * If it is, then it ranks the string using IterateThrough().
     */
    private static void adjustRanks(String string, int count, String[] topStrings, int[] ranks) {

        int last = ranks.length - 1;

        if (count >= ranks[last]){
            for (int i = 0; i < ranks.length; i++){
                if (count > ranks[i]){
                    iterateThrough(i, string, count, topStrings, ranks);
                }
                i = last+1;
            }
        }
    }

    /*
     * Once a top string has been found, it is placed into topStrings[] and the other array
     * items are pushed back once.
     */
    private static void iterateThrough(int start, String string, int count, String[] topStrings, int[] ranks) {
        for (int i = start; i < ranks.length; i++){
            int tempInt = ranks[i];
            ranks[i] = count;
            count = tempInt;

            if (string != topStrings[start]){
                String tempString = topStrings[i];
                topStrings[i] = string;
                string = tempString;
            }
        }
    }
}





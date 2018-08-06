package com.company;

import java.util.ArrayList;
import java.util.HashSet;

public class InterviewQuestion {

    //args[0] = string to solve
    //args[1...] = substrings
    //goodSubstrings = An ArrayList of ArrayLists
    //each arraylist is a length
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        int substringCount = args.length-1;
        String fullString = args[0];
        HashSet<Character> fullStringSet = dissolve(args[0]);
        HashSet<Character> substringSuperSet = new HashSet<>();
        ArrayList<ArrayList<Integer>> goodSubstrings = new ArrayList<>();

        for (int i = 0; i < substringCount; i++){
            ArrayList<Integer> tempList;
            HashSet<Character> tempSet;
            tempSet = dissolve(args[i+1]);
            if (fullStringSet.containsAll(tempSet)){
                tempList = match(fullString, args[i+1]);
                if (tempList.size() > 0){
                    goodSubstrings.add(tempList);
                    substringSuperSet.addAll(tempSet);
                }
            }
        }

        if (!substringSuperSet.containsAll(fullStringSet)){
            System.out.println("The given substrings cannot form the string (no search needed.)");
            printTimeTaken(startTime);
        }
        else {
            char[] fullStringArray = fullString.toCharArray();
            ArrayList<ArrayList<ArrayList<Integer>>> memo = new ArrayList<>();
            loop(fullStringArray, goodSubstrings, memo, startTime);
            System.out.println("After searching through the possibilities, the given substrings cannot form the string.");
            printTimeTaken(startTime);
        }
    }

    /*
     * Transforms a string into a HashSet of its constituent characters.
     */
    private static HashSet<Character> dissolve(String string){
        HashSet<Character> charSet = new HashSet<>();
        for (char ch:string.toCharArray()){
            charSet.add(ch);
        }
        return charSet;
    }

    /*
    * Checks whether removing the given substring at startIndex will completely clear fullString.
     */
    private static void check(char[] fullStringArray, int indexOfList, int indexOfStartIndex,
                              ArrayList<ArrayList<Integer>> goodSubstrings,
                              ArrayList<ArrayList<ArrayList<Integer>>> memo, long startTime) {
        boolean proceed = false;

        for (char c : fullStringArray){
            if (!(c == ' ')){
                proceed = true;
            }
        }

        if (!proceed){
            System.out.println("String can be formed from the substrings!");
            printTimeTaken(startTime);
            System.exit(0);
        }

        if (goodSubstrings.isEmpty()){
            proceed = false;
        }

        if (proceed){
            int length = goodSubstrings.get(indexOfList).get(0);
            int startIndex = goodSubstrings.get(indexOfList).get(indexOfStartIndex);
            for (int i = startIndex; i < startIndex+length; i++){
                fullStringArray[i] = ' ';
            }

            goodSubstrings.get(indexOfList).remove(indexOfStartIndex);

            for (ArrayList<Integer> list : goodSubstrings){
                for (int i = 1; i < list.size(); i++){
                    int hitFromTheFront = list.get(i)+list.get(0);
                    int startInRange = startIndex+length;
                    if ((list.get(i) < startIndex && hitFromTheFront > startIndex) || list.get(i) >= startIndex && list.get(i) < startInRange){
                        list.remove(i);
                        i--;
                    }
                }
            }

            for (int i = 0; i < goodSubstrings.size(); i++){
                if (goodSubstrings.get(i).size() == 1){
                    goodSubstrings.remove(i);
                    i--;
                }
            }
            if (!memo.contains(goodSubstrings)){
                memo.add(goodSubstrings);
                loop(fullStringArray, goodSubstrings, memo, startTime);
            }
        }
    }

    /*
    * The central loop that, starting with every valid substring, checks whether FullString array can
    * be constructed by adding a sequence of every other substring.
     */
    private static void loop(char[] fullStringArray, ArrayList<ArrayList<Integer>> goodSubstrings, ArrayList<ArrayList<ArrayList<Integer>>> memo, long startTime){
        int i = 0;

        for (ArrayList<Integer> list : goodSubstrings){
            for (int j = 1; j < list.size(); j++){
                ArrayList<ArrayList<Integer>> substringsCopy = copyList(goodSubstrings);
                check(fullStringArray, i, j, substringsCopy, memo, startTime);
            }
            i++;
        }
    }

    /*
    * Deep copies an ArrayList of Integer ArrayLists and returns the result.
     */
    private static ArrayList<ArrayList<Integer>> copyList (ArrayList<ArrayList<Integer>> listOfLists){
        ArrayList<ArrayList<Integer>> newList = new ArrayList<>();
        for (ArrayList<Integer> list : listOfLists){
            ArrayList<Integer> temp = new ArrayList<>();
            for (int i : list){
                temp.add(i);
            }
            newList.add(temp);
        }
        return newList;
    }

    /*
    * Checks if substring is a substring of fullString.
    * This algorithm uses the naive approach to string matching. I plan on updating
    * to use the rabin-karp method later.
     */
    private static ArrayList<Integer> match(String fullString, String subString){
        int subLength = subString.length();
        boolean isMatch = false;
        int decrement = subLength-1;

        ArrayList<Integer> matchingPositions = new ArrayList<>();
        if (subLength > fullString.length()){
            return matchingPositions;
        }

        for (int i = 0; i < fullString.length()-decrement; i++){
            int iterator = 1;
            if (fullString.charAt(i) == subString.charAt(0)){
                if (subLength == 1){
                    isMatch = true;
                }
                else {
                    for (int j = 0; j < subLength-1; j++){
                        if (!(fullString.charAt(i+iterator) == subString.charAt(j+1))) {
                            j = subLength;
                            isMatch = false;
                        } else{
                            isMatch = true;
                            iterator++;
                        }
                    }
                }
                if (isMatch == true){
                    if (matchingPositions.size() == 0){
                        matchingPositions.add(subLength);
                    }
                    matchingPositions.add(i);
                }
                isMatch = false;
            }
        }
        return matchingPositions;
    }

    /*
    * Prints the amount of time the coding task took.
     */
    private static void printTimeTaken(long startTime){
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime);
        double seconds = (double)elapsedTime / 1000000000.0;
        System.out.println("Task took " + seconds + " seconds.");
    }
}

package com.company;

import java.util.ArrayList;
import java.util.HashSet;

public class InterviewQuestion {

    //args[0] = string to solve
    //args[1...] = substrings
    //goodSubstrings = An ArrayList of ArrayLists
    //each arraylist is a length
    public static void main(String[] args) {

        int substringCount = args.length-1;
        String fullString = args[0];
        HashSet<Character> fullStringSet = dissolve(args[0]);
        HashSet<Character> substringSuperSet = new HashSet<>();
        ArrayList<ArrayList<Integer>> goodSubstrings = new ArrayList<>();

        for (int i = 0; i < substringCount; i++){
            ArrayList<Integer> tempList = new ArrayList<>();
            HashSet<Character> tempSet = new HashSet<>();
            tempSet = dissolve(args[i+1]);
            if (fullStringSet.containsAll(tempSet)){
                tempList = match(fullString, args[i+1]);
                if (tempList.size() > 0){
                    goodSubstrings.add(tempList);
                    substringSuperSet.addAll(tempSet);
                }
            }
        }

        //At this stage, the program has a list of strings that are good


        if (!substringSuperSet.containsAll(fullStringSet)){
            System.out.println("False A");
            for (char c : substringSuperSet){
                System.out.print(c);
            }
            System.exit(0);
        }
        else {
            recursiveShell(fullString, goodSubstrings);
            System.out.println("False B");
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
    * Recursive matching shell. Transforms fullString into a set of integers from 0 -> string length-1.
    * Matches the substring arrays to the fullString set
     */
    private static void recursiveShell(String fullString, ArrayList<ArrayList<Integer>> goodSubstrings){
        char[] fullStringArray = fullString.toCharArray();

        for (ArrayList<Integer> list : goodSubstrings){
            System.out.println("Next list ");
            for (int i : list){
                System.out.print(i + " ");
            }
            System.out.println();
        }

        System.out.println(goodSubstrings.size());
        for (int i = 0; i < goodSubstrings.size(); i++){
            System.out.println(i);
            for (int j = 1; j < goodSubstrings.get(i).size(); j++){
                System.out.println(j);
                System.out.println("List " + i + " starting " + j);
                ArrayList<ArrayList<Integer>> substringsCopy = new ArrayList<>(goodSubstrings);
                check(fullStringArray, i, j, substringsCopy);
            }
        }
        System.out.println("New list");
    }

    /*
    * Removes the numbers from fullSet according to the length specified by list.get(0)
     */
    private static void check(char[] fullStringArray, int indexOfList, int indexOfStartIndex, ArrayList<ArrayList<Integer>> goodSubstrings) {
        boolean proceed = false;

        for (char c : fullStringArray){
            System.out.print(c + " ");
            if (!(c == ' ')){
                proceed = true;
            }
        }
        System.out.print("\n");

        if (!proceed){
            System.out.println("True, string fully matched!");
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

            int removeIndex = 0;
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

            for (int i = 0; i < goodSubstrings.size(); i++){
                for (int j = 1; j < goodSubstrings.get(i).size(); j++){
                    ArrayList<ArrayList<Integer>> substringsCopy = new ArrayList<>(goodSubstrings);
                    check(fullStringArray, i, j, substringsCopy);
                }
            }
        }
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
}

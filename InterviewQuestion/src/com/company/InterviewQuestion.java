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
        HashSet<Integer> fullSet = new HashSet<>();

        for (int i = 0; i < fullString.length(); i++){
            fullSet.add(i);
        }

        for (int i = 0; i < goodSubstrings.size(); i++){

            HashSet<Integer> fullSetCopy = new HashSet<>(fullSet);
            ArrayList<ArrayList<Integer>> substringsCopy = new ArrayList<>(goodSubstrings);

            check(fullSetCopy, substringsCopy.get(i), i, substringsCopy);
        }
    }

    /*
    * Removes the numbers from fullSet according to the length specified by list.get(0)
     */
    private static void check(HashSet<Integer> fullSet, ArrayList<Integer> list, int index, ArrayList<ArrayList<Integer>> goodSubstrings) {
        boolean proceed = true;
        if (fullSet.isEmpty()){
            System.out.println("True");
            System.exit(0);
        }

        if (fullSet.size() >= list.get(0) && list.size() > 1){
            for (int i = 0; i < list.get(0); i++){
                if (!fullSet.contains(i+list.get(1))){
                    proceed = false;
                }
            }
        } else{
            proceed = false;
        }

        if (proceed == true){
            if (goodSubstrings.isEmpty()){
                System.out.println("False C");
                System.exit(0);
            }

            for (int i : fullSet){
               System.out.print(i + " ");
            }
            System.out.print("\n");

            for (int i = 0; i < list.get(0); i++){
                fullSet.remove(i+list.get(1)); //index 1
            }

            list.remove(1); //index 1

            if (list.size() == 1){
                goodSubstrings.remove(index);
            }

            for (int i = 0; i < goodSubstrings.size(); i++) {
                check(fullSet, list, i, goodSubstrings);
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

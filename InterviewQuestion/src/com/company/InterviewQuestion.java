package com.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InterviewQuestion {

    // d is the number of characters in the input alphabet
    public final static int INPUT_CHARACTERS = 256;

    //args[0] = string to solve
    //args[1...] = substrings
    public static void main(String[] args) {

        int substringCount = args.length-1;
        String fullString = args[0];
        HashSet<Character> fullStringSet = dissolve(args[0]);
        HashSet<Character> substringSuperSet = new HashSet<>();
        ArrayList<String> goodSubstrings = new ArrayList<>();

        for (int i = 0; i < substringCount; i++){
            ArrayList<Integer> tempList = new ArrayList<>();
            HashSet<Character> tempSet = new HashSet<>();
            tempSet = dissolve(args[i+1]);
            if (fullStringSet.containsAll(tempSet)){
                tempList = match(fullString, args[i+1]);
                if (tempList.size() > 0){
                    goodSubstrings.add(args[i+1]);
                    substringSuperSet.addAll(tempSet);
                }
            }
        }

        //At this stage, the program has a list of strings that are good

        if (!substringSuperSet.containsAll(fullStringSet)){
            System.out.println("False");
        }
        else {
            System.out.println("True");
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
    * Checks if substring is a substring of fullString.
    * This algorithm uses the naive approach to string matching. I plan on updating
    * to use the rabin-karp method later.
     */
    private static ArrayList<Integer> match(String fullString, String subString){
        int subLength = subString.length();
        boolean isMatch = false;
        int decrement;

        if (subLength == 1 || subLength == fullString.length()){
            decrement = 0;
        }
        else {
            decrement = subLength+1;
        }

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
                    matchingPositions.add(i);
                }
                isMatch = false;
            }
        }
        return matchingPositions;
    }
}

# Current projects:

## WordCount 
A java program that reads a text file and prints out the X most frequent words in alphabetical order. 

### How to run:

java WordCount <path text file> <number>

**example:**

java WordCount big.txt 10

**expected output:**

Total words: 1095683

Unique words: 75173

The 10 most common words are:

the (78162), of (39450), and (37009), to (28295), in (21393), a (20608), that (11483), he (11481), was (11159), his (9961)

Task took 0.359889856 seconds.

## InterviewQuestion
An interview question I worked on during a tech interview that totally blew me away! This is a dynamic programming problem,
but I haven't figured out how to use memoization for this particular problem...

### How to run:

java InterviewQuestion <string> <list of substrings to match>

**example for bad substrings (that don't contain all the characters of the string):**

java InterviewQuestion preposterous p pre prep

The given substrings cannot form the string (no search needed.)

**example for matching substrings:**

java InterviewQuestion preposterous p pre prep os oste oster e r p s t ous 

**expected output:**

String can be formed from the substrings!

**example for substrings that are a near match, but can't be put together to form the string:**

java InterviewQuestion preposterous p pre prep os oste oster e r p s t us 

**expected output:**

After searching through the possibilities, the given substrings cannot form the string.
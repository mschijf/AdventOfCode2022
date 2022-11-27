package com.adventofcode.december08;

import java.util.*;
import java.util.stream.Collectors;

//0:    6 - abcefg
//1: un 2 - cf
//2:    5 - acdeg
//3:    5 - acdfg
//4: un 4 - bcdf
//5:    5 - abdfg
//6:    6 - abdefg
//7: un 3 - acf
//8: un 7 - abcdefg
//9:    6 - abcdfg

public class SignalLine {
    private List<String> inputPatternList;
    private List<String> outputPatternList;

    private Map<String, Integer> patternToNumber;
    private Map<Integer, String> numberToPattern;
    private List<String> fiveDigitsPatternList;
    private List<String> sixDigitsPatternList;

    private static final int[] numberLength = {6, 2, 5, 5, 4, 5, 6, 3, 7, 6};

    public SignalLine (String line) {
        String[] input = line.split("\\s\\|\\s");
        inputPatternList = Arrays.stream(input[0].split(" ")).map(this::sortedString).collect(Collectors.toList());
        outputPatternList = Arrays.stream(input[1].split(" ")).map(this::sortedString).collect(Collectors.toList());

        fiveDigitsPatternList = new ArrayList<>();
        sixDigitsPatternList = new ArrayList<>();

        patternToNumber = new HashMap<>();
        numberToPattern = new HashMap<>();
    }

    private String sortedString(final String s) {
        char tempArray[] = s.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    public int doRun() {
        fillFiveAndSixDigitsPatternList();
        fillKnownPatterns();
        determinePatternFiveDigits(fiveDigitsPatternList.get(0));
        determinePatternFiveDigits(fiveDigitsPatternList.get(1));
        determinePatternFiveDigits(fiveDigitsPatternList.get(2));
        determinePatternSixDigits(sixDigitsPatternList.get(0));
        determinePatternSixDigits(sixDigitsPatternList.get(1));
        determinePatternSixDigits(sixDigitsPatternList.get(2));
        return determineOutput();
    }

    /*
    Note the difference in not overlapping line segments between the numbers of length 5 (with 5 segments) ad those of length 6.
    Using these differences it is possible to determine the numbers with 5 and 6 segments.
    We start with finding out the numbers for the five segments patterns.

 five-segments       six-segments
   patterns.           patterns.

      2              6      9      0
     ---            ---    ---    ---
        |          |      |   |  |   |
     ---            ---    ---                    Diff = (resp.) 3 , 3 , 3 (sum = 9)
    |              |   |      |  |   |
     ---            ---    ---    ---

      3              6      9      0
     ---            ---    ---    ---
        |          |      |   |  |   |
     ---            ---    ---                    Diff = (resp.) 3 , 1 , 3 (sum = 7)
        |          |   |      |  |   |
     ---            ---    ---    ---

      5              6      9      0
     ---            ---    ---    ---
    |              |      |   |  |   |
     ---            ---    ---                    Diff = (resp.) 1 , 1 , 3 (sum = 5)
        |          |   |      |  |   |
     ---            ---    ---    ---

     */

    private void determinePatternFiveDigits(String p5) {
        if (sixDigitsPatternList.size() != 3)
            throw new RuntimeException("unexpected amount of six patterns!");
        String p6_1 = sixDigitsPatternList.get(0);
        String p6_2 = sixDigitsPatternList.get(1);
        String p6_3 = sixDigitsPatternList.get(2);
        int delta1 = countDifferences(p5, p6_1);
        int delta2 = countDifferences(p5, p6_2);
        int delta3 = countDifferences(p5, p6_3);
        int somDelta = delta1 + delta2 + delta3;
        if (somDelta == 9) {
            numberToPattern.put(2, p5);
            patternToNumber.put(p5, 2);
        } else if (somDelta == 7) {
            numberToPattern.put(3, p5);
            patternToNumber.put(p5, 3);
        } else if (somDelta == 5) {
            numberToPattern.put(5, p5);
            patternToNumber.put(p5, 5);
        } else {
            throw new RuntimeException("Hey, what am I doing here?");
        }
    }

    private void determinePatternSixDigits(String p6) {
        String number2Pattern = numberToPattern.get(2);
        String number3Pattern = numberToPattern.get(3);
        String number5Pattern = numberToPattern.get(5);
        int delta2 = countDifferences(number2Pattern, p6);
        int delta3 = countDifferences(number3Pattern, p6);
        int delta5 = countDifferences(number5Pattern, p6);
        if (delta2 == 3 && delta3 == 3 && delta5 == 1) {
            numberToPattern.put(6, p6);
            patternToNumber.put(p6, 6);
        } else if (delta2 == 3 && delta3 == 1 && delta5 == 1) {
            numberToPattern.put(9, p6);
            patternToNumber.put(p6, 9);
        } else if (delta2 == 3 && delta3 == 3 && delta5 == 3) {
            numberToPattern.put(0, p6);
            patternToNumber.put(p6, 0);
        } else {
            throw new RuntimeException("I should not come here");
        }
    }


    private void fillFiveAndSixDigitsPatternList() {
        for (String s: inputPatternList) {
            if (s.length() == 5)
                fiveDigitsPatternList.add(s);
            else if (s.length() == 6)
                sixDigitsPatternList.add(s);
        }
        if (fiveDigitsPatternList.size() != 3)
            throw new RuntimeException("unexpected amount of five patterns!");
        if (sixDigitsPatternList.size() != 3)
            throw new RuntimeException("unexpected amount of six patterns (after init)!");

    }

    private void fillKnownPatterns() {
        for (String s: inputPatternList) {
            if (s.length() == numberLength[1]) {
                patternToNumber.put(s, 1);
                numberToPattern.put(1, s);
            } else if (s.length() == numberLength[4]) {
                patternToNumber.put(s, 4);
                numberToPattern.put(4, s);
            } else if (s.length() == numberLength[7]) {
                patternToNumber.put(s, 7);
                numberToPattern.put(7, s);
            } else if (s.length() == numberLength[8]) {
                patternToNumber.put(s, 8);
                numberToPattern.put(8, s);
            }
        }
    }

    private int countDifferences(String s5, String s6) {
        int count = 0;
        for (int i=0; i < s5.length(); ++i) {
            char ch = s5.charAt(i);
            if (s6.indexOf(ch) < 0)
                ++count;
        }
        for (int i=0; i < s6.length(); ++i) {
            char ch = s6.charAt(i);
            if (s5.indexOf(ch) < 0)
                ++count;
        }
        return count;
    }

    private int determineOutput() {
        int d0 = patternToNumber.get(outputPatternList.get(0));
        int d1 = patternToNumber.get(outputPatternList.get(1));
        int d2 = patternToNumber.get(outputPatternList.get(2));
        int d3 = patternToNumber.get(outputPatternList.get(3));
        return 1000*d0 + 100*d1 + 10*d2 + d3;
    }

    //==================================================================================================================

    public int countUniquePattersInOutputPatternList() {
        int count = 0;
        for (String s: outputPatternList) {
            int len = s.length();
            if (len == numberLength[1] || len == numberLength[4] || len == numberLength[7] || len == numberLength[8]) {
                ++count;
            }
        }
        return count;
    }

    public void print() {
        for (String b: inputPatternList)
            System.out.print(b + " ");
        System.out.print (" | ");
        for (String b: outputPatternList)
            System.out.print(b + " ");
        System.out.println();
    }
}

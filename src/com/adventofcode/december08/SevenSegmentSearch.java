package com.adventofcode.december08;

import java.util.List;
import java.util.NoSuchElementException;

public class SevenSegmentSearch {

    public static void main(String[] args) {
        SevenSegmentSearch pp = new SevenSegmentSearch();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<SignalLine> signalList;

    public SevenSegmentSearch() {
//        Input input = new Input("input08_example");
        Input input = new Input("input08_1");
        signalList = input.getSignalLines();
//        printInput();
    }

    public long run() {
//        return sumUniqueSignals();
        int sum = 0;
        for (SignalLine s: signalList) {
            int result = s.doRun();
            System.out.println(result);
            sum += result;
        }
        return sum;
    }

    private int sumUniqueSignals() {
        int sum = 0;
        for (SignalLine b: signalList)
            sum += b.countUniquePattersInOutputPatternList();
        return sum;
    }

    private void printInput() {
        for (SignalLine b: signalList)
            b.print();
        System.out.println();
    }

    //---------------------------------------------------------------------------------------------------------------------

    public static long sumArray(long[] serie) {
        long sum = 0;
        for (long value : serie) {
            sum += value;
        }
        return sum;
    }

    //---------------------------------------------------------------------------------------------------------------------

    /* Checks if a string is empty ("") or null. */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /* Counts how many times the substring appears in the larger string. */
    public static int countMatches(String text, String str)
    {
        if (isEmpty(text) || isEmpty(str)) {
            return 0;
        }

        int index = 0, count = 0;
        while (true)
        {
            index = text.indexOf(str, index);
            if (index != -1)
            {
                count ++;
                index += str.length();
            }
            else {
                break;
            }
        }

        return count;
    }
}

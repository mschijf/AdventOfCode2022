package com.adventofcode.december03;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BinaryDiagnostic {

    public static void main(String[] args) {
        BinaryDiagnostic pp = new BinaryDiagnostic();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<String> bitList;

    public BinaryDiagnostic() {
        Input input = new Input("input03_example");
//        Input input = new Input("input03_1");
        bitList = input.getInputList();
//        printInput();
    }

    public long run() {
        return getMostUsedBitPattern();
    }

    private int getMostUsedBitPattern() {
        String gammaRate = "";
        String epsilonRate = "";
        for (int i=0; i< bitList.get(0).length(); ++i) {
            if (getMostUsedBit(i) == 1) {
                gammaRate = gammaRate + "1";
                epsilonRate = epsilonRate + "0";
            } else {
                gammaRate = gammaRate + "0";
                epsilonRate = epsilonRate + "1";
            }
        }
        System.out.println("GammaRate  : " + gammaRate + " (" + Integer.parseInt(gammaRate, 2) + ")");
        System.out.println("EpsilonRate: " + epsilonRate + " (" + Integer.parseInt(epsilonRate, 2) + ")");
        return Integer.parseInt(gammaRate, 2) * Integer.parseInt(epsilonRate, 2);
    }

    private int getMostUsedBit(int index) {
        int countOne = 0;
        int countZero = 0;
        for (String s : bitList) {
            if (s.charAt(index)=='1')
                ++countOne;
            else
                ++countZero;

        }
        if (countOne == countZero)
            System.out.println("We have an equal count!!!");
        return (countOne >= countZero ? 1 : 0);
    }

    private void printInput() {
        for (String b: bitList)
            System.out.println(b);
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

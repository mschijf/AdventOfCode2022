package com.adventofcode.december03;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryDiagnosticPart2 {

    public static void main(String[] args) {
        BinaryDiagnosticPart2 pp = new BinaryDiagnosticPart2();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<String> inputBitList;

    public BinaryDiagnosticPart2() {
//        Input input = new Input("input03_example");
        Input input = new Input("input03_1");
        inputBitList = input.getInputList();
    }

    public long run() {
        return getMostUsedBitPattern();
    }

    private int getMostUsedBitPattern() {
        List<String> bitListOxygen = inputBitList.stream().collect(Collectors.toList());
        for (int i=0; i< bitListOxygen.get(0).length(); ++i) {
            int bit = getMostUsedBit(bitListOxygen, i, 1);
            bitListOxygen = cleanBitList(bitListOxygen, i, bit);
            if (bitListOxygen.size() == 1)
                break;
        }

        List<String> bitListCO2 = inputBitList.stream().collect(Collectors.toList());
        for (int i=0; i< bitListCO2.get(0).length(); ++i) {
            int bit = getMostUsedBit(bitListCO2, i, 1);
            int leastUsedBit = 1 - bit;
            bitListCO2 = cleanBitList(bitListCO2, i, leastUsedBit);
            if (bitListCO2.size() == 1)
                break;
        }

        System.out.println("OxygenRate  : " + bitListOxygen.get(0) + " (" + Integer.parseInt(bitListOxygen.get(0), 2) + ")");
        System.out.println("Co2Rate: " + bitListCO2.get(0) + " (" + Integer.parseInt(bitListCO2.get(0), 2) + ")");
        return Integer.parseInt(bitListOxygen.get(0), 2) * Integer.parseInt(bitListCO2.get(0), 2);
    }

    private List<String> cleanBitList(List<String> bitList, int index, int needsValue) {
        List<String> newBitList = new ArrayList<>();
        for (String s : bitList) {
            if (s.charAt(index) == '0' + needsValue)
                newBitList.add(s);
        }
        return newBitList;
    }

    private int getMostUsedBit(List<String> bitList, int index, int useWhenEqual) {
        int countOne = 0;
        int countZero = 0;
        for (String s : bitList) {
            if (s.charAt(index)=='1')
                ++countOne;
            else
                ++countZero;

        }
        if (countOne == countZero)
            return useWhenEqual;
        return (countOne > countZero ? 1 : 0);
    }

    private void printInput(List<String> bitList) {
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

package com.adventofcode.december01;

import com.adventofcode.december08.SignalLine;

import java.util.ArrayList;
import java.util.List;

public class DepthPuzzle {

    public static void main(String[] args) {
        DepthPuzzle pp = new DepthPuzzle();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<Integer> depthList;
    private List<Integer> depthThreeList;

    public DepthPuzzle() {
//        Input input = new Input("input01_example");
        Input input = new Input("input01_1");
        depthList = input.getNumbers();
        depthThreeList = createThreeSum();
        printInput();
    }

    public long run() {
//        return countIncreases(depthList);
        return countIncreases(depthThreeList);
    }

    private long countIncreases(List<Integer> list) {
        int count=0;
        for (int i=1; i < list.size(); ++i) {
            if (list.get(i) > list.get(i-1))
                count++;
        }
        return count;
    }

    private List<Integer> createThreeSum() {
        List<Integer> depthThreeList = new ArrayList<>();
        for (int i=0; i < depthList.size()-2; ++i) {
            depthThreeList.add(depthList.get(i) + depthList.get(i+1) + depthList.get(i+2));
        }
        return depthThreeList;
    }

    private void printInput() {
        for (Integer b: depthList)
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

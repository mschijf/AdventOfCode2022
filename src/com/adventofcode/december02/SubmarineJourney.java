package com.adventofcode.december02;

import java.util.ArrayList;
import java.util.List;

public class SubmarineJourney {

    public static void main(String[] args) {
        SubmarineJourney pp = new SubmarineJourney();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<SubmarineCommand> commandList;

    public SubmarineJourney() {
//        Input input = new Input("input02_example");
        Input input = new Input("input02_1");
        commandList = input.getCommandList();
//        printInput();
    }

    public long run() {
        int hor = 0;
        int depth = 0;
        int aim = 0;
        for (SubmarineCommand b: commandList) {
            hor += b.horizontalChange();
            if (b.isHorizontal()) {
                depth += b.horizontalChange() * aim;
            } else {
                aim += b.depthChange();
            }
        }

        return hor * depth;
    }


    private void printInput() {
        for (SubmarineCommand b: commandList)
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

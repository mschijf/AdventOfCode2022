package com.adventofcode.december06;

public class LanternFish {

    public static void main(String[] args) {
        LanternFish pp = new LanternFish();
        long output = pp.run();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private Input input;
    private long[] countFish;

    public LanternFish() {
        input = new Input("input06_1");
//        input = new Input("input06_example");
        countFish = initCountFish();
    }

    public long run() {
        simulateDays(256);
        return sumArray(countFish);
    }

    private long[] initCountFish() {
        long[] countFish = new long[9];
        for (String line : input.getInputList()) {
            for (int i=0; i<=8; ++i) {
                countFish[i] += countMatches(line, Integer.toString(i));
            }
        }
        return countFish;
    }

    private void simulateDays(int days) {
        for (int d=0; d < days; ++d) {
            nextGeneration();
        }
    }

    private void nextGeneration() {
        long save = countFish[0];
        System.arraycopy(countFish, 1, countFish, 0, 8);
        countFish[8] = save;
        countFish[6] += save;
    }

    public int run2() {
        return -1;
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

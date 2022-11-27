package com.adventofcode.december10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SyntaxScoring {

    public static void main(String[] args) {
        SyntaxScoring pp = new SyntaxScoring();
        long output = pp.run_2();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<ChunkLine> chunkLineList;

    public SyntaxScoring() {
//        Input input = new Input("input10_example");
        Input input = new Input("input10_1");
        chunkLineList = input.getChunkLines();
    }

    public long run_1() {
        long sum = 0;
        for (ChunkLine chunkLine: chunkLineList) {
            sum += chunkLine.syntaxErrorScore();
        }
        return sum;
    }

    public long run_2() {
        List<Long> completionScoreList = new ArrayList<>();
        for (ChunkLine chunkLine: chunkLineList) {
            long completionScore = chunkLine.completionScore();
            if (completionScore >= 0)
                completionScoreList.add(completionScore);
        }
        completionScoreList.sort(Comparator. naturalOrder());
        int mid = completionScoreList.size() / 2;
        return completionScoreList.get(mid);
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

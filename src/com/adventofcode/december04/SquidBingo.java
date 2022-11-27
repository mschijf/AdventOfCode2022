package com.adventofcode.december04;

import java.util.List;

public class SquidBingo {

    public static void main(String[] args) {
        SquidBingo pp = new SquidBingo();
//        long output = pp.run();
        long output = pp.run2();
        System.out.println("Puzzle output : " + output);
    }

    //-----------------------------------------------------------

    private List<Integer> bingoNumbers;
    private List<BingoCard> bingoCardList;
    private BingoCard lastWinner;
    private int lastNumber;

    public SquidBingo() {
//        Input input = new Input("input04_example");
        Input input = new Input("input04_1");
        bingoNumbers = input.getBingoNumbers();
        bingoCardList = input.getBingoCards();

    }

    public long run() {
        for (Integer b: bingoNumbers) {
            fillInNumberOnALlBingoCards(b);
            BingoCard winner = winningBingoCard();
            if (winner != null) {
                return (long) winner.sumOfNotChecked() * b;
            }
        }
        return -1;
    }

    public long run2() {
        for (Integer b: bingoNumbers) {
            fillInNumberOnALlBingoCards(b);
            BingoCard winner = winningBingoCard();
            while (winner != null) {
                lastWinner = winner;
                lastNumber = b;
                bingoCardList.remove(winner);
                winner = winningBingoCard();
            }
        }
        return (long) lastWinner.sumOfNotChecked() * lastNumber;
    }

    private void fillInNumberOnALlBingoCards(int number) {
        for (BingoCard bc: bingoCardList)
            bc.fillInNumber(number);
    }

    private BingoCard winningBingoCard() {
        for (BingoCard bc: bingoCardList) {
            if (bc.hasBingo())
                return bc;
        }
        return null;
    }

    private void printInput() {
        for (Integer b: bingoNumbers)
            System.out.print(b + " ");
        System.out.println();
        for (BingoCard bc: bingoCardList)
            bc.print();
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

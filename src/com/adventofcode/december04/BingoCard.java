package com.adventofcode.december04;

import java.util.List;

public class BingoCard {
    int bingoCard[][] = new int[5][5];
    boolean bingoCheck[][] = new boolean[5][5];

    public BingoCard(List<String> bingoCardRows) {
        int rowNumber = 0;
        for (String row: bingoCardRows) {
            int colNumber= 0;
            String rowNumberStrings[] = row.split(" ");
            for (String numberString: rowNumberStrings) {
                if (!numberString.trim().isEmpty()) {
                    bingoCard[rowNumber][colNumber] = Integer.parseInt(numberString);
                    bingoCheck[rowNumber][colNumber] = false;
                    colNumber++;
                }
            }
            rowNumber++;
        }
    }

    public void fillInNumber(int number) {
        for (int r=0; r < 5; ++r) {
            for (int c=0; c < 5; ++c) {
                if (bingoCard[r][c] == number) {
                    bingoCheck[r][c] = true;
                }
            }
        }
    }

    public boolean hasBingo() {
        return rowBingo() || columnBingo();
    }

    private boolean rowBingo() {
        for (int r=0; r < 5; ++r) {
            boolean bingo = true;
            for (int c=0; c < 5; ++c) {
                bingo = bingo && bingoCheck[r][c];
            }
            if (bingo)
                return true;
        }
        return false;
    }

    private boolean columnBingo() {
        for (int c=0; c < 5; ++c) {
            boolean bingo = true;
            for (int r=0; r < 5; ++r) {
                bingo = bingo && bingoCheck[r][c];
            }
            if (bingo)
                return true;
        }
        return false;
    }

    public int sumOfNotChecked() {
        int sum =0;
        for (int r = 0; r < 5; ++r) {
            for (int c=0; c < 5; ++c) {
                if (!bingoCheck[r][c]) {
                    sum += bingoCard[r][c];
                }
            }
        }
        return sum;
    }

    public void print() {
        System.out.println();
        for (int r=0; r < 5; ++r) {
            for (int c=0; c < 5; ++c) {
                System.out.print(bingoCard[r][c] + " ");
            }
            System.out.println();
        }
    }
}

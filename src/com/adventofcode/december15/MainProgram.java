package com.adventofcode.december15;

public class MainProgram {

    public static void main(String[] args) {
        new MainProgram();
    }

    //-----------------------------------------------------------

    private CavernMap cavernMap;

    private static final boolean test = false;

    public MainProgram() {
        String fileName = getFileName(test);
        Input input = new Input(fileName);
        cavernMap = input.getBaseClass(true);

        long output = run();
        System.out.println("Puzzle output : " + output);
    }

    public long run() {
//        cavernMap.print();
        long sum = cavernMap.findShortestPath();
        System.out.println("Nodes visited " + Position.visitCount);
        System.out.println("Nodes updated " + Position.updateCount);
        return sum;
    }

    private String getFileName(boolean test) {
        int day = getDayOfMonthFromClassName();
        return "input" + String.format("%02d", day) + "_" + (test ? "example" : "1");
    }

    private int getDayOfMonthFromClassName() {
        String monthName = "december";
        int i = this.getClass().getName().indexOf(monthName);
        int j = this.getClass().getName().lastIndexOf(".");
        String dayOfMonth = this.getClass().getName().substring(i + monthName.length(), j);
        return Integer.parseInt(dayOfMonth);
    }

}

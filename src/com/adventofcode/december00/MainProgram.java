package com.adventofcode.december00;

public class MainProgram {

    public static void main(String[] args) {
        new MainProgram();
    }

    //-----------------------------------------------------------

    private BaseClass baseClass;

    private static final boolean test = false;

    public MainProgram() {
        String fileName = getFileName(test);
        Input input = new Input(fileName);
        baseClass = input.getBaseClass();

        long output = run();
        System.out.println("Puzzle output : " + output);
    }

    public long run() {
        long sum = 0;
        return sum;
    }

    //------------------------------------------------------------------------------------------------------------------

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

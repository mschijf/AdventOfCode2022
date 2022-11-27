package com.adventofcode.december13;

public class FoldCommand {

    private int number;
    private boolean horizontalFolding;

    public FoldCommand(String inputLine) {
        String[] cmdParts = inputLine.split("\\s");
        if (!cmdParts[0].equals("fold"))
            throw new RuntimeException("no fold command");
        if (!cmdParts[1].equals("along"))
            throw new RuntimeException("wrong fold command");
        String[] folding = cmdParts[2].split("\\=");
        horizontalFolding = folding[0].equals("y");
        number = Integer.parseInt(folding[1]);
    }

    public boolean isHorizontalFolding() {
        return horizontalFolding;
    }

    public int getNumber() {
        return number;
    }
}

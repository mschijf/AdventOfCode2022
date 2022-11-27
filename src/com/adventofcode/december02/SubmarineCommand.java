package com.adventofcode.december02;

import java.util.Locale;

public class SubmarineCommand {
    private static String FORWARD = "forward";
    private static String DOWN = "down";
    private static String UP = "up";

    private final String direction;
    private final int movementUnits;

    public SubmarineCommand(String line) {
        String[] tmp = line.split("\\s");
        direction = tmp[0].trim().toLowerCase(Locale.ROOT);
        movementUnits = Integer.parseInt(tmp[1]);
    }

    public boolean isHorizontal() {
        return direction.equals(FORWARD);
    }

    public int horizontalChange() {
        if (direction.equals(FORWARD))
            return movementUnits;
        return 0;
    }

    public int depthChange() {
        if (direction.equals(UP))
            return -movementUnits;
        if (direction.equals(DOWN))
            return movementUnits;
        return 0;
    }

    public void print() {
        System.out.println(direction + " " + movementUnits);
    }
}

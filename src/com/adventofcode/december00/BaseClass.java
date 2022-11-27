package com.adventofcode.december00;

import java.util.List;

public class BaseClass {

    public BaseClass(List<String> inputLineList) {
        int lineCount = 0;
        for (String line: inputLineList) {
            ++lineCount;
        }
    }
}

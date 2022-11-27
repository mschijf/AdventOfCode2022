package com.adventofcode.december00;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Input {

    private List<String> inputList;

    public Input(String fileName) {
        inputList = readFile("data/" + fileName);
    }

    public List<String> getInputList() {
        return inputList;
    }

    //-----------------------------------------------------------------------------------------------------------

    public BaseClass getBaseClass() {
        return new BaseClass(inputList);
    }


    //-----------------------------------------------------------------------------------------------------------

    private List<String> readFile(String fileName) {
        int lineCount = 0;
        List<String> inputList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                inputList.add(line);
            }
        } catch ( Exception e) {

        }
        return inputList;
    }
}

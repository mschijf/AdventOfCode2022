package com.adventofcode.december02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Input {

    private List<String> inputList;
    private List<SubmarineCommand> commandList;

    public Input(String fileName) {
        inputList = readFile("data/" + fileName);
    }

    public List<String> getInputList() {
        return inputList;
    }

    //-----------------------------------------------------------------------------------------------------------

    public List<SubmarineCommand> getCommandList() {
        commandList = new ArrayList<>();
        for (String line: inputList) {
            commandList.add(new SubmarineCommand(line));
        }
        return commandList;
    }
    //-----------------------------------------------------------------------------------------------------------

    private  List<String> unwrapLines(List<String> list) {
        List<String> result = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        String extraSpace = "";
        for (String line: list) {
            if (line.trim().length() > 0) {
                sb.append(extraSpace + line);
                extraSpace = " ";
            } else {
                result.add(sb.toString());
                sb = new StringBuilder();
                extraSpace = "";
            }
        }
        if (extraSpace.length() > 0)
            result.add(sb.toString());
        return result;
    }

    //-----------------------------------------------------------------------------------------------------------

    private List<HashMap<String, String>> toListOfHashMaps(List<List<String>> list, String delimeters) {
        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        for (List<String> line: list) {
            result.add(toHashMap(line, delimeters));
        }
        return result;
    }

    private HashMap<String, String> toHashMap(List<String> line, String delimeters) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String item: line) {
            List<String> keyAndValueSeperated= split(item, delimeters);
            map.put(keyAndValueSeperated.get(0), keyAndValueSeperated.get(1) );
        }
        return map;
    }

    //-----------------------------------------------------------------------------------------------------------

    private List<List<String>> doSplit(List<String> list, String delimeters) {
        return list.stream().map(line -> split(line, delimeters)).collect(Collectors.toList());
//
//
//        List<List<String>> result = new ArrayList<List<String>>();
//        for (String line: list) {
//            result.add(split(line, delimeters));
//        }
//        return result;
    }

    private List<String> split(String input, String delimeters) {
        List<String> result = new ArrayList<String>();
        int j=0;
        for (;;) {
            int i = nextNonDelimeter(input, j, delimeters);
            if (i < 0)
                return result;

            j = nextDelimeter(input, i + 1, delimeters);
            if (j > i) {
                result.add(input.substring(i, j));
            } else {
                result.add(input.substring(i));
                return result;
            }
        }
    }

    private int nextDelimeter(String input, int startIndex, String delimeters) {
        for (int i=startIndex; i<input.length(); ++i) {
            if (delimeters.indexOf(input.charAt(i)) >= 0)
                return i;
        }
        return -1;
    }
    private int nextNonDelimeter(String input, int startIndex, String delimeters) {
        for (int i=startIndex; i<input.length(); ++i) {
            if (delimeters.indexOf(input.charAt(i)) < 0)
                return i;
        }
        return -1;
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

package com.adventofcode.december10;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ChunkLine {

    private String chunkLine;
    private Stack<Character> chunkStack;
    private Map<Character, Character> openToClose;
    private Map<Character, Long> errorPoint;
    private Map<Character, Long> completionPoint;

    public ChunkLine(String line) {
        chunkLine = line;
        chunkStack = new Stack<>();

        openToClose = new HashMap<>();
        openToClose.put('(', ')');
        openToClose.put('[', ']');
        openToClose.put('{', '}');
        openToClose.put('<', '>');

        errorPoint = new HashMap<>();
        errorPoint.put(')', 3L);
        errorPoint.put(']', 57L);
        errorPoint.put('}', 1197L);
        errorPoint.put('>', 25137L);

        completionPoint = new HashMap<>();
        completionPoint.put(')', 1L);
        completionPoint.put(']', 2L);
        completionPoint.put('}', 3L);
        completionPoint.put('>', 4L);
    }

    public long syntaxErrorScore() {
        for (int i = 0; i< chunkLine.length(); ++i) {
            char ch = chunkLine.charAt(i);
            if (openToClose.containsKey(ch)) {
                chunkStack.push(ch);
            } else if (openToClose.containsValue(ch)) {
                char lastChunkOpener = chunkStack.pop();
                char expectedCloser = openToClose.get(lastChunkOpener);
                if (ch != expectedCloser)
                    return errorPoint.get(ch);
            } else {
                throw new RuntimeException("should not be possible");
            }
        }
        return 0;
    }

    public long completionScore() {
        for (int i = 0; i< chunkLine.length(); ++i) {
            char ch = chunkLine.charAt(i);
            if (openToClose.containsKey(ch)) {
                chunkStack.push(ch);
            } else if (openToClose.containsValue(ch)) {
                char lastChunkOpener = chunkStack.pop();
                char expectedCloser = openToClose.get(lastChunkOpener);
                if (ch != expectedCloser)
                    return -1;
            } else {
                throw new RuntimeException("should not be possible");
            }
        }
        return stackScore();
    }

    private long stackScore() {
        long sum = 0;
        while (!chunkStack.isEmpty()) {
            char lastChunkOpener = chunkStack.pop();
            char expectedCloser = openToClose.get(lastChunkOpener);
            sum = 5*sum + completionPoint.get(expectedCloser);
        }
        return sum;
    }
}

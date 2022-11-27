package com.adventofcode.december14;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Polymer {

    private String polymerTemplate;
    private Map<String, String> pairMap = new HashMap<>();
    private Map<String, Long> pairCount = new HashMap<>();

    public Polymer(List<String> inputLineList) {
        int lineCount = 0;
        for (String line: inputLineList) {
            if (lineCount == 0) {
                polymerTemplate = line;
            } else {
                if (!line.isEmpty()) {
                    //"CH -> B"
                    String[] fromTo = line.split("\\s\\-\\>\\s");
                    pairMap.put(fromTo[0], fromTo[1]);
                    pairCount.put(fromTo[0], 0L);
                }
            }
            ++lineCount;
        }
    }

    public long runProcessesAlternative(int numberOfprocesses) {
        doInitialProcessStep();
        for (int i=0; i<numberOfprocesses; ++i) {
            doProcessStepAlternative();
        }
        return calculateResultAlternative();
    }


    private void doInitialProcessStep() {
        clearCount(pairCount);
        for (int i=0; i<polymerTemplate.length()-1; ++i) {
            String pair = polymerTemplate.substring(i, i+2);
            pairCount.put(pair, pairCount.get(pair) + 1L);
        }
    }

    private void clearCount(Map<String, Long> pairCountMap) {
        for (String key: pairMap.keySet()) {
            pairCountMap.put(key, 0L);
        }
    }


//    Template:     NNCB
//    After step 1: NCNBCHB
//    After step 2: NBCCNBBBCBHCB
//    After step 3: NBBBCNCCNBBNBNBBCHBHHBCHB

    private void doProcessStepAlternative() {
        Map<String, Long> newPairCount = new HashMap<>();
        clearCount(newPairCount);

        for (String pair: pairCount.keySet()) {
            long currentPairCount = pairCount.containsKey(pair) ? pairCount.get(pair) : 0;
            if (currentPairCount > 0) {
                String pair1 = pair.charAt(0) + pairMap.get(pair);
                String pair2 = pairMap.get(pair) + pair.charAt(1);
                newPairCount.put(pair1, newPairCount.get(pair1) + currentPairCount);
                newPairCount.put(pair2, newPairCount.get(pair2) + currentPairCount);
            }
        }
        pairCount = newPairCount;
    }

    private long calculateResultAlternative() {
        char firstLetter = polymerTemplate.charAt(0);
        char lastLetter = polymerTemplate.charAt(polymerTemplate.length()-1);

        Map<Character, Long> charCount = new HashMap<>();
        for (String pair: pairCount.keySet()) {
            increaseCharCount(charCount, pair.charAt(0), pairCount.get(pair));
            increaseCharCount(charCount, pair.charAt(1), pairCount.get(pair));
        }
        for (Character ch: charCount.keySet()) {
            long counted = charCount.get(ch);
            if (ch == firstLetter || ch == lastLetter) {
                if (counted % 2 != 1) {
                    throw new RuntimeException("expected odd number of '" + ch + "'s here");
                }
                charCount.put(ch, 1 + ((counted - 1) / 2));
            } else {
                if (counted % 2 != 0) {
                    throw new RuntimeException("expected even number of '" + ch + "'s here");
                }
                charCount.put(ch, counted / 2);
            }
        }
        long min = charCount.values().stream().min(Comparator.naturalOrder()).orElse(0L);
        long max = charCount.values().stream().max(Comparator.naturalOrder()).orElse(0L);
        return max - min;
    }

    private void increaseCharCount(Map<Character, Long> charCount, char ch, long increasedBy) {
        if (!charCount.containsKey(ch)) {
            charCount.put(ch, increasedBy);
        } else {
            charCount.put(ch, charCount.get(ch) + increasedBy);
        }
    }



    public long runProcesses(int numberOfprocesses) {
        for (int i=0; i<numberOfprocesses; ++i) {
            System.out.print("running step " + i + " ...");
            doProcessStep();
            System.out.println("resultLength = " + polymerTemplate.length());
        }
        return calculateResult();
    }

    private void doProcessStep() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<polymerTemplate.length()-1; ++i) {
            String pair = polymerTemplate.substring(i, i+2);
            sb.append(polymerTemplate.charAt(i) + pairMap.get(pair));
        }
        sb.append(polymerTemplate.charAt(polymerTemplate.length()-1));
        polymerTemplate = sb.toString();
    }


    private long calculateResult() {
        Map<Character, Long> countChars = new HashMap<>();
        for (int i=0; i<polymerTemplate.length(); ++i) {
            Character ch = polymerTemplate.charAt(i);
            if (!countChars.containsKey(ch)) {
                countChars.put(ch, 0L);
            }
            countChars.put(ch, countChars.get(ch) + 1L);
        }
        long min = countChars.values().stream().min(Comparator.naturalOrder()).orElse(0L);
        long max = countChars.values().stream().max(Comparator.naturalOrder()).orElse(0L);
        return max - min;
    }


}

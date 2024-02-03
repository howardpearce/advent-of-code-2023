/**
 * Day 1: Trebuchet?!
 *
 * @Author Howard Pearce
 * @Date December 23, 2023
 */

package day.one;

import day.AdventOfCodeSolution;

public class Trebuchet extends AdventOfCodeSolution {

    static final String[] NUMBERS_AS_STRINGS = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    static final String PARSE_LINE_ERROR_MSG = "Unable to parse calibration value. Exception message follows: %s";

    public Trebuchet(String day) {
        super(day);
    }

    public static void main(String[] args) {
        Trebuchet solution = new Trebuchet("one");
        solution.solve();
    }

    @Override
    public void solvePartOne() {
        for (String line : input) {
            debugLog("Iterating over line " + line);
            try {
                int value = getCalibrationValue(line);
                debugLog("Got value of: " + value);
                addTotal(value);
            } catch (NumberFormatException e) {
                System.out.println(String.format(PARSE_LINE_ERROR_MSG, e.getMessage()));
            }
        }
        System.out.println("Final total ( part 1 ): " + getTotal());
    }

    @Override
    public void solvePartTwo() {
        for (String line : input) {
            debugLog("Iterating over line " + line);
            try {
                line = parseNumericStrings(line);
                debugLog("Line was converted to " + line);
                int value = getCalibrationValue(line);
                debugLog("Got value of: " + value);
                addTotal(value);
            } catch (NumberFormatException e) {
                System.out.println(String.format(PARSE_LINE_ERROR_MSG, e.getMessage()));
            }
        }
        System.out.println("Final total ( part 2 ): " + getTotal());
    }

    /**
     * Given a line of input, replace occurrences of strings such as 'one' with integer value equivalent. i.e. 'two' --> 2
     * Note: eightwothree should parse to 823!
     * @param line line of input from file
     * @return A sanitized version of the input
     */
    static String parseNumericStrings(String line) {
        // Note: This is hacky. Correct thing would be to parse the old string, and build a new one off that.
        //       Replacing elements in the existing string causes the 'eightwo' problem where you get '8wo'. If
        //       you simply parse the original to build a new one, you shouldn't run into this problem. I too much
        //       time finding this out, so I took the easy way out here since I don't have unlimited time.
        for (int i = 0; i < NUMBERS_AS_STRINGS.length; i++) {
            switch (NUMBERS_AS_STRINGS[i]) {
                case "one": line = line.replace("one", "o1e"); break;
                case "two": line = line.replace("two", "t2o"); break;
                case "three": line = line.replace("three", "t3e"); break;
                case "four": line = line.replace("four", "f4r"); break;
                case "five": line = line.replace("five", "f5e"); break;
                case "six": line = line.replace("six", "s6x"); break;
                case "seven": line = line.replace("seven", "s7n"); break;
                case "eight": line = line.replace("eight", "e8t"); break;
                case "nine": line = line.replace("nine", "n9e"); break;
                default:
            }
        }
        return line;
    }

    /**
     * Given a line of input, parse what the calibration value should be.
     * @param input line of input from file
     * @return calibration value for this line
     * @throws NumberFormatException If a calibration value is incorrectly parsed
     */
    static int getCalibrationValue(String input) throws NumberFormatException {
        Character first = null, last = null;
        for (char s : input.toCharArray()) {
            if (Character.isDigit(s) && first == null) {
                first = s;
            } else if (Character.isDigit(s)) {
                last = s;
            }
        }
        if (last == null) {
            return Integer.parseInt(String.valueOf(first) + first);
        } else {
            return Integer.parseInt(String.valueOf(first) + last);
        }
    }
}
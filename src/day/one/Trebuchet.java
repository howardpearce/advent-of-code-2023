/**
 * Day 1: Trebuchet?!
 *
 * Problem statement:
 *
 * Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.
 *
 * You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * You try to ask why they can't just use a weather machine ("not powerful enough") and where they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot of questions") and hang on did you just say the sky ("of course, where do you think snow comes from") when you realize that the Elves are already loading you into a trebuchet ("please hold still, we need to strap you in").
 *
 * As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are having trouble reading the values on the document.
 *
 * The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.
 *
 * Consider your entire calibration document. What is the sum of all of the calibration values?
 *
 * @Author Howard Pearce
 * @Date December 23, 2023
 */

package day.one;

import day.AdventOfCodeSolution;
import day.three.GearRatios;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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
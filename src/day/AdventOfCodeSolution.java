package day;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AdventOfCodeSolution {

    public static final boolean DEBUG_FLAG = false;
    public static int total = 0;
    public static final String FILE_OPERATIONS_ERROR_MSG = "Exception occurred during file operations. Exception message: %s";
    public static final String FILENAME = "input.txt";
    public List<String> input;

    public abstract void solvePartOne();
    public abstract void solvePartTwo();
    public void solve() {
        solvePartOne();
        total = 0;
        solvePartTwo();
    }

    public AdventOfCodeSolution(String day) {
        input = getInput(System.getProperty("user.dir") + "/src/day/" + day + "/" + FILENAME);
    }

    public ArrayList<String> getInput(String filepath) {
        File inputFile = new File(filepath);
        try {
            ArrayList<String> input = new ArrayList<>();
            Scanner scan = new Scanner(inputFile);
            while (scan.hasNextLine()) {
                input.add(scan.nextLine());
            }
            return input;
        } catch (IOException e) {
            System.out.println(String.format(FILE_OPERATIONS_ERROR_MSG, e.getMessage()));
        }
        return null;
    }

    /**
     * Turns on debug logging for visibility into what code is doing.
     * @param msg
     */
    public static void debugLog(String msg) {
        if (DEBUG_FLAG) {
            System.out.println(String.format("DEBUG: %s", msg));
        }
    }

    public static void addTotal(int value) {
        total += value;
    }

    protected static int getTotal() {
        return total;
    }

    protected static void printTotal() {
        System.out.println("Final total: " + getTotal());
    }

}
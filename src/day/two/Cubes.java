/**
 * Day 2: Cube Conundrum
 *
 * @Author Howard Pearce
 * @Date December 23, 2023
 * */

package day.two;

import day.AdventOfCodeSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cubes extends AdventOfCodeSolution {

    static final int GAME_ID_STRING_INDEX = 5;

    public Cubes(String day) {
        super(day);
    }

    public static void main(String[] args) throws Exception {
        Cubes solution = new Cubes("two");
        solution.solve();
    }

    @Override
    public void solvePartOne() {
        for (String line : input) {
            debugLog("Iterating over line " + line);
            String gameId = getGameId(line);
            line = line.substring(line.indexOf(gameId)+gameId.length()+2);
            String[] games = line.split(";");

            Game g = new Game();
            g.setRules(12,14,13);
            for(String game : games) {
                g.parseGame(game);
            }
            if(g.followsRules()) {
                addTotal(Integer.valueOf(gameId));
            }
        }
        System.out.println("Final total ( part 1 ): " + getTotal());
    }

    @Override
    public void solvePartTwo() {
        for (String line : input) {
            debugLog("Iterating over line " + line);
            String gameId = getGameId(line);
            line = line.substring(line.indexOf(gameId)+gameId.length()+2);
            String[] games = line.split(";");

            Game g = new Game();
            g.setRules(12,14,13);
            for(String game : games) {
                g.parseGame(game);
            }

            debugLog("Min red:" + g.getMinCubes(Color.RED));
            debugLog("Min blue:" + g.getMinCubes(Color.BLUE));
            debugLog("Min green:" + g.getMinCubes(Color.GREEN));
            addTotal(g.getMinCubes(Color.RED) *  g.getMinCubes(Color.BLUE) * g.getMinCubes(Color.GREEN));

        }
        System.out.println("Final total ( part 2 ): " + getTotal());
    }

    /**
     * Given a Game, retrieve its corresponding ID as a string. Assumes each string is of the form 'Game X:'
     * @param input the string containing the game
     * @return The ID for that game as a string
     */
    public static String getGameId(String input) {
        StringBuilder gameId = new StringBuilder();
        int modifier = 0;

        while(input.charAt(GAME_ID_STRING_INDEX +modifier) !=':') {
            gameId.append(input.charAt(GAME_ID_STRING_INDEX +modifier));
            modifier++;
        }

        return gameId.toString();
    }
}

/**
 * Represents a cube's color
 */
enum Color {
    RED("RED"),
    GREEN("GREEN"),
    BLUE("BLUE");

    private final String color;
    Color(String color) {
        this.color = color;
    }

    /**
     * Get a color from a string representation of a color
     * @param colorString the string representation
     * @return a Color corresponding to that string.
     */
    public static Color getColor(String colorString) {
        colorString = colorString.toUpperCase();
        if (colorString.equals(Color.BLUE.toString())) {
            return Color.BLUE;
        } else if (colorString.equals(Color.GREEN.toString())) {
            return Color.GREEN;
        } else if (colorString.equals(Color.RED.toString())) {
            return Color.RED;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.color;
    }
}

/**
 * Represents a single game, where a number of cubes of different colors are pulled from a bag.
 */
class Game {

    public int id, redLimit, blueLimit, greenLimit;
    public ArrayList<Cube> cubes;

    public final Pattern CUBE_PATTERN = Pattern.compile("(\\d+)\\s(\\w+)");

    public Game(int id, ArrayList<Cube> cubes) {
        this.id = id;
        this.cubes = cubes;
    }

    public Game() {
        this.cubes = new ArrayList<Cube>();
    }

    /**
     * Set the rules observed for this game
     * @param redLimit the max number of red cubes seen
     * @param blueLimit the max number of blue cubes seen
     * @param greenLimit the max number of green cubes seen
     */
    public void setRules(int redLimit, int blueLimit, int greenLimit) {
        this.redLimit = redLimit;
        this.blueLimit = blueLimit;
        this.greenLimit = greenLimit;
    }

    /**
     * Given all the games we have seen, are the rules observed?
     * @return if the game follows the set rules
     * @throws Exception if a color was observed that is not expected
     */
    public boolean followsRules() {
        boolean follows = true;
        for(Cube cube : this.cubes) {
            int limit = 0;
            switch (cube.color) {
                case BLUE:
                    limit = blueLimit;
                    break;
                case GREEN:
                    limit = greenLimit;
                    break;
                case RED:
                    limit = redLimit;
                    break;
                default:
                    System.out.print("No limit was found this time!");
            }
            follows = follows && cube.count <= limit;
        }
        return follows;
    }

    /**
     * For a color, get the maxmimum number of times it occurred in a game
     * @param color the color we wish to count
     * @return the minimum number of cubes required to pass a game
     */
    int getMinCubes(Color color) {
        ArrayList<Integer> counts = new ArrayList<Integer>();
        for (Cube cube : this.cubes) {
            if (cube.color == color) {
                counts.add(cube.count);
            }
        }
        return Collections.max(counts);
    }

    /**
     * Parse a string representation of a game
     * @param gameText
     */
    public void parseGame(String gameText) {
        String[] cubes = gameText.split(",");
        for(String cubeString : cubes) {
            this.cubes.add(createCube(cubeString));
        }
    }

    /**
     * Initialize a cube given its description (ex '1 green')
     * @param gameText
     * @return A Cube that models the string representation of the cube
     */
    public Cube createCube(String gameText) {
        Matcher m = CUBE_PATTERN.matcher(gameText);
        m.find();
        int count = Integer.parseInt(m.group(1));
        Color color = Color.getColor(m.group(2));
        return new Cube(count, color);
    }
}

/**
 * Represents a colored cube in the game described by this problem
 */
class Cube {
    public Color color;
    public int count;

    public Cube(int count, Color color) {
        this.count = count;
        this.color = color;
    }
}

/**
 * You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside.
 *
 * It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.
 *
 * "Aaah!"
 *
 * You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.
 *
 * The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.
 *
 * The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 *
 * 467..114..
 * ...*......
 * ..35..633.
 * ......#...
 * 617*......
 * .....+.58.
 * ..592.....
 * ......755.
 * ...$.*....
 * .664.598..
 *
 * In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.
 *
 * Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?
 *
 */

package day.three;

import day.AdventOfCodeSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static day.AdventOfCodeSolution.debugLog;


public class GearRatios extends AdventOfCodeSolution {
    public GearRatios(String day) {
        super(day);
    }

    public static void main(String[] args) {
        GearRatios solution = new GearRatios("three");
        solution.solve();
    }

    @Override
    public void solvePartOne() {
        Schematic schematic = new Schematic(input);
        List<Part> parts = schematic.getValidParts();
        for (Part part : parts) {
            addTotal(Integer.parseInt(part.id));
        }

        System.out.println("Final total ( part 1 ): " + getTotal());
    }

    @Override
    public void solvePartTwo() {
        Schematic schematic = new Schematic(input);
        List<Part> parts = schematic.getValidParts();
        for (GearRatio ratio : schematic.gearRatios) {
            addTotal(ratio.getRatio());
        }
        System.out.println("Final total ( part 2 ): " + getTotal());
    }
}

/**
 * Implements and contains a model of the Schematic provided in the question
 */
class Schematic {

    protected String[] SPECIAL_CHARACTERS = {"&", "*", "#", "=", "+", "$", "%", "/", "@", "^", "-", ")", "("};
    protected List<String> schematic;
    private List<Part> ratioParts;
    protected List<GearRatio> gearRatios;
    protected List<Part> parts;
    public Schematic(List<String> schematic) {
        this.schematic = schematic;
        this.parts = new ArrayList<>();
        this.ratioParts = new ArrayList<>();
        this.gearRatios = new ArrayList<>();
    }

    /**
     * Iterates over the schematic and extracts parts that have a symbol adjacent to them.
     */
    public List<Part> getValidParts() {
        // iterate over the schematic and discover all the parts
        for (int col = 0; col < this.schematic.size(); col++ ) {
            String line = this.schematic.get(col);
            // remove special characters. they don't matter during part discovery
            for (String c : SPECIAL_CHARACTERS) {
                line = line.replace(c,".");
            }
            String currentPart = "";
            boolean foundPart = false;

            // iterate over the line and discover all the parts
            for (int index = 0; index <= line.length(); index++) {
                char currentPosition = this.getSymbolAsChar(index, col);

                if (currentPosition != '.' && !isSymbol(currentPosition)) {
                    foundPart = true;
                    currentPart += line.charAt(index);
                }

                // finish with this part
                if (foundPart == true && (currentPosition == '.' || isSymbol(currentPosition))) {
                    foundPart = false;
                    int partStart = index - currentPart.length();
                    validatePart(partStart, col, currentPart);
                    currentPart = "";
                }
            }
        }
        return this.parts;
    }

    /**
     * Create a part and check if it is valid. Add it to the list of valid parts if so.
     * @param x x position of the part
     * @param y y position of the part
     * @param id part ID
     */
    public void validatePart(int x, int y, String id) {
        Part newPart = new Part(x, y, id);
        if (hasAdjacentSymbol(newPart)) {
            this.parts.add(newPart);
        }
    }

    /**
     * Does the provided part have an adjacent symbol?
     * @param p the part to check
     * @return true of the part has an adjacent symbol. False otherwise.
     */
    public boolean hasAdjacentSymbol(Part p) {
        int length = p.id.length();
        for(int i = -1; i <= length; i++) {
            String firstRow = getSymbol(p.x+i, p.y+1);
            String secondRow = getSymbol(p.x+i, p.y);
            String thirdRow = getSymbol(p.x+i, p.y-1);
            if ( isSymbol(firstRow) | isSymbol(secondRow) | isSymbol(thirdRow) ) {
                if ((firstRow + secondRow + thirdRow).contains("*")) {
                    p.hasAdjacentAsterisk = true;
                    displayPart(p);
                }
                List<Part> parts = null;
                if (firstRow.equals("*")) {
                    parts = findPartsAroundPoint(p.x+i, p.y+1);
                }
                if (secondRow.equals("*")) {
                    parts = findPartsAroundPoint(p.x+i, p.y);
                }
                if (thirdRow.equals("*")) {
                    parts = findPartsAroundPoint(p.x+i, p.y-1);
                }
                if (parts != null && parts.size() == 2)
                    if (!this.ratioParts.contains(parts.get(0)) && !this.ratioParts.contains(parts.get(1))) {
                        this.ratioParts.add(parts.get(0));
                        this.ratioParts.add(parts.get(1));
                        GearRatio r = new GearRatio(parts.get(0), parts.get(1));
                        gearRatios.add(r);
                    }
                return true;
            }
        }
        return false;
    }

    /**
     * Return all parts that can be found around a given position in the schematic
     * @param xPos x position
     * @param yPos y position
     * @return a list of parts
     */
    public List<Part> findPartsAroundPoint(int xPos, int yPos) {
        ArrayList<Part> parts = new ArrayList<>();
        for(int y = yPos - 1; y <= yPos+1; y++) {
            for (int x = xPos-1; x <= xPos+1; x++) {
                String tempSymbol = getSymbol(x,y);
                if (isDigit(tempSymbol)) {
                    String newPartId = "";
                    int index = x;
                    // get the right hand side of the part
                    while(isDigit(getSymbol(index, y))) {
                        newPartId += getSymbol(index, y);
                        index++;
                    }
                    index = x-1;
                    while(isDigit(getSymbol(index, y))) {
                        newPartId = getSymbol(index, y) + newPartId;
                        index--;
                    }
                    parts.add(new Part(index, y, newPartId, true));
                }
            }
        }
        return parts.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Print out a visual representation of a part for debugging
     * @param p part to print
     */
    public void displayPart(Part p) {
        if (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            for(int y = p.y - 1; y <= p.y+1; y++) {
                for (int x = p.x-1; x <= p.x+p.id.length(); x++) {
                    sb.append(getSymbol(x, y));
                }
                sb.append("\n");
            }
            debugLog(p.toString() + sb.toString());
        }
    }

    /**
     * Return the symbol at the indicated position in the schematic. Out of bounds positions return as '*'
     * @param x x position
     * @param y y position
     * @return the symbol at the indicated position
     */
    public String getSymbol(int x, int y) {
        try {
            return this.schematic.get(y).substring(x, x + 1);
        } catch (IndexOutOfBoundsException e) {
            return ".";
        }
    }

    /**
     * Return the symbol at the indicated position in the schematic. Out of bounds positions return as '*'
     * @param x x position
     * @param y y position
     * @return the symbol at the indicated position
     */
    public char getSymbolAsChar(int x, int y) {
        try {
            return this.schematic.get(y).charAt(x);
        } catch (IndexOutOfBoundsException e) {
            return '.';
        }
    }

    /**
     * Checks if the provided character is a symbol.
     * @param symbol the symbol to check
     * @return true if it is a symbol. False otherwise.
     */
    public boolean isSymbol(String symbol) {
        if (symbol.length() > 1 || symbol.length() == 0) {
            System.out.println("Unable to work with string that isn't size one: " + symbol);
        }
        return Arrays.stream(SPECIAL_CHARACTERS).anyMatch(symbol::equals);
    }

    /**
     * Is the provided string a symbol? Assumes String of size 1.
     * @param symbol
     * @return true if the provided string is a symbol.
     */
    public boolean isDigit(String symbol) {
        return symbol.chars().allMatch(Character::isDigit);
    }

    /**
     * Is the provided character a symbol? Assumes String of size 1.
     * @param symbol
     * @return true if the provided char is a symbol.
     */
    public boolean isSymbol(char symbol) {
        return isSymbol(String.valueOf(symbol));
    }
}

/**
 * Simple object representation of a Gear Ratio
 */
class GearRatio {
    private Part one;
    private Part two;

    public GearRatio(Part one, Part two) {
        this.one = one;
        this.two = two;
    }

    int getRatio(){
        return one.getId() * two.getId();
    }
}

/**
 * Simple object representation of a part in the engine.
 * Just holds the necessary data types.
 */
class Part {
    protected int x,y;
    protected String id;
    protected boolean hasAdjacentAsterisk;
    public Part (int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.hasAdjacentAsterisk = false;
    }

    public Part (int x, int y, String id, boolean hasAdjacentAsterisk) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.hasAdjacentAsterisk = hasAdjacentAsterisk;
    }

    public int getId() {
        return Integer.parseInt(this.id);
    }

    @Override
    public String toString() {
        return "Part{ X: " + this.x + ", Y: " + this.y + ", id: " + this.id + ", hasAdjacentAsterisk: " + this.hasAdjacentAsterisk +  "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return x == part.x && y == part.y && hasAdjacentAsterisk == part.hasAdjacentAsterisk && id.equals(part.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, id, hasAdjacentAsterisk);
    }
}

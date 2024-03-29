/**
 * Day 4: Scratchcards
 *
 * @Author Howard Pearce
 * @Date February 2, 2024
 */
package day.four;

import day.AdventOfCodeSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scratchcards extends AdventOfCodeSolution {

    public Scratchcards(String day) {
        super(day);
    }

    public static void main(String[] args) {
        Scratchcards solution = new Scratchcards("four");
        solution.solve();
    }

    public void solvePartOne() {
        ScratchGame game = new ScratchGame(input);
        for (Scratchcard s : game.cards) {
            addTotal(s.getPartOneScore());
        }
        System.out.println("Solution ( part 1 ): " + total);
    }

    public void solvePartTwo() {
        ScratchGame game = new ScratchGame(input);
        System.out.println("Solution ( part 2 ): " + game.play().size());
    }
}

/**
 * Represents a list of scratchcards
 */
class ScratchGame {
    List<Scratchcard> cards = new ArrayList<>();
    public ScratchGame(List<String> input) {
        for(String line : input) {
            cards.add(new Scratchcard(line));
        }
    }

    /**
     * Kick off the scratchcard game from part 2
     * @return a list of scratchcard games as requested in part 2
     */
    public List<Scratchcard> play() {
        List<Scratchcard> gameResults = new ArrayList<>();
        for (Scratchcard card : cards) {
            gameResults.add(card.clone());
        }

        playGame(gameResults, gameResults);
        return gameResults;
    }

    /**
     * Recursively play the game and all sub-games that occur as a result
     * @param game the current game to play
     * @param gameResults where to store the results of the game
     */
    public void playGame(List<Scratchcard> game, List<Scratchcard> gameResults) {
        List<Scratchcard> subgame = new ArrayList<>();
        for (int i = 0; i < game.size(); i++) {
            Scratchcard c = game.get(i);
            if (!c.counted) {
                c.counted = true;
                for (int j = c.id; j < c.id + c.matches; j++) {
                    Scratchcard newCard = cards.get(j).clone();
                    gameResults.add(newCard);
                    subgame.add(newCard);
                }
                if (c.matches != 0) {
                    playGame(subgame, gameResults);
                }
            }
        }
    }
}

/**
 * Represents a single scratchcard
 */
class Scratchcard implements Cloneable {

    public Integer id;
    private final String ID_REGEX = "Card \\s*(\\d+)";
    protected List<Integer> winningNumbers = new ArrayList<>();
    protected List<Integer> scratchNumbers = new ArrayList<>();
    public List<Integer> matchingNumbers;
    public int matches = 0;

    public boolean counted = false;

    public Scratchcard(String game) {
        String[] temp = game.split(":");
        this.id = getGameId(temp[0].trim());
        String gameString = temp[1].trim();
        extractGameInfo(gameString.split("\\|")[0], winningNumbers);
        extractGameInfo(gameString.split("\\|")[1], scratchNumbers);
        this.matchingNumbers = this.winningNumbers.stream().filter(x -> scratchNumbers.stream().anyMatch(y -> y == x)).toList();
        this.matches = this.matchingNumbers.size();
    }

    public Scratchcard(Integer id, List<Integer> winningNumbers, List<Integer> scratchNumbers, List<Integer> matchingNumbers, int matches) {
        this.id = id;
        this.winningNumbers = winningNumbers;
        this.scratchNumbers = scratchNumbers;
        this.matchingNumbers = matchingNumbers;
        this.matches = matches;
    }

    /**
     * Extract the Game ID from a string like 'Game 3'
     * @param idString
     * @return ID as an integer
     */
    private Integer getGameId(String idString) {
        Pattern r = Pattern.compile(ID_REGEX);
        Matcher m = r.matcher(idString);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        } else {
            return null;
        }
    }

    /**
     * Parse the numbers from the input text into a list of integers
     * @param gameInfo
     * @param resultsContainer
     */
    public void extractGameInfo(String gameInfo, List<Integer> resultsContainer) {
        for (String s : gameInfo.split(" ")) {
            if (!s.equals("") && !s.equals(" ")) {
                resultsContainer.add(Integer.parseInt(s));
            }
        }
    }

    /**
     * Return the score as described in part 1 of this problem
     * @return part 1 score for this scratch card
     */
    public int getPartOneScore() {
        return (int) Math.pow(2, matches-1);
    }

    public Scratchcard clone() {
        ArrayList<Integer> winningNumbers = new ArrayList<>(this.winningNumbers);
        ArrayList<Integer> scratchNumbers = new ArrayList<>(this.scratchNumbers);
        ArrayList<Integer> matchingNumbers = new ArrayList<>(this.matchingNumbers);
        return new Scratchcard(this.id, winningNumbers, scratchNumbers, matchingNumbers, this.matches);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scratchcard that = (Scratchcard) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
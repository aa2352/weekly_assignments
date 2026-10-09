import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class RockPaperScissorsArcade {

    private static final int TOTAL_ROUNDS = 5;
    private static final String[] VALID_MOVES = { "Rock", "Paper", "Scissors" };
    private static final String[] DEMO_PLAYER_MOVES = { "Rock", "Paper", "Scissors", "Rock", "paper" };
    private static final String RESULT_PLAYER_WINS = "Player Wins";
    private static final String RESULT_COMPUTER_WINS = "Computer Wins";
    private static final String RESULT_DRAW = "Draw";

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] playerMoves = readPlayerMoves(inputReader, TOTAL_ROUNDS);
        String[] computerMoves = generateComputerMoves(TOTAL_ROUNDS);
        String[] roundResults = playAllRounds(playerMoves, computerMoves);

        printRoundTable(playerMoves, computerMoves, roundResults);
        printFinalSummary(roundResults);

        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== The College Coding Arcade ===");
        System.out.println("\tGame:\t\"Rock-Paper-Scissors\"");
        System.out.println("\tRounds:\t" + TOTAL_ROUNDS);
        System.out.println("\tPath:\tC:\\arcade\\orientation-day\n");
    }

    private static String[] readPlayerMoves(Scanner inputReader, int totalRounds) {
        String[] playerMoves = new String[totalRounds];
        System.out.println("Enter your move for each round (Rock / Paper / Scissors):");

        for (int roundIndex = 0; roundIndex < totalRounds; roundIndex++) {
            String fallbackMove = DEMO_PLAYER_MOVES[roundIndex % DEMO_PLAYER_MOVES.length];
            System.out.print("Round " + (roundIndex + 1) + " move: ");
            try {
                String typedMove = inputReader.nextLine();
                playerMoves[roundIndex] = normaliseMove(typedMove);
            } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
                playerMoves[roundIndex] = fallbackMove.trim();
                System.out.println("[no console input - using demo move \"" + playerMoves[roundIndex] + "\"]");
            } catch (InvalidMoveException invalidMove) {
                playerMoves[roundIndex] = fallbackMove.trim();
                System.out.println("[" + invalidMove.getMessage()
                        + " - using demo move \"" + playerMoves[roundIndex] + "\"]");
            } catch (IllegalArgumentException blankMove) {
                playerMoves[roundIndex] = fallbackMove.trim();
                System.out.println("[" + blankMove.getMessage()
                        + " - using demo move \"" + playerMoves[roundIndex] + "\"]");
            }
        }
        System.out.println();
        return playerMoves;
    }

    private static String normaliseMove(String rawMove) throws InvalidMoveException {
        if (rawMove == null || rawMove.trim().isEmpty()) {
            throw new IllegalArgumentException("Move cannot be empty");
        }
        String cleanedMove = rawMove.trim();
        for (int moveIndex = 0; moveIndex < VALID_MOVES.length; moveIndex++) {
            if (VALID_MOVES[moveIndex].equalsIgnoreCase(cleanedMove)) {
                return VALID_MOVES[moveIndex];
            }
        }
        throw new InvalidMoveException("Unrecognised move \"" + cleanedMove + "\"");
    }

    private static String[] generateComputerMoves(int totalRounds) {
        Random moveGenerator = new Random();
        String[] computerMoves = new String[totalRounds];
        for (int roundIndex = 0; roundIndex < totalRounds; roundIndex++) {
            computerMoves[roundIndex] = VALID_MOVES[moveGenerator.nextInt(VALID_MOVES.length)];
        }
        return computerMoves;
    }

    private static String[] playAllRounds(String[] playerMoves, String[] computerMoves) {
        if (playerMoves.length != computerMoves.length) {
            throw new IllegalArgumentException("Both players must have the same number of moves");
        }
        String[] roundResults = new String[playerMoves.length];
        for (int roundIndex = 0; roundIndex < playerMoves.length; roundIndex++) {
            roundResults[roundIndex] = playRound(playerMoves[roundIndex], computerMoves[roundIndex]);
        }
        return roundResults;
    }

    private static String playRound(String playerMove, String computerMove) {
        if (playerMove.equalsIgnoreCase(computerMove)) {
            return RESULT_DRAW;
        }
        boolean playerBeatsComputer =
                (playerMove.equalsIgnoreCase("Rock") && computerMove.equalsIgnoreCase("Scissors"))
                        || (playerMove.equalsIgnoreCase("Paper") && computerMove.equalsIgnoreCase("Rock"))
                        || (playerMove.equalsIgnoreCase("Scissors") && computerMove.equalsIgnoreCase("Paper"));
        return playerBeatsComputer ? RESULT_PLAYER_WINS : RESULT_COMPUTER_WINS;
    }

    private static void printRoundTable(String[] playerMoves, String[] computerMoves, String[] roundResults) {
        System.out.println("Round | Player Move | Computer Move | Result");
        System.out.println("------------------------------------------------");
        for (int roundIndex = 0; roundIndex < roundResults.length; roundIndex++) {
            System.out.printf("%5d | %-11s | %-13s | %s%n",
                    roundIndex + 1, playerMoves[roundIndex], computerMoves[roundIndex], roundResults[roundIndex]);
        }
        System.out.println();
    }

    private static int countResult(String[] roundResults, String wantedResult) {
        int matchCount = 0;
        for (int roundIndex = 0; roundIndex < roundResults.length; roundIndex++) {
            if (roundResults[roundIndex].equals(wantedResult)) {
                matchCount++;
            }
        }
        return matchCount;
    }

    private static double calculateWinPercentage(int winCount, int totalRounds) {
        if (totalRounds <= 0) {
            throw new ArithmeticException("Win percentage needs at least one round");
        }
        return (winCount * 100.0) / totalRounds;
    }

    private static void printFinalSummary(String[] roundResults) {
        int winCount = countResult(roundResults, RESULT_PLAYER_WINS);
        int lossCount = countResult(roundResults, RESULT_COMPUTER_WINS);
        int drawCount = countResult(roundResults, RESULT_DRAW);

        System.out.println("Final Summary (after " + roundResults.length + " rounds)");
        try {
            double winPercentage = calculateWinPercentage(winCount, roundResults.length);
            System.out.printf("Wins: %d | Losses: %d | Draws: %d | Win %% = %.1f%%%n",
                    winCount, lossCount, drawCount, winPercentage);
        } catch (ArithmeticException noRoundsPlayed) {
            System.out.println("Wins: 0 | Losses: 0 | Draws: 0 | Win % = not available ("
                    + noRoundsPlayed.getMessage() + ")");
        }
    }
}

class InvalidMoveException extends Exception {
    InvalidMoveException(String message) {
        super(message);
    }
}

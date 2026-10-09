import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FantasyDraftRankingEngine {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 4;
    private static final int ESTABLISHED_MATCH_CUTOFF = 10;
    private static final int NEWCOMER_MATCH_CUTOFF = 5;
    private static final String RANK_SEPARATOR = " | ";
    private static final String[] DEMO_BATCHES = {
            "Virat,15,48.0,false;Rahul,7,55.0,false;Sameer,3,60.0,false;Dev,12,20.0,true",
            "Ishan,20,35.5,false;Nitin,6,44.0,true;Arjun,9,51.0,false",
            "Manav,2,70.0,false;Sahil,4,66.0,true",
            "Priya,eight,40.0,false",
            "   "
    };
    private static final Object[] DEMO_SCOUTING_ENTRIES = {
            new Player("Zaid", 18, 41.0, false), "scouting note", new Player("Aman", 11, 33.5), Double.valueOf(9.9) };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            reportDraftBoard(batches[batchIndex]);
        }
        reportScoutingDesk();
        System.out.println("\nPlayer records created in this session: " + Player.getPlayersCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Fantasy League Auto-Draft Ranking Engine ===");
        System.out.println("\tInput format:\t\"Virat,15,48.0,false;Rahul,7,55.0,false\"");
        System.out.println("\tEstablished rule:\tmatches >= " + ESTABLISHED_MATCH_CUTOFF + ", fitness ignored");
        System.out.println("\tNewcomer rule:\tmatches >= " + NEWCOMER_MATCH_CUTOFF + " and currently fit");
        System.out.println("\tFantasy points:\tbatting average + matches x 0.5 - injury penalty\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the player batch (press Enter to use the demo list): ");
        try {
            String typedBatch = inputReader.nextLine();
            if (typedBatch.trim().isEmpty()) {
                System.out.println("[no batch typed - running the demo list]\n");
                return DEMO_BATCHES;
            }
            System.out.println();
            return new String[] { typedBatch };
        } catch (NoSuchElementException | IllegalStateException consoleUnavailable) {
            System.out.println("\n[no console input - running the demo list]\n");
            return DEMO_BATCHES;
        }
    }

    private static void reportDraftBoard(String batchLine) {
        System.out.println("players = {" + batchLine + "}");
        try {
            Player[] players = parsePlayers(batchLine);
            System.out.println(draftAndRank(players));
            System.out.println("Players scouted: " + players.length + "\n");
        } catch (NoDraftablePlayerException noDraftablePlayer) {
            System.out.println("Draft board empty: " + noDraftablePlayer.getMessage() + "\n");
        } catch (MalformedPlayerException malformedPlayer) {
            System.out.println("Batch rejected: " + malformedPlayer.getMessage() + "\n");
        } catch (NumberFormatException nonNumericField) {
            System.out.println("Batch rejected: matches and batting average must be numbers\n");
        } catch (IllegalArgumentException emptyBatch) {
            System.out.println("Batch rejected: " + emptyBatch.getMessage() + "\n");
        }
    }

    private static void reportScoutingDesk() {
        System.out.println("Scouting desk entries (Player objects mixed with paperwork):");
        try {
            System.out.println(draftAndRank(DEMO_SCOUTING_ENTRIES));
        } catch (NoDraftablePlayerException noDraftablePlayer) {
            System.out.println("Draft board empty: " + noDraftablePlayer.getMessage());
        }
    }

    static boolean isDraftable(int matchesPlayed) {
        return matchesPlayed >= ESTABLISHED_MATCH_CUTOFF;
    }

    static boolean isDraftable(int matchesPlayed, boolean injured) {
        return matchesPlayed >= NEWCOMER_MATCH_CUTOFF && !injured;
    }

    static String draftAndRank(Player[] players) throws NoDraftablePlayerException {
        int draftableCount = 0;
        for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {
            if (isDrafted(players[playerIndex])) {
                draftableCount++;
            }
        }
        if (draftableCount == 0) {
            throw new NoDraftablePlayerException("no player clears either draft rule");
        }

        Player[] draftable = new Player[draftableCount];
        int draftIndex = 0;
        for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {
            if (isDrafted(players[playerIndex])) {
                draftable[draftIndex] = players[playerIndex];
                draftIndex++;
            }
        }
        Arrays.sort(draftable);

        StringBuilder draftBoard = new StringBuilder();
        StringBuilder pointsBreakdown = new StringBuilder();
        for (int rankIndex = 0; rankIndex < draftable.length; rankIndex++) {
            if (rankIndex > 0) {
                draftBoard.append(RANK_SEPARATOR);
                pointsBreakdown.append(RANK_SEPARATOR);
            }
            draftBoard.append(rankIndex + 1).append(". ").append(draftable[rankIndex].getName());
            pointsBreakdown.append(draftable[rankIndex].describe());
        }
        System.out.println("Fantasy points: " + pointsBreakdown);
        return draftBoard.toString();
    }

    static String draftAndRank(Object[] scoutingEntries) throws NoDraftablePlayerException {
        int playerCount = 0;
        for (int entryIndex = 0; entryIndex < scoutingEntries.length; entryIndex++) {
            if (scoutingEntries[entryIndex] instanceof Player) {
                playerCount++;
            }
        }
        if (playerCount == 0) {
            throw new NoDraftablePlayerException("no entry at the desk is a player record");
        }

        Player[] players = new Player[playerCount];
        int playerIndex = 0;
        for (int entryIndex = 0; entryIndex < scoutingEntries.length; entryIndex++) {
            if (scoutingEntries[entryIndex] instanceof Player) {
                players[playerIndex] = (Player) scoutingEntries[entryIndex];
                playerIndex++;
            }
        }
        System.out.println("Usable player records after instanceof filtering: " + playerCount
                + " of " + scoutingEntries.length);
        return draftAndRank(players);
    }

    private static boolean isDrafted(Player player) {
        return isDraftable(player.getMatchesPlayed())
                || isDraftable(player.getMatchesPlayed(), player.isInjured());
    }

    private static Player[] parsePlayers(String batchLine) throws MalformedPlayerException {
        if (batchLine == null || batchLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the batch holds no players");
        }
        String[] recordFields = batchLine.split(RECORD_SEPARATOR);
        Player[] players = new Player[recordFields.length];
        for (int recordIndex = 0; recordIndex < recordFields.length; recordIndex++) {
            players[recordIndex] = parsePlayer(recordFields[recordIndex]);
        }
        return players;
    }

    private static Player parsePlayer(String recordField) throws MalformedPlayerException {
        String[] playerFields = recordField.split(FIELD_SEPARATOR);
        if (playerFields.length != EXPECTED_FIELD_COUNT) {
            throw new MalformedPlayerException("\"" + recordField.trim()
                    + "\" needs name, matches, batting average and injury flag");
        }
        String name = playerFields[0].trim();
        int matchesPlayed = Integer.parseInt(playerFields[1].trim());
        double battingAverage = Double.parseDouble(playerFields[2].trim());
        boolean injured = Boolean.parseBoolean(playerFields[3].trim());
        return new Player(name, matchesPlayed, battingAverage, injured);
    }
}

class Player implements Comparable<Player> {

    private static final double MATCH_WEIGHT = 0.5;
    private static final double INJURY_PENALTY = 10.0;
    private static final boolean FIT_BY_DEFAULT = false;

    private static int playersCreated = 0;

    private final String name;
    private final int matchesPlayed;
    private final double battingAverage;
    private final boolean injured;

    public Player(String name, int matchesPlayed, double battingAverage) {
        this(name, matchesPlayed, battingAverage, FIT_BY_DEFAULT);
    }

    public Player(String name, int matchesPlayed, double battingAverage, boolean injured) {
        playersCreated++;
        this.name = name;
        this.matchesPlayed = matchesPlayed;
        this.battingAverage = battingAverage;
        this.injured = injured;
    }

    String getName() {
        return this.name;
    }

    int getMatchesPlayed() {
        return this.matchesPlayed;
    }

    double getBattingAverage() {
        return this.battingAverage;
    }

    boolean isInjured() {
        return this.injured;
    }

    double getFantasyPoints() {
        double injuryPenalty = this.injured ? INJURY_PENALTY : 0.0;
        return this.battingAverage + this.matchesPlayed * MATCH_WEIGHT - injuryPenalty;
    }

    String describe() {
        return this.name + " (" + String.format("%.1f", this.getFantasyPoints()) + ")";
    }

    public int compareTo(Player other) {
        return Double.compare(other.getFantasyPoints(), this.getFantasyPoints());
    }

    static int getPlayersCreated() {
        return playersCreated;
    }
}

class NoDraftablePlayerException extends Exception {
    NoDraftablePlayerException(String message) {
        super(message);
    }
}

class MalformedPlayerException extends Exception {
    MalformedPlayerException(String message) {
        super(message);
    }
}

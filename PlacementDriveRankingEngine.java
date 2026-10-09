import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PlacementDriveRankingEngine {

    private static final String RECORD_SEPARATOR = ";";
    private static final String FIELD_SEPARATOR = ",";
    private static final int EXPECTED_FIELD_COUNT = 3;
    private static final double DIRECT_CGPA_CUTOFF = 7.0;
    private static final double BORDERLINE_CGPA_CUTOFF = 6.5;
    private static final int CODING_SCORE_CUTOFF = 60;
    private static final String RANK_SEPARATOR = " | ";
    private static final String[] DEMO_BATCHES = {
            "Aisha,8.2,40;Rohit,6.8,65;Meena,6.0,90;Karan,7.5,20",
            "Nikhil,9.1,85;Sara,7.0,55;Imran,6.6,72",
            "Latha,5.5,95;Vivek,6.4,58",
            "Priya,8.0,notanumber",
            "   "
    };
    private static final Object[] DEMO_MIXED_ENTRIES = {
            new Candidate("Zoya", 8.4, 55), "walk-in form", new Candidate("Arun", 7.1), Integer.valueOf(2026) };

    public static void main(String[] args) {
        printBanner();

        Scanner inputReader = new Scanner(System.in);
        String[] batches = collectBatches(inputReader);
        for (int batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            reportShortlist(batches[batchIndex]);
        }
        reportWalkInDesk();
        System.out.println("\nCandidate records created in this session: " + Candidate.getCandidatesCreated());
        inputReader.close();
    }

    private static void printBanner() {
        System.out.println("=== Placement Drive Shortlisting & Ranking Engine ===");
        System.out.println("\tInput format:\t\"Aisha,8.2,40;Rohit,6.8,65\"");
        System.out.println("\tDirect rule:\tCGPA >= " + DIRECT_CGPA_CUTOFF);
        System.out.println("\tBorderline rule:\tCGPA >= " + BORDERLINE_CGPA_CUTOFF
                + " and coding score >= " + CODING_SCORE_CUTOFF);
        System.out.println("\tComposite score:\tCGPA x 10 + coding score x 0.5\n");
    }

    private static String[] collectBatches(Scanner inputReader) {
        System.out.print("Enter the candidate batch (press Enter to use the demo list): ");
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

    private static void reportShortlist(String batchLine) {
        System.out.println("candidates = {" + batchLine + "}");
        try {
            Candidate[] candidates = parseCandidates(batchLine);
            System.out.println(shortlistAndRank(candidates));
            System.out.println("Applicants reviewed: " + candidates.length + "\n");
        } catch (NoEligibleCandidateException noEligibleCandidate) {
            System.out.println("Shortlist empty: " + noEligibleCandidate.getMessage() + "\n");
        } catch (MalformedCandidateException malformedCandidate) {
            System.out.println("Batch rejected: " + malformedCandidate.getMessage() + "\n");
        } catch (NumberFormatException nonNumericField) {
            System.out.println("Batch rejected: CGPA and coding score must be numbers\n");
        } catch (IllegalArgumentException emptyBatch) {
            System.out.println("Batch rejected: " + emptyBatch.getMessage() + "\n");
        }
    }

    private static void reportWalkInDesk() {
        System.out.println("Walk-in desk entries (Candidate objects mixed with paperwork):");
        try {
            System.out.println(shortlistAndRank(DEMO_MIXED_ENTRIES));
        } catch (NoEligibleCandidateException noEligibleCandidate) {
            System.out.println("Shortlist empty: " + noEligibleCandidate.getMessage());
        }
    }

    static boolean isEligible(double cgpa) {
        return cgpa >= DIRECT_CGPA_CUTOFF;
    }

    static boolean isEligible(double cgpa, int codingScore) {
        return cgpa >= BORDERLINE_CGPA_CUTOFF && codingScore >= CODING_SCORE_CUTOFF;
    }

    static String shortlistAndRank(Candidate[] candidates) throws NoEligibleCandidateException {
        int shortlistedCount = 0;
        for (int candidateIndex = 0; candidateIndex < candidates.length; candidateIndex++) {
            if (isShortlisted(candidates[candidateIndex])) {
                shortlistedCount++;
            }
        }
        if (shortlistedCount == 0) {
            throw new NoEligibleCandidateException("no applicant clears either eligibility rule");
        }

        Candidate[] shortlisted = new Candidate[shortlistedCount];
        int shortlistIndex = 0;
        for (int candidateIndex = 0; candidateIndex < candidates.length; candidateIndex++) {
            if (isShortlisted(candidates[candidateIndex])) {
                shortlisted[shortlistIndex] = candidates[candidateIndex];
                shortlistIndex++;
            }
        }
        Arrays.sort(shortlisted);

        StringBuilder rankingReport = new StringBuilder();
        for (int rankIndex = 0; rankIndex < shortlisted.length; rankIndex++) {
            if (rankIndex > 0) {
                rankingReport.append(RANK_SEPARATOR);
            }
            rankingReport.append(rankIndex + 1).append(". ").append(shortlisted[rankIndex].describe());
        }
        return rankingReport.toString();
    }

    static String shortlistAndRank(Object[] walkInEntries) throws NoEligibleCandidateException {
        int candidateCount = 0;
        for (int entryIndex = 0; entryIndex < walkInEntries.length; entryIndex++) {
            if (walkInEntries[entryIndex] instanceof Candidate) {
                candidateCount++;
            }
        }
        if (candidateCount == 0) {
            throw new NoEligibleCandidateException("no entry at the desk is a candidate record");
        }

        Candidate[] candidates = new Candidate[candidateCount];
        int candidateIndex = 0;
        for (int entryIndex = 0; entryIndex < walkInEntries.length; entryIndex++) {
            if (walkInEntries[entryIndex] instanceof Candidate) {
                candidates[candidateIndex] = (Candidate) walkInEntries[entryIndex];
                candidateIndex++;
            }
        }
        System.out.println("Usable candidate records after instanceof filtering: " + candidateCount
                + " of " + walkInEntries.length);
        return shortlistAndRank(candidates);
    }

    private static boolean isShortlisted(Candidate candidate) {
        return isEligible(candidate.getCgpa())
                || isEligible(candidate.getCgpa(), candidate.getCodingScore());
    }

    private static Candidate[] parseCandidates(String batchLine) throws MalformedCandidateException {
        if (batchLine == null || batchLine.trim().isEmpty()) {
            throw new IllegalArgumentException("the batch holds no candidates");
        }
        String[] recordFields = batchLine.split(RECORD_SEPARATOR);
        Candidate[] candidates = new Candidate[recordFields.length];
        for (int recordIndex = 0; recordIndex < recordFields.length; recordIndex++) {
            candidates[recordIndex] = parseCandidate(recordFields[recordIndex]);
        }
        return candidates;
    }

    private static Candidate parseCandidate(String recordField) throws MalformedCandidateException {
        String[] candidateFields = recordField.split(FIELD_SEPARATOR);
        if (candidateFields.length != EXPECTED_FIELD_COUNT) {
            throw new MalformedCandidateException("\"" + recordField.trim()
                    + "\" needs name, CGPA and coding score");
        }
        String name = candidateFields[0].trim();
        double cgpa = Double.parseDouble(candidateFields[1].trim());
        int codingScore = Integer.parseInt(candidateFields[2].trim());
        return new Candidate(name, cgpa, codingScore);
    }
}

class Candidate implements Comparable<Candidate> {

    private static final double CGPA_WEIGHT = 10.0;
    private static final double CODING_WEIGHT = 0.5;
    private static final int NO_CODING_SCORE = 0;

    private static int candidatesCreated = 0;

    private final String name;
    private final double cgpa;
    private final int codingScore;

    public Candidate(String name, double cgpa) {
        this(name, cgpa, NO_CODING_SCORE);
    }

    public Candidate(String name, double cgpa, int codingScore) {
        candidatesCreated++;
        this.name = name;
        this.cgpa = cgpa;
        this.codingScore = codingScore;
    }

    String getName() {
        return this.name;
    }

    double getCgpa() {
        return this.cgpa;
    }

    int getCodingScore() {
        return this.codingScore;
    }

    double getCompositeScore() {
        return this.cgpa * CGPA_WEIGHT + this.codingScore * CODING_WEIGHT;
    }

    String describe() {
        return this.name + " (" + String.format("%.1f", this.getCompositeScore()) + ")";
    }

    public int compareTo(Candidate other) {
        return Double.compare(other.getCompositeScore(), this.getCompositeScore());
    }

    static int getCandidatesCreated() {
        return candidatesCreated;
    }
}

class NoEligibleCandidateException extends Exception {
    NoEligibleCandidateException(String message) {
        super(message);
    }
}

class MalformedCandidateException extends Exception {
    MalformedCandidateException(String message) {
        super(message);
    }
}

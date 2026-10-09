public class QuizScorecardDemo {

    private static final int QUESTION_COUNT = 4;
    private static final boolean[] DEMO_ANSWERS = { true, true, false, true, true };

    public static void main(String[] args) {
        Scorecard scorecard = new Scorecard(QUESTION_COUNT);
        System.out.println("Scorecard created for " + scorecard.getTotalQuestions() + " questions");

        for (int answerIndex = 0; answerIndex < DEMO_ANSWERS.length; answerIndex++) {
            boolean recorded = scorecard.recordAnswer(DEMO_ANSWERS[answerIndex]);
            if (recorded) {
                System.out.println("recordAnswer(" + DEMO_ANSWERS[answerIndex] + ") -> recorded");
            } else {
                System.out.println("recordAnswer(" + DEMO_ANSWERS[answerIndex] + ") -> rejected, scorecard is full");
            }
        }

        System.out.println("getScore() -> " + scorecard.getScore());
    }
}

class Scorecard {

    private final boolean[] results;
    private int answersRecorded;

    Scorecard(int totalQuestions) {
        this.results = new boolean[totalQuestions];
        this.answersRecorded = 0;
    }

    boolean recordAnswer(boolean correct) {
        if (this.answersRecorded >= this.results.length) {
            return false;
        }
        this.results[this.answersRecorded] = correct;
        this.answersRecorded++;
        return true;
    }

    int getScore() {
        int score = 0;
        for (int answerIndex = 0; answerIndex < this.answersRecorded; answerIndex++) {
            if (this.results[answerIndex]) {
                score++;
            }
        }
        return score;
    }

    int getTotalQuestions() {
        return this.results.length;
    }
}

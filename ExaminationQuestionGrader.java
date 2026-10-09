import java.util.Scanner;

public class ExaminationQuestionGrader {

    private static final char QUOTE = '"';
    private static final int QUOTED_FIELD_COUNT = 3;
    private static final int TEXT_INDEX = 0;
    private static final int CORRECT_ANSWER_INDEX = 1;
    private static final int STUDENT_ANSWER_INDEX = 2;
    private static final String[] DEMO_QUESTION_LINES = {
            "MCQ \"What is the capital of France?\" \"Paris\" \"Paris\" 10",
            "TF \"The Earth is flat?\" \"False\" \"True\" 5",
            "ESSAY \"Name two primary OOP principles.\" \"Inheritance, Polymorphism, Encapsulation\" "
                    + "\"Polymorphism is one.\" 20",
            "ESSAY \"Describe abstraction and composition.\" \"Abstraction, Composition\" "
                    + "\"I talked about abstraction.\" 15"
    };

    public static void main(String[] args) {
        String[] questionLines = readQuestionLines();

        Question[] questions = new Question[questionLines.length];
        for (int questionIndex = 0; questionIndex < questionLines.length; questionIndex++) {
            questions[questionIndex] = createQuestion(questionLines[questionIndex]);
        }

        double totalScore = 0.0;
        for (int questionIndex = 0; questionIndex < questions.length; questionIndex++) {
            double score = questions[questionIndex].grade();
            System.out.printf("%s: %.2f%n", questions[questionIndex].getTypeLabel(), score);
            totalScore = totalScore + score;
        }
        System.out.printf("Total Score: %.2f%n", totalScore);
    }

    private static String[] readQuestionLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_QUESTION_LINES;
        }
        int questionCount = Integer.parseInt(countLine);
        String[] questionLines = new String[questionCount];
        for (int questionIndex = 0; questionIndex < questionCount; questionIndex++) {
            questionLines[questionIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return questionLines;
    }

    private static String nextFilledLine(Scanner inputReader) {
        while (inputReader.hasNextLine()) {
            String typedLine = inputReader.nextLine().trim();
            if (!typedLine.isEmpty()) {
                return typedLine;
            }
        }
        return "";
    }

    private static Question createQuestion(String questionLine) {
        String questionType = questionLine.substring(0, questionLine.indexOf(' ')).toUpperCase();

        String[] quotedFields = new String[QUOTED_FIELD_COUNT];
        int cursor = 0;
        for (int fieldIndex = 0; fieldIndex < QUOTED_FIELD_COUNT; fieldIndex++) {
            int openingQuote = questionLine.indexOf(QUOTE, cursor);
            int closingQuote = questionLine.indexOf(QUOTE, openingQuote + 1);
            quotedFields[fieldIndex] = questionLine.substring(openingQuote + 1, closingQuote);
            cursor = closingQuote + 1;
        }
        int points = Integer.parseInt(questionLine.substring(cursor).trim());

        String questionText = quotedFields[TEXT_INDEX];
        String correctAnswer = quotedFields[CORRECT_ANSWER_INDEX];
        String studentAnswer = quotedFields[STUDENT_ANSWER_INDEX];

        switch (questionType) {
            case "MCQ":
                return new MultipleChoiceQuestion(questionText, correctAnswer, studentAnswer, points);
            case "TF":
                return new TrueFalseQuestion(questionText, correctAnswer, studentAnswer, points);
            case "ESSAY":
                return new EssayQuestion(questionText, correctAnswer, studentAnswer, points);
            default:
                throw new IllegalArgumentException("unknown question type: " + questionType);
        }
    }
}

abstract class Question {

    protected static final double NO_SCORE = 0.0;

    private final String typeLabel;
    protected final String questionText;
    protected final String correctAnswer;
    protected final String studentAnswer;
    protected final int points;

    Question(String typeLabel, String questionText, String correctAnswer, String studentAnswer, int points) {
        this.typeLabel = typeLabel;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.studentAnswer = studentAnswer;
        this.points = points;
    }

    abstract double grade();

    protected double gradeByExactMatch() {
        return this.studentAnswer.equals(this.correctAnswer) ? this.points : NO_SCORE;
    }

    String getTypeLabel() {
        return this.typeLabel;
    }
}

class MultipleChoiceQuestion extends Question {

    MultipleChoiceQuestion(String questionText, String correctAnswer, String studentAnswer, int points) {
        super("MCQ", questionText, correctAnswer, studentAnswer, points);
    }

    @Override
    double grade() {
        return gradeByExactMatch();
    }
}

class TrueFalseQuestion extends Question {

    TrueFalseQuestion(String questionText, String correctAnswer, String studentAnswer, int points) {
        super("TF", questionText, correctAnswer, studentAnswer, points);
    }

    @Override
    double grade() {
        return gradeByExactMatch();
    }
}

class EssayQuestion extends Question {

    private static final String KEYWORD_SEPARATOR = ",";
    private static final int STRONG_KEYWORD_COUNT = 2;
    private static final double STRONG_SCORE_RATE = 0.75;
    private static final double PARTIAL_SCORE_RATE = 0.50;

    EssayQuestion(String questionText, String correctAnswer, String studentAnswer, int points) {
        super("ESSAY", questionText, correctAnswer, studentAnswer, points);
    }

    @Override
    double grade() {
        int keywordsFound = countKeywordsFound();
        if (keywordsFound >= STRONG_KEYWORD_COUNT) {
            return this.points * STRONG_SCORE_RATE;
        }
        if (keywordsFound == 1) {
            return this.points * PARTIAL_SCORE_RATE;
        }
        return NO_SCORE;
    }

    private int countKeywordsFound() {
        String[] keywords = this.correctAnswer.split(KEYWORD_SEPARATOR);
        String lowerCaseAnswer = this.studentAnswer.toLowerCase();
        int keywordsFound = 0;
        for (int keywordIndex = 0; keywordIndex < keywords.length; keywordIndex++) {
            String keyword = keywords[keywordIndex].trim().toLowerCase();
            if (!keyword.isEmpty() && lowerCaseAnswer.contains(keyword)) {
                keywordsFound++;
            }
        }
        return keywordsFound;
    }
}

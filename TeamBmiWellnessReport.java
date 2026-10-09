import java.util.Arrays;
import java.util.Random;

public class TeamBmiWellnessReport {

    private static final int TEAM_SIZE = 10;
    private static final String RECORD_SEPARATOR = ",";
    private static final String[] TYPED_HEALTH_RECORDS = {
            "Aarav,1.75,70",
            "Diya,1.60,90",
            "Kabir,1.82,58",
            "Meera,1.68,seventy",
            "Rohan,0.00,64",
            "Ishita,1.71"
    };
    private static final String[] RANDOM_PERSON_NAMES = {
            "Neha", "Vikram", "Ananya", "Farhan", "Priya", "Arjun", "Sana", "Dev" };
    private static final double MIN_RANDOM_HEIGHT_METRES = 1.50;
    private static final double MAX_RANDOM_HEIGHT_METRES = 1.95;
    private static final double MIN_RANDOM_WEIGHT_KILOGRAMS = 45.0;
    private static final double MAX_RANDOM_WEIGHT_KILOGRAMS = 100.0;
    private static final double UNDERWEIGHT_LIMIT = 18.5;
    private static final double NORMAL_LIMIT = 25.0;
    private static final double OVERWEIGHT_LIMIT = 30.0;
    private static final String STATUS_UNDERWEIGHT = "Underweight";
    private static final String STATUS_NORMAL = "Normal";
    private static final String STATUS_OVERWEIGHT = "Overweight";
    private static final String STATUS_OBESE = "Obese";

    public static void main(String[] args) {
        System.out.println("=== The Corporate Wellness Program ===\n");

        String[] personNames = new String[TEAM_SIZE];
        double[] heightsInMetres = new double[TEAM_SIZE];
        double[] weightsInKilograms = new double[TEAM_SIZE];

        int loadedCount = loadTypedRecords(TYPED_HEALTH_RECORDS, personNames, heightsInMetres, weightsInKilograms);
        loadedCount = loadRandomRecords(personNames, heightsInMetres, weightsInKilograms, loadedCount);

        printWellnessReport(
                Arrays.copyOf(personNames, loadedCount),
                Arrays.copyOf(heightsInMetres, loadedCount),
                Arrays.copyOf(weightsInKilograms, loadedCount));
    }

    private static int loadTypedRecords(String[] healthRecords, String[] personNames,
                                        double[] heightsInMetres, double[] weightsInKilograms) {
        int loadedCount = 0;
        System.out.println("Reading typed health records:");
        for (int recordIndex = 0; recordIndex < healthRecords.length && loadedCount < personNames.length; recordIndex++) {
            String healthRecord = healthRecords[recordIndex];
            try {
                String[] recordFields = splitHealthRecord(healthRecord);
                String personName = recordFields[0].trim();
                double heightInMetres = Double.parseDouble(recordFields[1].trim());
                double weightInKilograms = Double.parseDouble(recordFields[2].trim());
                calculateBmi(heightInMetres, weightInKilograms);

                personNames[loadedCount] = personName;
                heightsInMetres[loadedCount] = heightInMetres;
                weightsInKilograms[loadedCount] = weightInKilograms;
                loadedCount++;
                System.out.println("\tAccepted: \"" + healthRecord + "\"");
            } catch (InvalidHealthRecordException malformedRecord) {
                System.out.println("\tSkipped : \"" + healthRecord + "\" -> " + malformedRecord.getMessage());
            } catch (NumberFormatException nonNumericField) {
                System.out.println("\tSkipped : \"" + healthRecord + "\" -> height and weight must be numbers");
            } catch (ArithmeticException impossibleHeight) {
                System.out.println("\tSkipped : \"" + healthRecord + "\" -> " + impossibleHeight.getMessage());
            }
        }
        System.out.println();
        return loadedCount;
    }

    private static String[] splitHealthRecord(String healthRecord) throws InvalidHealthRecordException {
        if (healthRecord == null || healthRecord.trim().isEmpty()) {
            throw new IllegalArgumentException("the record is empty");
        }
        String[] recordFields = healthRecord.split(RECORD_SEPARATOR);
        if (recordFields.length != 3) {
            throw new InvalidHealthRecordException("expected name,height,weight but found "
                    + recordFields.length + " field(s)");
        }
        return recordFields;
    }

    private static int loadRandomRecords(String[] personNames, double[] heightsInMetres,
                                         double[] weightsInKilograms, int startIndex) {
        Random valueGenerator = new Random();
        int loadedCount = startIndex;
        int randomNameIndex = 0;
        while (loadedCount < personNames.length && randomNameIndex < RANDOM_PERSON_NAMES.length) {
            personNames[loadedCount] = RANDOM_PERSON_NAMES[randomNameIndex];
            heightsInMetres[loadedCount] = randomBetween(valueGenerator,
                    MIN_RANDOM_HEIGHT_METRES, MAX_RANDOM_HEIGHT_METRES);
            weightsInKilograms[loadedCount] = randomBetween(valueGenerator,
                    MIN_RANDOM_WEIGHT_KILOGRAMS, MAX_RANDOM_WEIGHT_KILOGRAMS);
            loadedCount++;
            randomNameIndex++;
        }
        return loadedCount;
    }

    private static double randomBetween(Random valueGenerator, double lowestValue, double highestValue) {
        return lowestValue + (valueGenerator.nextDouble() * (highestValue - lowestValue));
    }

    private static double calculateBmi(double heightInMetres, double weightInKilograms) {
        if (heightInMetres <= 0) {
            throw new ArithmeticException("height must be greater than zero");
        }
        if (weightInKilograms <= 0) {
            throw new ArithmeticException("weight must be greater than zero");
        }
        return weightInKilograms / (heightInMetres * heightInMetres);
    }

    private static String getBmiStatus(double bmi) {
        if (bmi < UNDERWEIGHT_LIMIT) {
            return STATUS_UNDERWEIGHT;
        }
        if (bmi < NORMAL_LIMIT) {
            return STATUS_NORMAL;
        }
        if (bmi < OVERWEIGHT_LIMIT) {
            return STATUS_OVERWEIGHT;
        }
        return STATUS_OBESE;
    }

    private static void printWellnessReport(String[] personNames, double[] heightsInMetres,
                                            double[] weightsInKilograms) {
        if (personNames.length != heightsInMetres.length || heightsInMetres.length != weightsInKilograms.length) {
            throw new IllegalArgumentException("Name, height and weight arrays must be the same length");
        }

        System.out.println("Person        | Height (m) | Weight (kg) |   BMI | Status");
        System.out.println("---------------------------------------------------------------");
        for (int personIndex = 0; personIndex < personNames.length; personIndex++) {
            try {
                double bmi = calculateBmi(heightsInMetres[personIndex], weightsInKilograms[personIndex]);
                System.out.printf("%-13s | %10.2f | %11.1f | %5.2f | %s%n",
                        personNames[personIndex], heightsInMetres[personIndex],
                        weightsInKilograms[personIndex], bmi, getBmiStatus(bmi));
            } catch (ArithmeticException invalidMeasurement) {
                System.out.printf("%-13s | %10.2f | %11.1f |     - | not calculated (%s)%n",
                        personNames[personIndex], heightsInMetres[personIndex],
                        weightsInKilograms[personIndex], invalidMeasurement.getMessage());
            }
        }
        System.out.println("\nPeople reported: " + personNames.length);
    }
}

class InvalidHealthRecordException extends Exception {
    InvalidHealthRecordException(String message) {
        super(message);
    }
}

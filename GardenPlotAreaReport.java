import java.util.Scanner;

public class GardenPlotAreaReport {

    private static final String FIELD_SEPARATOR = "\\s+";
    private static final int SHAPE_INDEX = 0;
    private static final int OWNER_INDEX = 1;
    private static final int FIRST_MEASURE_INDEX = 2;
    private static final int SECOND_MEASURE_INDEX = 3;
    private static final String[] DEMO_PLOT_LINES = { "CIRCLE Asha 5", "RECTANGLE Ravi 4 6", "TRIANGLE Neha 10 3" };

    public static void main(String[] args) {
        String[] plotLines = readPlotLines();

        GardenPlot[] gardenPlots = new GardenPlot[plotLines.length];
        for (int plotIndex = 0; plotIndex < plotLines.length; plotIndex++) {
            gardenPlots[plotIndex] = createPlot(plotLines[plotIndex]);
        }

        double totalArea = 0.0;
        for (int plotIndex = 0; plotIndex < gardenPlots.length; plotIndex++) {
            double area = gardenPlots[plotIndex].calculateArea();
            System.out.printf("%s (%s): %.2f%n", gardenPlots[plotIndex].getOwner(),
                    gardenPlots[plotIndex].getShapeLabel(), area);
            totalArea = totalArea + area;
        }
        System.out.printf("Total Area: %.2f%n", totalArea);
    }

    private static String[] readPlotLines() {
        Scanner inputReader = new Scanner(System.in);
        String countLine = nextFilledLine(inputReader);
        if (countLine.isEmpty()) {
            inputReader.close();
            return DEMO_PLOT_LINES;
        }
        int plotCount = Integer.parseInt(countLine);
        String[] plotLines = new String[plotCount];
        for (int plotIndex = 0; plotIndex < plotCount; plotIndex++) {
            plotLines[plotIndex] = nextFilledLine(inputReader);
        }
        inputReader.close();
        return plotLines;
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

    private static GardenPlot createPlot(String plotLine) {
        String[] plotFields = plotLine.split(FIELD_SEPARATOR);
        String shape = plotFields[SHAPE_INDEX].toUpperCase();
        String owner = plotFields[OWNER_INDEX];
        double firstMeasure = Double.parseDouble(plotFields[FIRST_MEASURE_INDEX]);

        switch (shape) {
            case "CIRCLE":
                return new CirclePlot(owner, firstMeasure);
            case "RECTANGLE":
                return new RectanglePlot(owner, firstMeasure, Double.parseDouble(plotFields[SECOND_MEASURE_INDEX]));
            case "TRIANGLE":
                return new TrianglePlot(owner, firstMeasure, Double.parseDouble(plotFields[SECOND_MEASURE_INDEX]));
            default:
                throw new IllegalArgumentException("unknown plot shape: " + shape);
        }
    }
}

abstract class GardenPlot {

    private final String owner;
    private final String shapeLabel;

    GardenPlot(String owner, String shapeLabel) {
        this.owner = owner;
        this.shapeLabel = shapeLabel;
    }

    abstract double calculateArea();

    String getOwner() {
        return this.owner;
    }

    String getShapeLabel() {
        return this.shapeLabel;
    }
}

class CirclePlot extends GardenPlot {

    private final double radius;

    CirclePlot(String owner, double radius) {
        super(owner, "CIRCLE");
        this.radius = radius;
    }

    @Override
    double calculateArea() {
        return Math.PI * this.radius * this.radius;
    }
}

class RectanglePlot extends GardenPlot {

    private final double length;
    private final double width;

    RectanglePlot(String owner, double length, double width) {
        super(owner, "RECTANGLE");
        this.length = length;
        this.width = width;
    }

    @Override
    double calculateArea() {
        return this.length * this.width;
    }
}

class TrianglePlot extends GardenPlot {

    private static final double HALF = 0.5;

    private final double base;
    private final double height;

    TrianglePlot(String owner, double base, double height) {
        super(owner, "TRIANGLE");
        this.base = base;
        this.height = height;
    }

    @Override
    double calculateArea() {
        return HALF * this.base * this.height;
    }
}

public class TrafficLightDemo {

    private static final int DEMO_STEPS = 5;

    public static void main(String[] args) {
        TrafficLight trafficLight = new TrafficLight("TL-9");
        System.out.println("Light " + trafficLight.getId() + " getColor() -> \"" + trafficLight.getColor() + "\"");

        for (int stepIndex = 0; stepIndex < DEMO_STEPS; stepIndex++) {
            System.out.println("next() -> \"" + trafficLight.next() + "\"");
        }
    }
}

class TrafficLight {

    private static final String[] COLOR_CYCLE = { "RED", "GREEN", "YELLOW" };

    private final String id;
    private int colorIndex;

    TrafficLight(String id) {
        this.id = id;
        this.colorIndex = 0;
    }

    String next() {
        this.colorIndex = (this.colorIndex + 1) % COLOR_CYCLE.length;
        return getColor();
    }

    String getColor() {
        return COLOR_CYCLE[this.colorIndex];
    }

    String getId() {
        return this.id;
    }
}

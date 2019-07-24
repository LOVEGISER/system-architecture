package hello.java.designpattern.strategy;

public class Context {
    private TravelStrategy travelStrategy;

    public TravelStrategy getTravelStrategy() {
        return travelStrategy;
    }

    public void setTravelStrategy(TravelStrategy travelStrategy) {
        this.travelStrategy = travelStrategy;
    }
    public void travelMode() {
        this.travelStrategy.travelMode();
    }
}

package statistics;

import OSPABA.Simulation;
import simulation.Config;

public class Weighted_Arithmetic_Mean {

    private final Simulation myCore;
    private double lastTimeChanged;
    double sumValuesCount;
    double sumTotal;
    int currentCount;

    public Weighted_Arithmetic_Mean(Simulation myCore) {
        this.myCore = myCore;
        this.lastTimeChanged = Config.startTimeSTK;
//        this.lastTimeChanged = myCore.getStartTime();
    }

    public void add(int count) {
        update();
        this.currentCount += count;
    }

    public void delete(int count) {
        update();
        this.currentCount -= count;
        if (currentCount < 0) {
            currentCount = 0;
        }
    }

    private void update() {
        double time = myCore.currentTime() - lastTimeChanged;
        sumValuesCount += time*currentCount;
        sumTotal += time;
        lastTimeChanged = myCore.currentTime();
    }

    public double getWeightedMean() {
        double value = sumValuesCount/sumTotal;
        if (Double.isNaN(value)) {
            return 0;
        }
        return value;
    }

    public void reset() {
        this.lastTimeChanged = Config.startTimeSTK;
//        this.lastTimeChanged = myCore.getStartTime();
        currentCount = 0;
        sumValuesCount = 0;
        sumTotal = 0;
    }

    public void updateEnd(){
        System.out.println(myCore.currentTime() - lastTimeChanged);
        delete(0);
    }
}

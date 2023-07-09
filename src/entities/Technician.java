package entities;

import OSPABA.Entity;
import OSPABA.Simulation;
import entities.States.StateTechnician;

public class Technician extends Entity implements Actor {
    private StateTechnician state;
    private Customer currentCustomer;

    private boolean lunchBreak;
    public Technician(Simulation mySim) {
        super(mySim);
        state = StateTechnician.FREE;
        lunchBreak = false;
    }

    @Override
    public String getName() {
        return "Technik " + _id;
    }

    @Override
    public String getStateDesc() {
        String description = "";
        switch (state){
            case FREE:
                description = "Volny";
                break;
            case SERVING_PAY:
                description = "Platba";
                break;
            case TRANSPORTING_CAR:
                description = "Transportuje";
                break;
            case LUNCH_BREAK:
                description = "Pauza obed";
                break;
        }
        return description;
    }

    @Override
    public String getCarDesc() {
        return currentCustomer == null ? "" : currentCustomer.getCarDesc() + "-Zk." + currentCustomer.id();
    }

    public void setState(StateTechnician state, Customer currentCustomer) {
        this.state = state;
        this.currentCustomer = currentCustomer;
    }

    public StateTechnician getState() {
        return state;
    }

    public boolean isLunchBreak() {
        return lunchBreak;
    }

    public void setLunchBreak(boolean lunchBreak) {
        this.lunchBreak = lunchBreak;
    }
}

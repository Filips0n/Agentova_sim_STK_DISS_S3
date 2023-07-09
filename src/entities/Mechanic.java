package entities;

import OSPABA.Entity;
import OSPABA.Simulation;
import entities.States.StateMechanic;

public class Mechanic extends Entity implements Actor {

    private StateMechanic state;
    private Customer currentCustomer;

    private boolean lunchBreak;
    private LicenceType licence;
    public Mechanic(Simulation mySim) {
        super(mySim);
        state = StateMechanic.FREE;
        licence = LicenceType.TRUCK_DELIVERY_PERSONAL;
        lunchBreak = false;
    }

    @Override
    public String getName() {
        return "Mechanik " + _id;
    }

    @Override
    public String getStateDesc() {
        String description = "";
        switch (state){
            case FREE:
                description = "Volny";
                break;
            case CHECKING_CAR:
                description = "Kontrola";
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

    public void setState(StateMechanic state, Customer currentCustomer) {
        this.state = state;
        this.currentCustomer = currentCustomer;
    }

    public StateMechanic getState() {
        return state;
    }

    public void setLicence(LicenceType licence) {
        this.licence = licence;
    }

    public LicenceType getLicence() {
        return licence;
    }

    public String getLicenceDesc() {
        String description = "";
        switch (licence){
            case TRUCK_DELIVERY_PERSONAL:
                description = "2";
                break;
            case DELIVERY_PERSONAL:
                description = "1";
                break;
        }
        return description;
    }

    public boolean isLunchBreak() {
        return lunchBreak;
    }

    public void setLunchBreak(boolean lunchBreak) {
        this.lunchBreak = lunchBreak;
    }
}

package entities;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Car extends Entity {

    private CarType carType;
    private Customer owner;

    public Car(Simulation mySim, CarType carType) {
        super(mySim);
        this.carType = carType;
    }

    public Customer getOwner() {
        return owner;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setOwner(Customer customer) {
        this.owner = customer;
    }

    public String getCarName() {
        return "" + carType + "-" + "Zk." + this.owner.id();
    }
}

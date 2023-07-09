package entities;

import OSPABA.Entity;
import OSPABA.Simulation;
import entities.States.StateCustomer;

public class Customer extends Entity implements Actor, Comparable<Customer> {

    private StateCustomer state;
    private double startTimeInQueue;
    private double endTimeInQueue;
    private double endTimeInSystem;

    private double startTimeInQueuePay;
    private double endTimeInQueuePay;
    private double startTimeWaitingCheck;
    private double endTimeInWaitingCheck;
    private boolean wantsToPay;

    private Car car;

    public Customer(Simulation mySim, CarType customerCarType, StateCustomer initialState) {
        super(mySim);
        car = new Car(mySim, customerCarType);
        wantsToPay = false;
        car.setOwner(this);
        this.state = initialState;
    }

    public void setStartTimeInQueue(double startTimeInQueue) {
        this.startTimeInQueue = startTimeInQueue;
    }

    public void setEndTimeInQueue(double endTimeInQueue) {
        this.endTimeInQueue = endTimeInQueue;
    }

    public void setStartTimeInQueuePay(double startTimeInQueuePay) {
        this.startTimeInQueuePay = startTimeInQueuePay;
    }

    public void setEndTimeInQueuePay(double endTimeInQueuePay) {
        this.endTimeInQueuePay = endTimeInQueuePay;
    }

    public void setStartTimeWaitingCheck(double startTimeWaitingCheck) {
        this.startTimeWaitingCheck = startTimeWaitingCheck;
    }

    public void setEndTimeInWaitingCheck(double endTimeInWaitingCheck) {
        this.endTimeInWaitingCheck = endTimeInWaitingCheck;
    }

    public double getStartTimeInQueuePay() {
        return startTimeInQueuePay;
    }

    public double getStartTimeWaitingCheck() {
        return startTimeWaitingCheck;
    }
    public double getWaitingTimeInWaitingCheck() {
        return endTimeInWaitingCheck - startTimeWaitingCheck;
    }
    public double getWaitingTimeInQueuePay() {
        return endTimeInQueuePay - startTimeInQueuePay;
    }
    public double getWaitingTimeInQueue() {
        return endTimeInQueue - startTimeInQueue;
    }

    public double getStartTimeInQueue() {
        return startTimeInQueue;
    }

    public boolean wantToPay() {
        return wantsToPay;
    }

    public void setWantsToPay(boolean wantsToPay) {
        this.wantsToPay = wantsToPay;
    }

    public Car getCar() {
        return car;
    }

    @Override
    public int compareTo(Customer customer) {
        if(this.wantsToPay == customer.wantToPay())
            if (this.wantsToPay) {
                return Double.compare(this.startTimeInQueuePay, customer.getStartTimeInQueuePay());
            } else {
                return Double.compare(this.startTimeInQueue, customer.getStartTimeInQueue());
            }
        else if(this.wantsToPay && !customer.wantToPay())
            return -1;
        else
            return 1;
    }

    public String getStateDesc(){
        String description = "";
        switch (state){
            case LEFT:
                description = "Odisiel";
                break;
            case QUEUE_CAR:
                description = "V rade na odovz. auta";
                break;
            case QUEUE_PAY:
                description = "V rade na zaplatenie";
                break;
            case PAYING:
                description = "Plati";
                break;
            case WAITING_CAR:
                description = "Caka na koniec kontroly";
                break;
        }
        return description;
    }

    @Override
    public String getCarDesc() {
        return "" + car.getCarType();
    }

    public String getName() {
        return "Zakaznik " + _id;
    }

    public void setState(StateCustomer state) {
        this.state = state;
    }

    public void setEndTimeInSystem(double currentTime) {
        this.endTimeInSystem = currentTime;
    }

    public double getWaitingTimeInSystem() {
        return endTimeInSystem-startTimeInQueue;
    }

    public StateCustomer getState() {
        return state;
    }
}

package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import entities.Car;
import entities.CarType;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.ArrayList;
import java.util.Arrays;

//meta! id="92"
public class Agent_Parking extends Agent
{
	private SimQueue<Car> workShopParking;
	private int freeParkingSlotsQuantity;
	public Agent_Parking(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		this.workShopParking = new SimQueue<>(new WStat(mySim()));
		freeParkingSlotsQuantity = Config.maxParkingCapacity;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new Manager_Parking(Id.manager_Parking, mySim(), this);
		addOwnMessage(Mc.parkedCar);
		addOwnMessage(Mc.allocateParkingSlot);
		addOwnMessage(Mc.getParkedCar);
	}
	//meta! tag="end"


	public SimQueue<Car> getWorkShopParking() {
		return workShopParking;
	}

	public int getFreeParkingSlotsQuantity() {
		return freeParkingSlotsQuantity;
	}

	public void changeFreeParkingSlotsQuantity(int value){
		freeParkingSlotsQuantity += value;
	}

	public ArrayList<ArrayList<String>> getAllCarsWaiting() {
		ArrayList<ArrayList<String>> stringList = new ArrayList<>();

		int i = 1;
		for (Car car: workShopParking) {
			stringList.add(new ArrayList<>(Arrays.asList("" + i + ".", car.getCarName())));
			i++;
		}

		return stringList;
	}

	public Car getDeliveryPersonalCar() {
		for (int i = 0; i < workShopParking.size(); i++) {
			Car car = workShopParking.get(i);
			if (car.getCarType() == CarType.DELIVERY || car.getCarType() == CarType.PERSONAL) {
				workShopParking.remove(car);
				return car;
			}
		}

//		for (Car car : workShopParking) {
//			if (car.getCarType() == CarType.DELIVERY || car.getCarType() == CarType.PERSONAL) {
//				workShopParking.remove(car);
//				return car;
//			}
//		}
		return null;
	}
}
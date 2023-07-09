package agents;

import OSPABA.*;
import entities.Actor;
import simulation.*;
import managers.*;

import java.util.ArrayList;

//meta! id="3"
public class Agent_STK extends Agent
{
//	private ArrayList<Actor> allCustomers;
	public Agent_STK(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new Manager_STK(Id.manager_STK, mySim(), this);
		addOwnMessage(Mc.allocateTechnician);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.tryAssignJobMechanic);
		addOwnMessage(Mc.carArrivalParking);
		addOwnMessage(Mc.allocateParkingSlot);
		addOwnMessage(Mc.freeMechanic);
		addOwnMessage(Mc.isTechnicianAvailable);
		addOwnMessage(Mc.serveCustomer);
		addOwnMessage(Mc.parkedCar);
		addOwnMessage(Mc.endCarCheck);
		addOwnMessage(Mc.freeTechnician);
		addOwnMessage(Mc.tryAssignJobTechnician);
		addOwnMessage(Mc.isMechanicAvailable);
		addOwnMessage(Mc.allocateMechanic);
		addOwnMessage(Mc.getParkedCar);
	}
	//meta! tag="end"
}
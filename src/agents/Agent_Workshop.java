package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="6"
public class Agent_Workshop extends Agent
{
	private MyMessage arrivalMessage;

	private int carsChecking;
	public Agent_Workshop(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		arrivalMessage = null;
		this.carsChecking = 0;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new Manager_Workshop(Id.manager_Workshop, mySim(), this);
		new Process_Check_Car(Id.process_Check_Car, mySim(), this);
		addOwnMessage(Mc.performCheck);
		addOwnMessage(Mc.endCheck);
		addOwnMessage(Mc.tryAssignJobMechanic);
		addOwnMessage(Mc.startCheck);
		addOwnMessage(Mc.isMechanicAvailable);
		addOwnMessage(Mc.allocateMechanic);
		addOwnMessage(Mc.getParkedCar);
	}
	//meta! tag="end"

	public void setArrivalMessage(MyMessage message) {
		if (arrivalMessage == null) {
			arrivalMessage = message;
			arrivalMessage.setTechnician(null);
			arrivalMessage.setAllocatedParkingSlot(false);
		}
	}

	public MyMessage getArrivalMessage() {
		return this.arrivalMessage;
	}

	public int getCarsChecking() {
		return carsChecking;
	}

	public void changeCarsChecking(int value) {
		carsChecking += value;
	}
}
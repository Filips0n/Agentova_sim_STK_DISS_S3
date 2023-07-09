package managers;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import entities.Car;
import entities.LicenceType;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.Calendar;

//meta! id="92"
public class Manager_Parking extends Manager
{
	public Manager_Parking(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="Agent_STK", id="135", type="Notice"
	public void processParkedCar(MessageForm message)
	{
		MyMessage myMessage = (MyMessage)message;
		myAgent().getWorkShopParking().enqueue(myMessage.getCustomer().getCar());

		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.carArrivalParking);
		notice(message);
	}

	//meta! sender="Agent_STK", id="132", type="Request"
	public void processAllocateParkingSlot(MessageForm message)
	{
		MyMessage myMessage = (MyMessage)message;
		myMessage.setAllocatedParkingSlot(false);
		if (myAgent().getFreeParkingSlotsQuantity() > 0) {
			myAgent().changeFreeParkingSlotsQuantity(-1);
			myMessage.setAllocatedParkingSlot(true);
		}
		response(myMessage);
	}

	//meta! sender="Agent_STK", id="133", type="Request"
	public void processGetParkedCar(MessageForm message)
	{
		MyMessage myMessage = (MyMessage)message;
		myMessage.setParkedCar(null);
		if (myAgent().getWorkShopParking().size() > 0) {
			if (Config.modificationsEnabled) {
				if (myMessage.getMechanicLicence() == LicenceType.DELIVERY_PERSONAL) {
					myMessage.setParkedCar(myAgent().getDeliveryPersonalCar());
					if (myMessage.getParkedCar() != null) {
						myMessage.setCustomer(myMessage.getParkedCar().getOwner());
						myAgent().changeFreeParkingSlotsQuantity(1);
					}
				} else {
					myMessage.setParkedCar(myAgent().getWorkShopParking().dequeue());
					myMessage.setCustomer(myMessage.getParkedCar().getOwner());
					myAgent().changeFreeParkingSlotsQuantity(1);
				}
			} else {
				myMessage.setParkedCar(myAgent().getWorkShopParking().dequeue());
				myMessage.setCustomer(myMessage.getParkedCar().getOwner());
				myAgent().changeFreeParkingSlotsQuantity(1);
			}
		}
		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.allocateParkingSlot:
			processAllocateParkingSlot(message);
		break;

		case Mc.getParkedCar:
			processGetParkedCar(message);
		break;

		case Mc.parkedCar:
			processParkedCar(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public Agent_Parking myAgent()
	{
		return (Agent_Parking)super.myAgent();
	}

}
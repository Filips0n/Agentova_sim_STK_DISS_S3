package managers;

import OSPABA.*;
import entities.Customer;
import entities.States.StateCustomer;
import entities.States.StateMechanic;
import entities.States.StateTechnician;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="6"
public class Manager_Workshop extends Manager
{
	public Manager_Workshop(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="Agent_STK", id="68", type="Notice"
	public void processPerformCheck(MessageForm message)
	{
		myAgent().setArrivalMessage((MyMessage) message.createCopy());
		isMechanicAvailable((MyMessage)message);
	}

	//meta! sender="Process_Check_Car", id="26", type="Notice"
	public void processEndCheck(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message.createCopy();

		((MyMessage)message).setMechanic(null);
		myAgent().changeCarsChecking(-1);
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.endCarCheck);
		notice(message);

		setNewCarToMechanic((MyMessage) myMessage);
	}

	//meta! sender="Agent_STK", id="38", type="Response"
	public void processAllocateMechanic(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getMechanic() != null) {
			startCheck(myMessage);
		} else {
			throw new RuntimeException();
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}
	//meta! sender="Agent_STK", id="99", type="Response"
	public void processGetParkedCar(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getParkedCar() != null) {
			if (myMessage.getMechanic() == null) {
				allocateMechanic(myMessage);
			} else {
				startCheck(myMessage);
			}
		} else {
			freeMechanic(myMessage);
		}
	}
	private void freeMechanic(MyMessage message) {
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.freeMechanic);
		notice(message);
	}

	//meta! sender="Agent_STK", id="139", type="Response"
	public void processIsMechanicAvailable(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.msgResult() == 1) {
			myMessage.setMsgResult(0);
			getParkedCar(myMessage);
		}
	}

	//meta! sender="Agent_STK", id="185", type="Notice"
	public void processTryAssignJobMechanic(MessageForm message)
	{
		MyMessage message1 = (MyMessage) myAgent().getArrivalMessage().createCopy();
		message1.setMechanic(((MyMessage) message).getMechanic());
		setNewCarToMechanic(message1);
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
		case Mc.getParkedCar:
			processGetParkedCar(message);
		break;

		case Mc.isMechanicAvailable:
			processIsMechanicAvailable(message);
		break;

		case Mc.allocateMechanic:
			processAllocateMechanic(message);
		break;

		case Mc.tryAssignJobMechanic:
			processTryAssignJobMechanic(message);
		break;

		case Mc.performCheck:
			processPerformCheck(message);
		break;

		case Mc.endCheck:
			processEndCheck(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public Agent_Workshop myAgent()
	{
		return (Agent_Workshop)super.myAgent();
	}
	private void allocateMechanic(MyMessage message) {
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.allocateMechanic);
		request(message);
	}

	private void startCheck(MyMessage message) {
		myAgent().changeCarsChecking(1);
		message.getMechanic().setState(StateMechanic.CHECKING_CAR, message.getParkedCar().getOwner());
		message.setCode(Mc.startCheck);
		message.setAddressee(myAgent().findAssistant(Id.process_Check_Car));
		notice(message);
	}
	private void setNewCarToMechanic(MyMessage message) {
		if (!message.getMechanic().isLunchBreak()) {
			message.setMechanicLicence(message.getMechanic().getLicence());
			getParkedCar(message);
		} else {
			freeMechanic(message);
		}
	}
	private void getParkedCar(MessageForm message) {
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.getParkedCar);
		request(message);
	}

	private void isMechanicAvailable(MyMessage myMessage) {
		myMessage.setAddressee(mySim().findAgent(Id.agent_STK));
		myMessage.setCode(Mc.isMechanicAvailable);
		request(myMessage);
	}
}
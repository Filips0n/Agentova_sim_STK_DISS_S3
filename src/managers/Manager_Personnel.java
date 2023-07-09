package managers;

import OSPABA.*;
import entities.*;
import entities.States.StateMechanic;
import entities.States.StateTechnician;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;
import statistics.Arithmetic_Mean;
import statistics.Weighted_Arithmetic_Mean;

import java.util.ArrayList;

//meta! id="5"
public class Manager_Personnel extends Manager
{

	public Manager_Personnel(int id, Simulation mySim, Agent myAgent)
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

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}
	//meta! sender="Agent_STK", id="130", type="Notice"
	public void processFreeTechnician(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getTechnician().isLunchBreak()) {myMessage.setMechanic(null);startLunchBreak(myMessage);return;}
		myAgent().availableTechniciansWAM.add(1);
		myAgent().getAvailableTechnicians().enqueue(myMessage.getTechnician());

		myMessage.getTechnician().setState(StateTechnician.FREE, null);
		myMessage.setTechnician(null);
		myMessage.setCustomer(null);
		myMessage.setMechanic(null);
		myMessage.setParkedCar(null);

	}

	//meta! sender="Agent_STK", id="131", type="Notice"
	public void processFreeMechanic(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getMechanic().isLunchBreak()) {myMessage.setTechnician(null);startLunchBreak(myMessage);return;}
		myAgent().availableMechanicsWAM.add(1);
		myAgent().getAvailableMechanics().enqueue(myMessage.getMechanic());

		myMessage.getMechanic().setState(StateMechanic.FREE, null);
		myMessage.setTechnician(null);
		myMessage.setCustomer(null);
		myMessage.setMechanic(null);
		myMessage.setParkedCar(null);
	}

	private void startLunchBreak(MyMessage message) {
		if (message.getMechanic() != null) {
			message.getMechanic().setState(StateMechanic.LUNCH_BREAK, null);
		} else {
			message.getTechnician().setState(StateTechnician.LUNCH_BREAK, null);
		}
		message.setCode(Mc.startLunchBreak);
		message.setAddressee(myAgent().findAssistant(Id.process_Lunch_Break));
		notice(message);
	}

	//meta! sender="Agent_STK", id="127", type="Request"
	public void processAllocateTechnician(MessageForm message)
	{
		myAgent().availableTechniciansWAM.delete(1);
		if (myAgent().getAvailableTechnicians().size() > 0) {myAgent().getAvailableTechnicians().dequeue();}
		MyMessage myMessage = (MyMessage) message;
		myMessage.setTechnician(myAgent().getTechnician());
		if (myMessage.getTechnician() != null) {
			if (myMessage.getCustomer().wantToPay()) {
				myMessage.getTechnician().setState(StateTechnician.SERVING_PAY, myMessage.getCustomer());
			} else {
				myMessage.getTechnician().setState(StateTechnician.TRANSPORTING_CAR, myMessage.getCustomer());
			}
		}

//		if (myMessage.getTechnician() == null) {
//			System.out.println("1");
//		}
		response(message);
	}

	//meta! sender="Agent_STK", id="129", type="Request"
	public void processAllocateMechanic(MessageForm message)
	{
		myAgent().availableMechanicsWAM.delete(1);
		if (myAgent().getAvailableMechanics().size() > 0) {myAgent().getAvailableMechanics().dequeue();}

		MyMessage myMessage = (MyMessage) message;
		if (Config.modificationsEnabled) {
			((Adviser_Choose_Mechanic)myAgent().findAssistant(Id.adviser_Choose_Mechanic)).execute(myMessage);
		} else {
			myMessage.setMechanic(myAgent().getMechanic());
		}

		if (myMessage.getMechanic() != null) {
			myMessage.getMechanic().setState(StateMechanic.CHECKING_CAR, myMessage.getCustomer());
		}

		message.setCode(Mc.allocateMechanic);
		response(message);
	}

	//meta! sender="Agent_STK", id="138", type="Request"
	public void processIsTechnicianAvailable(MessageForm message)
	{
		if (((MyMessage)message).getTwoAvailableTechnicians()) {areTwoTechniciansAvailable(message);return;}
		message.setMsgResult(0);
		if (myAgent().isTechnicianAvailable()) {
			message.setMsgResult(1);
		}
		response(message);
	}

	private void areTwoTechniciansAvailable(MessageForm message) {
		message.setMsgResult(0);
//		((MyMessage)message).setTwoAvailableTechnicians(false);
		if (myAgent().areTwoTechnicianAvailable()) {
			message.setMsgResult(1);
		}
		response(message);
	}

	//meta! sender="Agent_STK", id="140", type="Request"
	public void processIsMechanicAvailable(MessageForm message)
	{
		message.setMsgResult(0);
		if (Config.modificationsEnabled) {
			MyMessage myMessage = (MyMessage) message;
			if (myMessage.getCustomer().getCar().getCarType() == CarType.TRUCK) {
				if (myAgent().isMechanicType2Available()) {
					message.setMsgResult(1);
					myMessage.setMechanicLicence(LicenceType.TRUCK_DELIVERY_PERSONAL);
				}
			} else {
				if (myAgent().isMechanic1Available()) {
					message.setMsgResult(1);
					myMessage.setMechanicLicence(LicenceType.DELIVERY_PERSONAL);
				}
				if (message.msgResult() == 0 && myAgent().isMechanicType2Available()) {
					message.setMsgResult(1);
					myMessage.setMechanicLicence(LicenceType.TRUCK_DELIVERY_PERSONAL);
				}
			}
		} else {
			if (myAgent().isMechanicAvailable()) {
				message.setMsgResult(1);
			}
		}
		response(message);
	}

	//meta! sender="Agent_STK", id="168", type="Notice"
	public void processInit(MessageForm message)
	{
		if (Config.modificationsEnabled) {
			message.setCode(Mc.holdToLunchBreak);
			message.setAddressee(myAgent().findAssistant(Id.process_Lunch_Break));
			notice(message);
		}
	}

	//meta! sender="Process_Lunch_Break", id="174", type="Notice"
	public void processEndLunchBreak(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getMechanic() != null) {
			myMessage.getMechanic().setLunchBreak(false);
			message.setCode(Mc.tryAssignJobMechanic);
		} else {
			myMessage.getTechnician().setLunchBreak(false);
			message.setCode(Mc.tryAssignJobTechnician);
		}
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		notice(message);
	}

	//meta! sender="Process_Lunch_Break", id="207", type="Notice"
	public void processLunchBreak(MessageForm message)
	{
		//11:00 start of lunch break
		MyMessage myMessage = (MyMessage) message;
		for (Actor mechanic: myAgent().allMechanics()) {
			Mechanic mechanic1 = ((Mechanic)mechanic);
			mechanic1.setLunchBreak(true);
			if (mechanic1.getState() == StateMechanic.FREE) {
				MyMessage copyMessage = (MyMessage) myMessage.createCopy();
				copyMessage.setTechnician(null);
				copyMessage.setMechanic(mechanic1);

				myAgent().availableMechanicsWAM.delete(1);
				if (myAgent().getAvailableMechanics().size() > 0) {myAgent().getAvailableMechanics().dequeue();}

				copyMessage.getMechanic().setState(StateMechanic.LUNCH_BREAK, null);

				startLunchBreak(copyMessage);
			}
		}

		for (Actor technician: myAgent().allTechnicians()) {
			Technician technician1 = ((Technician)technician);
			technician1.setLunchBreak(true);
			if (technician1.getState() == StateTechnician.FREE) {
				MyMessage copyMessage = (MyMessage) myMessage.createCopy();
				copyMessage.setMechanic(null);
				copyMessage.setTechnician(technician1);

				myAgent().availableTechniciansWAM.delete(1);
				if (myAgent().getAvailableTechnicians().size() > 0) {myAgent().getAvailableTechnicians().dequeue();}

				copyMessage.getTechnician().setState(StateTechnician.LUNCH_BREAK, null);

				startLunchBreak(copyMessage);
			}
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
		case Mc.isTechnicianAvailable:
			processIsTechnicianAvailable(message);
		break;

		case Mc.freeTechnician:
			processFreeTechnician(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.allocateTechnician:
			processAllocateTechnician(message);
		break;

		case Mc.allocateMechanic:
			processAllocateMechanic(message);
		break;

		case Mc.isMechanicAvailable:
			processIsMechanicAvailable(message);
		break;

		case Mc.freeMechanic:
			processFreeMechanic(message);
		break;

		case Mc.lunchBreak:
			processLunchBreak(message);
		break;

		case Mc.endLunchBreak:
			processEndLunchBreak(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public Agent_Personnel myAgent()
	{
		return (Agent_Personnel)super.myAgent();
	}

}
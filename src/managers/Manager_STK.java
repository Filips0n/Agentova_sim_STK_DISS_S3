package managers;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="3"
public class Manager_STK extends Manager
{
	public Manager_STK(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="Agent_Parking", id="40", type="Notice"
	public void processCarArrivalParking(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Workshop));
		message.setCode(Mc.performCheck);
		notice(message);
	}

	//meta! sender="Agent_Model", id="31", type="Request"
	public void processServeCustomerAgent_Model(MessageForm message)
	{
		message.setCode(Mc.serveCustomer);
		message.setAddressee(mySim().findAgent(Id.agent_Reception));

		request(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="Agent_Workshop", id="116", type="Notice"
	public void processEndCarCheck(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Reception));
		message.setCode(Mc.paymentCustomer);
		notice(message);
	}

	//meta! sender="Agent_Reception", id="37", type="Request"
	public void processAllocateTechnicianAgent_Reception(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Personnel));
		request(message);
	}

	//meta! sender="Agent_Parking", id="132", type="Response"
	public void processAllocateParkingSlotAgent_Parking(MessageForm message)
	{
		response(message);
	}

	//meta! sender="Agent_Reception", id="93", type="Request"
	public void processAllocateParkingSlotAgent_Reception(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Parking));
		request(message);
	}

	//meta! sender="Agent_Workshop", id="91", type="Notice"
	public void processFreeMechanic(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Personnel));
		notice(message);
	}

	//meta! sender="Agent_Personnel", id="127", type="Response"
	public void processAllocateTechnicianAgent_Personnel(MessageForm message)
	{
		response(message);
	}

	//meta! sender="Agent_Reception", id="100", type="Notice"
	public void processParkedCar(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Parking));
		notice(message);
	}

	//meta! sender="Agent_Reception", id="90", type="Notice"
	public void processFreeTechnician(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Personnel));
		notice(message);
	}

	//meta! sender="Agent_Personnel", id="129", type="Response"
	public void processAllocateMechanicAgent_Personnel(MessageForm message)
	{
		response(message);
	}

	//meta! sender="Agent_Workshop", id="38", type="Request"
	public void processAllocateMechanicAgent_Workshop(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Personnel));
		request(message);
	}

	//meta! sender="Agent_Workshop", id="99", type="Request"
	public void processGetParkedCarAgent_Workshop(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Parking));
		request(message);
	}

	//meta! sender="Agent_Parking", id="133", type="Response"
	public void processGetParkedCarAgent_Parking(MessageForm message)
	{
		response(message);

		MyMessage noticeMessage = (MyMessage) message.createCopy();
		if (noticeMessage.getParkedCar() != null) {
			noticeMessage.setParkedCar(null);
			noticeMessage.setAddressee(mySim().findAgent(Id.agent_Reception));
			noticeMessage.setCode(Mc.availableParking);
			notice(noticeMessage);
		}
	}

	//meta! sender="Agent_Reception", id="137", type="Request"
	public void processIsTechnicianAvailableAgent_Reception(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Personnel));
		request(message);
	}

	//meta! sender="Agent_Personnel", id="138", type="Response"
	public void processIsTechnicianAvailableAgent_Personnel(MessageForm message)
	{
		response(message);
	}

	//meta! sender="Agent_Reception", id="152", type="Response"
	public void processServeCustomerAgent_Reception(MessageForm message)
	{
		message.setCode(Mc.serveCustomer);//Mc.processCustomer
		response(message);
	}

	//meta! sender="Agent_Personnel", id="140", type="Response"
	public void processIsMechanicAvailableAgent_Personnel(MessageForm message)
	{
		response(message);
	}

	//meta! sender="Agent_Workshop", id="139", type="Request"
	public void processIsMechanicAvailableAgent_Workshop(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Personnel));
		request(message);
	}

	//meta! sender="Agent_Model", id="167", type="Notice"
	public void processInit(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Personnel));
		notice(message);
	}

	//meta! sender="Agent_Personnel", id="184", type="Notice"
	public void processTryAssignJobMechanic(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Workshop));
		message.setCode(Mc.tryAssignJobMechanic);
		notice(message);
	}

	//meta! sender="Agent_Personnel", id="187", type="Notice"
	public void processTryAssignJobTechnician(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_Reception));
		message.setCode(Mc.tryAssignJobTechnician);
		notice(message);
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
			switch (message.sender().id())
			{
			case Id.agent_Personnel:
				processIsTechnicianAvailableAgent_Personnel(message);
			break;

			case Id.agent_Reception:
				processIsTechnicianAvailableAgent_Reception(message);
			break;
			}
		break;

		case Mc.tryAssignJobTechnician:
			processTryAssignJobTechnician(message);
		break;

		case Mc.isMechanicAvailable:
			switch (message.sender().id())
			{
			case Id.agent_Workshop:
				processIsMechanicAvailableAgent_Workshop(message);
			break;

			case Id.agent_Personnel:
				processIsMechanicAvailableAgent_Personnel(message);
			break;
			}
		break;

		case Mc.allocateMechanic:
			switch (message.sender().id())
			{
			case Id.agent_Workshop:
				processAllocateMechanicAgent_Workshop(message);
			break;

			case Id.agent_Personnel:
				processAllocateMechanicAgent_Personnel(message);
			break;
			}
		break;

		case Mc.allocateTechnician:
			switch (message.sender().id())
			{
			case Id.agent_Personnel:
				processAllocateTechnicianAgent_Personnel(message);
			break;

			case Id.agent_Reception:
				processAllocateTechnicianAgent_Reception(message);
			break;
			}
		break;

		case Mc.parkedCar:
			processParkedCar(message);
		break;

		case Mc.allocateParkingSlot:
			switch (message.sender().id())
			{
			case Id.agent_Reception:
				processAllocateParkingSlotAgent_Reception(message);
			break;

			case Id.agent_Parking:
				processAllocateParkingSlotAgent_Parking(message);
			break;
			}
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.getParkedCar:
			switch (message.sender().id())
			{
			case Id.agent_Workshop:
				processGetParkedCarAgent_Workshop(message);
			break;

			case Id.agent_Parking:
				processGetParkedCarAgent_Parking(message);
			break;
			}
		break;

		case Mc.freeMechanic:
			processFreeMechanic(message);
		break;

		case Mc.endCarCheck:
			processEndCarCheck(message);
		break;

		case Mc.serveCustomer:
			switch (message.sender().id())
			{
			case Id.agent_Model:
				processServeCustomerAgent_Model(message);
			break;

			case Id.agent_Reception:
				processServeCustomerAgent_Reception(message);
			break;
			}
		break;

		case Mc.carArrivalParking:
			processCarArrivalParking(message);
		break;

		case Mc.freeTechnician:
			processFreeTechnician(message);
		break;

		case Mc.tryAssignJobMechanic:
			processTryAssignJobMechanic(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public Agent_STK myAgent()
	{
		return (Agent_STK)super.myAgent();
	}

}
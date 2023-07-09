package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="1"
public class Manager_Model extends Manager
{
	public Manager_Model(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="Agent_Surroundings", id="29", type="Notice"
	public void processArrivalCustomer(MessageForm message)
	{
		message.setCode(Mc.serveCustomer);//Mc.processCustomer
		message.setAddressee(mySim().findAgent(Id.agent_STK));

		request(message);
	}

	//meta! sender="Agent_STK", id="31", type="Response"
	public void processServeCustomer(MessageForm message)
	{
		message.setCode(Mc.leftCustomer);
		message.setAddressee(mySim().findAgent(Id.agent_Surroundings));

		notice(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.init:
//				message.setAddressee(mySim().findAgent(Id.agent_Surroundings));
//				notice(message);
//				message.setAddressee(mySim().findAgent(Id.agent_STK));
//				notice(message);
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
		case Mc.serveCustomer:
			processServeCustomer(message);
		break;

		case Mc.arrivalCustomer:
			processArrivalCustomer(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public Agent_Model myAgent()
	{
		return (Agent_Model)super.myAgent();
	}

}
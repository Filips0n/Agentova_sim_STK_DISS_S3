package managers;

import OSPABA.*;
import entities.Customer;
import entities.States.StateCustomer;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="2"
public class Manager_Surroundings extends Manager
{
	public Manager_Surroundings(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="Agent_Model", id="34", type="Notice"
	public void processInit(MessageForm message)
	{
		message.setCode(Mc.init);
		message.setAddressee(myAgent().findAssistant(Id.scheduler_New_Customer));
		notice(message);
	}

	//meta! sender="Scheduler_New_Customer", id="46", type="Notice"
	public void processNewCustomer(MessageForm message)
	{
		myAgent().incCustomersIn();
		myAgent().addCustomerToAllCustomers(((MyMessage)message).getCustomer());
		myAgent().getAvgCustomerCountSystem().add(1);

		message.setCode(Mc.arrivalCustomer);
		message.setAddressee(_mySim.findAgent(Id.agent_Model));
		notice(message);
	}

	//meta! sender="Agent_Model", id="30", type="Notice"
	public void processLeftCustomer(MessageForm message)
	{
		MyMessage myMessage = (MyMessage)message;
		myAgent().incCustomersOut();
		myAgent().getAvgCustomerCountSystem().delete(1);
		//Prev code

		Customer customer = myMessage.getCustomer();

		customer.setState(StateCustomer.LEFT);
		//end timer
		customer.setEndTimeInSystem(mySim().currentTime());
		myAgent().getAvgCustomerTimeInSystem().addSample(customer.getWaitingTimeInSystem());
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
		case Mc.leftCustomer:
			processLeftCustomer(message);
		break;

		case Mc.newCustomer:
			processNewCustomer(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public Agent_Surroundings myAgent()
	{
		return (Agent_Surroundings)super.myAgent();
	}

}
package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="1"
public class Agent_Model extends Agent
{
	public Agent_Model(int id, Simulation mySim, Agent parent)
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
		new Manager_Model(Id.manager_Model, mySim(), this);
		addOwnMessage(Mc.serveCustomer);
		addOwnMessage(Mc.arrivalCustomer);
	}
	//meta! tag="end"

	public void startSimulation()
	{
		MyMessage message = new MyMessage(mySim());
		message.setCode(Mc.init);
		message.setAddressee(this);
		manager().notice(message);

		MyMessage message2 = (MyMessage) message.createCopy();
		message2.setAddressee(mySim().findAgent(Id.agent_STK));
		manager().notice(message2);
	}
}
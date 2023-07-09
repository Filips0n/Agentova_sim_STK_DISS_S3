package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import OSPRNG.TriangularRNG;
import entities.Customer;
import entities.States.StateCustomer;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="22"
public class Process_Transfer_Car extends Process
{
	private static TriangularRNG tria = new TriangularRNG(180.0, 431.0,695.0);
	public Process_Transfer_Car(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="Agent_Reception", id="25", type="Notice"
	public void processStartTransfer(MessageForm message)
	{
		message.setCode(Mc.endTransfer);
		hold(tria.sample(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.endTransfer:
				message.setAddressee(myAgent());
				notice(message);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.startTransfer:
			processStartTransfer(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public Agent_Reception myAgent()
	{
		return (Agent_Reception)super.myAgent();
	}

}
package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="61"
public class Process_Payment_Customer extends Process
{
	private static UniformContinuousRNG uni = new UniformContinuousRNG(65.0, 177.0);
	public Process_Payment_Customer(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="Agent_Reception", id="63", type="Notice"
	public void processStartPayment(MessageForm message)
	{
		message.setCode(Mc.endPayment);
		hold(uni.sample(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.endPayment:
				notice(message);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.startPayment:
			processStartPayment(message);
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
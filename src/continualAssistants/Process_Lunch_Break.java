package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="148"
public class Process_Lunch_Break extends Process
{
	private UniformContinuousRNG del = new UniformContinuousRNG(0.0,0.01);
	public Process_Lunch_Break(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.lunchBreak:
				message.setCode(Mc.lunchBreak);
				message.setAddressee(myAgent());
				notice(message);
			break;
			case Mc.endLunchBreak:
				message.setAddressee(myAgent());
				notice(message);
			break;
		}
	}

	//meta! sender="Agent_Personnel", id="173", type="Notice"
	public void processStartLunchBreak(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		myMessage.setCode(Mc.endLunchBreak);
		hold(Config.lunchBreakTime+del.sample(), myMessage);
	}

	//meta! sender="Agent_Personnel", id="206", type="Notice"
	public void processHoldToLunchBreak(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		myMessage.setCode(Mc.lunchBreak);
		hold(Config.lunchBreakStartTime, myMessage);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.holdToLunchBreak:
			processHoldToLunchBreak(message);
		break;

		case Mc.startLunchBreak:
			processStartLunchBreak(message);
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
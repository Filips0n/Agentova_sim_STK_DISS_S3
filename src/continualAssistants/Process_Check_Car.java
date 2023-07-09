package continualAssistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.UniformDiscreteRNG;
import entities.CarType;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="18"
public class Process_Check_Car extends Process
{
	private EmpiricRNG _empiricTruck = new EmpiricRNG(
		new EmpiricPair(new UniformDiscreteRNG(2220, 2520), 0.05),
		new EmpiricPair(new UniformDiscreteRNG(2580, 2700), 0.1),
		new EmpiricPair(new UniformDiscreteRNG(2760, 2820), 0.15),
		new EmpiricPair(new UniformDiscreteRNG(2880, 3060), 0.4),
		new EmpiricPair(new UniformDiscreteRNG(3120, 3300), 0.25),
		new EmpiricPair(new UniformDiscreteRNG(3360, 3900), 0.05)
	);

	private EmpiricRNG _empiricDelivery = new EmpiricRNG(
		new EmpiricPair(new UniformDiscreteRNG(2100, 2220), 0.2),
		new EmpiricPair(new UniformDiscreteRNG(2280, 2400), 0.35),
		new EmpiricPair(new UniformDiscreteRNG(2460, 2820), 0.3),
		new EmpiricPair(new UniformDiscreteRNG(2880, 3120), 0.15)
	);

	private UniformDiscreteRNG _discCar = new UniformDiscreteRNG(1860,2700);

	public Process_Check_Car(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="Agent_Workshop", id="27", type="Notice"
	public void processStartCheck(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		myMessage.setCode(Mc.endCheck);
		hold(getSampleByCarType(myMessage.getParkedCar().getCarType()), myMessage);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.endCheck:
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
		case Mc.startCheck:
			processStartCheck(message);
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

	public int getSampleByCarType(CarType carType) {
		if (carType == CarType.PERSONAL) {
			return _discCar.sample();
		} else if (carType == CarType.DELIVERY) {
			return (int) _empiricDelivery.sample();
		} else {
			return (int) _empiricTruck.sample();
		}
	}
}
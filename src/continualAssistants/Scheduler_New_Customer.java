package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import OSPRNG.UniformContinuousRNG;
import entities.CarType;
import entities.Customer;
import entities.States.StateCustomer;
import simulation.*;
import agents.*;

import java.util.Random;

//meta! id="15"
public class Scheduler_New_Customer extends Scheduler
{
	private static UniformContinuousRNG uniContGent = new UniformContinuousRNG(0.0,1.0);
	public Scheduler_New_Customer(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="Agent_Surroundings", id="54", type="Notice"
	public void processInit(MessageForm message)
	{
		message.setCode(Mc.newCustomer);
		hold(0, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.newCustomer:
				double time = myAgent().getExp().sample();
//				if (mySim().currentTime() + time > Config.latestArrivalTime - Config.startTimeSTK) {break;}
				if (mySim().currentTime() > Config.latestArrivalTime - Config.startTimeSTK) {return;}
				MyMessage copy = (MyMessage) message.createCopy();
				copy.setAddressee(myAgent());
				Customer customer = new Customer(mySim(), getCarType(), StateCustomer.QUEUE_CAR);
				customer.setStartTimeInQueue(mySim().currentTime());
				copy.setCustomer(customer);
				notice(copy);

				hold(time, message);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
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

	public CarType getCarType() {
		double value = uniContGent.sample();
		if (value < 0.65) {
			return CarType.PERSONAL;
		} else if (value < (0.65+0.21)) {
			return CarType.DELIVERY;
		} else {
			return  CarType.TRUCK;
		}
	}
}
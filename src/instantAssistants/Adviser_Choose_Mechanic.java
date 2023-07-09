package instantAssistants;

import OSPABA.*;
import entities.CarType;
import entities.Mechanic;
import simulation.*;
import agents.*;

//meta! id="145"
public class Adviser_Choose_Mechanic extends Adviser
{
	public Adviser_Choose_Mechanic(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getParkedCar().getCarType() == CarType.TRUCK) {
			myMessage.setMechanic(myAgent().getMechanicType2());
		} else {
			Mechanic mechanic = myAgent().getMechanicType1();
			if (mechanic == null) {
				mechanic = myAgent().getMechanicType2();
			}
			myMessage.setMechanic(mechanic);
		}
	}

	@Override
	public Agent_Personnel myAgent()
	{
		return (Agent_Personnel)super.myAgent();
	}

}
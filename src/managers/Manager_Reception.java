package managers;

import OSPABA.*;
import entities.Customer;
import entities.States.StateCustomer;
import entities.States.StateMechanic;
import entities.States.StateTechnician;
import entities.Technician;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

import javax.sound.midi.Soundbank;

//meta! id="4"
public class Manager_Reception extends Manager
{
	public Manager_Reception(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="Agent_STK", id="37", type="Response"
	public void processAllocateTechnician(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getTechnician() == null) {
			throw new RuntimeException();
		}
		if (myMessage.getCustomer().wantToPay()) {
			startPayment(myMessage);
		} else {
			transportCar(myMessage);
		}
	}

	//meta! sender="Process_Payment_Customer", id="64", type="Notice"
	public void processEndPayment(MessageForm message)
	{
		myAgent().decPayingCustomersQuantity();//Customer_End_Payment

		MyMessage myMessage = (MyMessage) message.createCopy();

		message.setCode(Mc.serveCustomer);
		response(message);

		setNewCustomerToTechnician((MyMessage) myMessage);
	}
	//meta! sender="Process_Transfer_Car", id="24", type="Notice"
	public void processEndTransfer(MessageForm message)
	{
		myAgent().decCarsTransportingQuantity();

		MyMessage myMessage = (MyMessage) message.createCopy();

		((MyMessage)message).setTechnician(null);
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.parkedCar);
		notice(message);

		setNewCustomerToTechnician((MyMessage) myMessage);
	}
	private void setNewCustomerToTechnician(MyMessage message) {
		if (message.getTechnician().isLunchBreak()){freeTechnician(message);return;}
		if (myAgent().isCustomerInQueue()) { //New customer for technician
			if (myAgent().showCustomerFromQueue().wantToPay()) {
				message.setCustomer(myAgent().getCustomerFromQueue());
				startPayment(message);
			} else {
				message.setCustomer(myAgent().peekCustomerFromQueue());
				allocateParkingSlot(message);
			}
		} else {	//queue is empty
			freeTechnician(message);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="Agent_STK", id="152", type="Request"
	public void processServeCustomer(MessageForm message)
	{
		myAgent().setArrivalMessage((MyMessage) message.createCopy());
		MyMessage myMessage = (MyMessage)message;
		if (!myAgent().isCustomerInQueue()) {
			isTechnicianAvailable(myMessage);
		} else {
//			addCustomerToQueue(myMessage);
			if (!myAgent().isCustomerInQueue(myMessage.getCustomer())) {
				addCustomerToQueue(myMessage);
			}
		}
	}
	//meta! sender="Agent_STK", id="155", type="Notice"
	public void processPaymentCustomer(MessageForm message)
	{
		MyMessage myMessage = (MyMessage)message;
		myMessage.getCustomer().setWantsToPay(true);

		myMessage.getCustomer().setEndTimeInWaitingCheck(mySim().currentTime());
		myAgent().getAvgCustomerWaitingTimeCheck().addSample(myMessage.getCustomer().getWaitingTimeInWaitingCheck());

		myMessage.getCustomer().setStartTimeInQueuePay(mySim().currentTime());

		isTechnicianAvailable(myMessage);
	}
	//meta! sender="Agent_STK", id="93", type="Response"
	public void processAllocateParkingSlot(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.isAllocatedParkingSlot()) {
			if (myMessage.getTechnician() == null) {
//				if (((MyMessage)message).getTwoAvailableTechnicians()) {
//					myMessage.setCustomer(myAgent().getCustomerFromQueue());
//					((MyMessage)message).setTwoAvailableTechnicians(false);
//				}
				if (((MyMessage)message).getCustomer().equals(myAgent().showCustomerFromQueue())) {
					myMessage.setCustomer(myAgent().getCustomerFromQueue());
					((MyMessage)message).setTwoAvailableTechnicians(false);
				}
				allocateTechnician(myMessage);
			} else {
				myMessage.setCustomer(myAgent().getCustomerFromQueue());
				transportCar(myMessage);
			}
		} else {
			if(!myAgent().isCustomerInQueue(myMessage.getCustomer())) {
				myMessage.setTechnician(null);
//				addCustomerToQueue(myMessage);
				if (!myAgent().isCustomerInQueue(myMessage.getCustomer())) {
					addCustomerToQueue(myMessage);
				}
			} else {
				freeTechnician(myMessage);
			}
		}
	}

	//meta! sender="Agent_STK", id="137", type="Response"
	public void processIsTechnicianAvailable(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.msgResult() == 1) {
			myMessage.setMsgResult(0);
			if (myMessage.getCustomer().wantToPay()) {
				if (myMessage.getTechnician() == null) {
					allocateTechnician(myMessage);
				} else {
					startPayment(myMessage);
				}
			} else {
				allocateParkingSlot(myMessage);
			}
		} else {
			if (!myAgent().isCustomerInQueue(myMessage.getCustomer())) {
				addCustomerToQueue(myMessage);
			}
		}
	}
	private void addCustomerToQueue(MessageForm message) {
		MyMessage myMessage = (MyMessage) message;
		if (myMessage.getCustomer().wantToPay()) {
			myMessage.getCustomer().setState(StateCustomer.QUEUE_PAY);
			myAgent().addCustomerToQueue(myMessage.getCustomer());
		} else {
			myMessage.getCustomer().setState(StateCustomer.QUEUE_CAR);
			myAgent().getAvgCustomerCountQueueCar().add(1);
			myAgent().addCustomerToQueue(myMessage.getCustomer());
		}
	}

	//meta! sender="Agent_STK", id="158", type="Notice"
	public void processAvailableParking(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		if (myAgent().isCustomerInQueue() && myAgent().peekCustomerFromQueue().getState() == StateCustomer.QUEUE_CAR) {
			myMessage.setTechnician(null);
			myMessage.setCustomer(myAgent().peekCustomerFromQueue());
//			if (myMessage.getMechanic() == null || myMessage.getMechanic().getState() != StateMechanic.LUNCH_BREAK) {
//				//If Mechanic is after lunch, then I need only 1 technician
//				myMessage.setTwoAvailableTechnicians(true);
//			}
			if (myMessage.getMechanic() != null && myMessage.getMechanic().getState() != StateMechanic.LUNCH_BREAK) {
				//Mechanik opravil auto a presiel na dalsie
				myMessage.setTwoAvailableTechnicians(true);
			}
			isTechnicianAvailable(myMessage);
			myMessage.setMechanic(null);
		}
	}

	//meta! sender="Agent_STK", id="188", type="Notice"
	public void processTryAssignJobTechnician(MessageForm message)
	{
		MyMessage message1 = (MyMessage) myAgent().getArrivalMessage().createCopy();
		message1.setTechnician(((MyMessage) message).getTechnician());
		setNewCustomerToTechnician(message1);
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
		case Mc.allocateTechnician:
			processAllocateTechnician(message);
		break;

		case Mc.availableParking:
			processAvailableParking(message);
		break;

		case Mc.endTransfer:
			processEndTransfer(message);
		break;

		case Mc.endPayment:
			processEndPayment(message);
		break;

		case Mc.serveCustomer:
			processServeCustomer(message);
		break;

		case Mc.paymentCustomer:
			processPaymentCustomer(message);
		break;

		case Mc.allocateParkingSlot:
			processAllocateParkingSlot(message);
		break;

		case Mc.isTechnicianAvailable:
			processIsTechnicianAvailable(message);
		break;

		case Mc.tryAssignJobTechnician:
			processTryAssignJobTechnician(message);
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
	private void allocateTechnician(MyMessage message)
	{
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.allocateTechnician);
		request(message);
	}
	private void allocateParkingSlot(MyMessage message) {
		message.setAllocatedParkingSlot(false);
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.allocateParkingSlot);
		request(message);
	}
	private void freeTechnician(MyMessage message) {
		message.setAddressee(mySim().findAgent(Id.agent_STK));
		message.setCode(Mc.freeTechnician);
		notice(message);
	}
	private void startPayment(MyMessage message){
		myAgent().incPayingCustomersQuantity();

		message.getCustomer().setEndTimeInQueuePay(mySim().currentTime());
		myAgent().getAvgCustomerTimeInQueuePay().addSample(message.getCustomer().getWaitingTimeInQueuePay());

		message.getTechnician().setState(StateTechnician.SERVING_PAY, message.getCustomer());
		message.getCustomer().setState(StateCustomer.PAYING);
		message.setCode(Mc.startPayment);
		message.setAddressee(myAgent().findAssistant(Id.process_Payment_Customer));
		notice(message);
	}

	private void transportCar(MyMessage myMessage) {
		myAgent().incCarsTransportingQuantity();
		myAgent().getAvgCustomerCountQueueCar().delete(1);

		myMessage.getCustomer().setEndTimeInQueue(mySim().currentTime());
		myAgent().getAvgCustomerTimeInQueueCar().addSample(myMessage.getCustomer().getWaitingTimeInQueue());

		myMessage.getCustomer().setStartTimeWaitingCheck(mySim().currentTime());

		myMessage.getTechnician().setState(StateTechnician.TRANSPORTING_CAR, myMessage.getCustomer());
		myMessage.getCustomer().setState(StateCustomer.WAITING_CAR);

		myMessage.setCode(Mc.startTransfer);
		myMessage.setAddressee(myAgent().findAssistant(Id.process_Transfer_Car));
		notice(myMessage);
	}

	private void isTechnicianAvailable(MyMessage myMessage) {
		myMessage.setAddressee(mySim().findAgent(Id.agent_STK));
		myMessage.setCode(Mc.isTechnicianAvailable);
		request(myMessage);
	}
}
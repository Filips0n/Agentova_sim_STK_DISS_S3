package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import entities.Actor;
import entities.Customer;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;
import statistics.Arithmetic_Mean;
import statistics.Weighted_Arithmetic_Mean;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

//meta! id="4"
public class Agent_Reception extends Agent
{
	private PriorityQueue<Customer> waitingQueue;
	private Weighted_Arithmetic_Mean avgCustomerCountQueueCar;
	private int payingCustomersQuantity;
	private int carsTransportingQuantity;
	private Stat avgCustomerTimeInQueueCar;
	private Stat avgCustomerTimeInPayQueue;
	private Stat avgCustomerTimeWaitingCheck;

	private MyMessage arrivalMessage;

	public Agent_Reception(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		waitingQueue = new PriorityQueue<>();
		avgCustomerCountQueueCar = new Weighted_Arithmetic_Mean(mySim());
		avgCustomerTimeInQueueCar = new Stat();
		avgCustomerTimeInPayQueue = new Stat();
		avgCustomerTimeWaitingCheck = new Stat();
		payingCustomersQuantity = 0;
		carsTransportingQuantity = 0;
		arrivalMessage = null;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new Manager_Reception(Id.manager_Reception, mySim(), this);
		new Process_Transfer_Car(Id.process_Transfer_Car, mySim(), this);
		new Process_Payment_Customer(Id.process_Payment_Customer, mySim(), this);
		addOwnMessage(Mc.allocateTechnician);
		addOwnMessage(Mc.endPayment);
		addOwnMessage(Mc.endTransfer);
		addOwnMessage(Mc.isTechnicianAvailable);
		addOwnMessage(Mc.startTransfer);
		addOwnMessage(Mc.serveCustomer);
		addOwnMessage(Mc.availableParking);
		addOwnMessage(Mc.paymentCustomer);
		addOwnMessage(Mc.allocateParkingSlot);
		addOwnMessage(Mc.startPayment);
		addOwnMessage(Mc.tryAssignJobTechnician);
	}
	//meta! tag="end"
	public boolean isCustomerInQueue() {
		return !waitingQueue.isEmpty();
	}

	public void addCustomerToQueue(Customer customer) {
		waitingQueue.add(customer);
	}

	public Weighted_Arithmetic_Mean getAvgCustomerCountQueueCar() {
		return avgCustomerCountQueueCar;
	}

	public Customer getCustomerFromQueue() {
		return this.waitingQueue.poll();
	}

	public Customer peekCustomerFromQueue() {
		return this.waitingQueue.peek();
	}

	public Customer showCustomerFromQueue() {
		return this.waitingQueue.peek();
	}
	public void decPayingCustomersQuantity() {
		this.payingCustomersQuantity--;
	}

	public void incPayingCustomersQuantity() {
		this.payingCustomersQuantity++;
	}

	public void decCarsTransportingQuantity() {
		this.carsTransportingQuantity--;
	}

	public void incCarsTransportingQuantity() {
		this.carsTransportingQuantity++;
	}

	public int getPayingCustomersQuantity() {
		return payingCustomersQuantity;
	}

	public int getCarsTransportingQuantity() {
		return carsTransportingQuantity;
	}

	public Stat getAvgCustomerTimeInQueueCar() {
		return avgCustomerTimeInQueueCar;
	}


	public Stat getAvgCustomerTimeInQueuePay() {
		return avgCustomerTimeInPayQueue;
	}

	public Stat getAvgCustomerWaitingTimeCheck() {
		return avgCustomerTimeWaitingCheck;
	}

	public ArrayList<ArrayList<String>> getAllInQueue() {
		PriorityQueue<Customer> sortedQueue = new PriorityQueue<>(waitingQueue);
		List<Actor> sortedList = new ArrayList<>();
		while (!sortedQueue.isEmpty()) {
			sortedList.add(sortedQueue.poll());
		}
		return SimUtils.getStringList(sortedList);
	}

	public int getWaitingCustomersQuantity() {
		return this.waitingQueue.size();
	}

	public int getWaitingPayCustomersQuantity() {
		int value = 0;
		for (Customer customer: this.waitingQueue) {
			if (customer.wantToPay()) {value++;}
		}
		return value;
	}

	public int getWaitingNewCustomersQuantity() {
		int value = 0;
		for (Customer customer: this.waitingQueue) {
			if (!customer.wantToPay()) {value++;}
		}
		return value;
	}

	public boolean isCustomerInQueue(Customer customer) {
		boolean value = false;

		for (Entity customer2 : waitingQueue) {
			if (customer2.equals(customer)) {return true;}
		}

		return value;
	}

	public void setArrivalMessage(MyMessage message) {
		if (arrivalMessage == null) {
			arrivalMessage = (MyMessage) message.createCopy();
		}
	}

	public MyMessage getArrivalMessage() {
		return arrivalMessage;
	}

}
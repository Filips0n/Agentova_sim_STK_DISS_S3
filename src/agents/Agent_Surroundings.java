package agents;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import OSPStat.Stat;
import OSPStat.WStat;
import entities.Actor;
import entities.Customer;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;
import statistics.Weighted_Arithmetic_Mean;
import structures.BoundedQueue;

import java.util.ArrayList;

//meta! id="2"
public class Agent_Surroundings extends Agent
{
	private int customersIn;
	private int customersOut;
	private ArrayList<Actor> allCustomers;
	private Weighted_Arithmetic_Mean avgCustomerCountSystem;
	private Stat avgCustomerTimeInSystem;

	private static ExponentialRNG exp;

	public Agent_Surroundings(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		customersIn = 0;
		customersOut = 0;
		allCustomers = new ArrayList<>();
		avgCustomerCountSystem = new Weighted_Arithmetic_Mean(mySim());
		avgCustomerTimeInSystem = new Stat();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new Manager_Surroundings(Id.manager_Surroundings, mySim(), this);
		new Scheduler_New_Customer(Id.scheduler_New_Customer, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.newCustomer);
		addOwnMessage(Mc.leftCustomer);
	}
	//meta! tag="end"

	public void incCustomersIn() {
		customersIn++;
	}

	public void incCustomersOut() {
		customersOut++;
	}

	public int getCustomersIn() {
		return customersIn;
	}

	public int getCustomersOut() {
		return customersOut;
	}

	public void addCustomerToAllCustomers(Customer customer) {
		this.allCustomers.add(customer);
	}

	public Weighted_Arithmetic_Mean getAvgCustomerCountSystem() {
		return avgCustomerCountSystem;
	}

	public Stat getAvgCustomerTimeInSystem() {
		return avgCustomerTimeInSystem;
	}

	public ArrayList<Actor> getCustomers() {
		return allCustomers;
	}
	public ArrayList<ArrayList<String>> getAllCustomers() {
		return SimUtils.getStringList(allCustomers);
	}

	public static double applyPercentage(double number, double percentage) {
		double change = (percentage / 100) * number;
		return number + change;
	}

	public void createGenerator() {
		exp = new ExponentialRNG((double)3600/applyPercentage(23, Config.arrivalCustomerFlowChange));
	}

	public static ExponentialRNG getExp() {
		return exp;
	}
}
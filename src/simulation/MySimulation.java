package simulation;

import OSPABA.*;
import OSPStat.Stat;
import OSPStat.WStat;
import agents.*;
import entities.Actor;
import entities.Car;
import entities.Customer;
import entities.States.StateCustomer;
import managers.Manager_Personnel;
import managers.Manager_Reception;
import managers.Manager_Workshop;
import org.jfree.data.function.NormalDistributionFunction2D;
import statistics.Weighted_Arithmetic_Mean;

import java.util.*;

public class MySimulation extends Simulation
{
	private SimulationMode simMode;
	private Stat avgSimCustomerTimeInSystem;
	private Stat avgSimCustomerTimeInQueueCar;
	private Stat avgSimCustomerCountQueueCar;
	private Stat avgSimCustomerCountSystem;
	private Stat avgSimTechnicianFreeCount;
	private Stat avgSimMechanicFreeCount;
	private Stat avgSimCarInSystemCount;
	private Stat avgSimCustomersInSystemCount;

	private Stat avgSimCustomerTimeInPayQueue;
	private Stat avgSimCustomerTimeWaitingCheck;
	public MySimulation()
	{
		init();
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
		this.avgSimCustomerTimeInSystem = new Stat();
		this.avgSimCustomerTimeInQueueCar = new Stat();
		this.avgSimCustomerCountQueueCar = new Stat();
		this.avgSimCustomerCountSystem = new Stat();
		this.avgSimTechnicianFreeCount = new Stat();
		this.avgSimMechanicFreeCount = new Stat();
		this.avgSimCarInSystemCount = new Stat();
		this.avgSimCustomersInSystemCount = new Stat();
		this.avgSimCustomerTimeWaitingCheck = new Stat();
		this.avgSimCustomerTimeInPayQueue = new Stat();

		_agent_Surroundings.createGenerator();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
		agent_Model().startSimulation();
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
		_agent_Personnel.updateStatsAfterReplication();//todo stats
		/**/
		this.getAvgCustomerCountSystem().delete(0);
		this.avgSimCustomerCountSystem.addSample(this.getAvgCustomerCountSystem().getWeightedMean());//WAM
		this.avgSimMechanicFreeCount.addSample(_agent_Personnel.getAvailableMechanics().lengthStatistic().mean());
		this.avgSimTechnicianFreeCount.addSample(_agent_Personnel.getAvailableTechnicians().lengthStatistic().mean());
		this.getAvgCustomerCountQueueCar().delete(getWaitingNewCustomersQuantity());//WAM
		this.avgSimCustomerCountQueueCar.addSample(this.getAvgCustomerCountQueueCar().getWeightedMean());
//		endTimeInSystemToCustomers();
		this.avgSimCustomerTimeInSystem.addSample(this.getAvgCustomerTimeInSystem().mean());
		this.avgSimCustomerTimeInQueueCar.addSample(this.getAvgCustomerTimeInQueueCar().mean());
		this.avgSimCarInSystemCount.addSample(this.getCarsInSTKQuantity());
		this.avgSimCustomersInSystemCount.addSample(this.getCustomersInSTKAtClosure());
		this.avgSimCustomerTimeInPayQueue.addSample(_agent_Reception.getAvgCustomerTimeInQueuePay().mean());
		this.avgSimCustomerTimeWaitingCheck.addSample(_agent_Reception.getAvgCustomerWaitingTimeCheck().mean());
 	}

	@Override
	public void simulationFinished()
	{
		// Display simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgent_Model(new Agent_Model(Id.agent_Model, this, null));
		setAgent_Surroundings(new Agent_Surroundings(Id.agent_Surroundings, this, agent_Model()));
		setAgent_STK(new Agent_STK(Id.agent_STK, this, agent_Model()));
		setAgent_Reception(new Agent_Reception(Id.agent_Reception, this, agent_STK()));
		setAgent_Personnel(new Agent_Personnel(Id.agent_Personnel, this, agent_STK()));
		setAgent_Workshop(new Agent_Workshop(Id.agent_Workshop, this, agent_STK()));
		setAgent_Parking(new Agent_Parking(Id.agent_Parking, this, agent_STK()));
	}

	private Agent_Model _agent_Model;

public Agent_Model agent_Model()
	{ return _agent_Model; }

	public void setAgent_Model(Agent_Model agent_Model)
	{_agent_Model = agent_Model; }

	private Agent_Surroundings _agent_Surroundings;

public Agent_Surroundings agent_Surroundings()
	{ return _agent_Surroundings; }

	public void setAgent_Surroundings(Agent_Surroundings agent_Surroundings)
	{_agent_Surroundings = agent_Surroundings; }

	private Agent_STK _agent_STK;

public Agent_STK agent_STK()
	{ return _agent_STK; }

	public void setAgent_STK(Agent_STK agent_STK)
	{_agent_STK = agent_STK; }

	private Agent_Reception _agent_Reception;

public Agent_Reception agent_Reception()
	{ return _agent_Reception; }

	public void setAgent_Reception(Agent_Reception agent_Reception)
	{_agent_Reception = agent_Reception; }

	private Agent_Personnel _agent_Personnel;

public Agent_Personnel agent_Personnel()
	{ return _agent_Personnel; }

	public void setAgent_Personnel(Agent_Personnel agent_Personnel)
	{_agent_Personnel = agent_Personnel; }

	private Agent_Workshop _agent_Workshop;

public Agent_Workshop agent_Workshop()
	{ return _agent_Workshop; }

	public void setAgent_Workshop(Agent_Workshop agent_Workshop)
	{_agent_Workshop = agent_Workshop; }

	private Agent_Parking _agent_Parking;

public Agent_Parking agent_Parking()
	{ return _agent_Parking; }

	public void setAgent_Parking(Agent_Parking agent_Parking)
	{_agent_Parking = agent_Parking; }
	//meta! tag="end"
	public Stat getAvgSimCustomerCountQueueCar() {
		return avgSimCustomerCountQueueCar;
	}
	public Stat getAvgSimCustomerTimeInSystem() {
		return avgSimCustomerTimeInSystem;
	}

	public Stat getAvgSimCustomerTimeInQueueCar() {
		return avgSimCustomerTimeInQueueCar;
	}

	public Stat getAvgSimCustomerCountSystem() {
		return avgSimCustomerCountSystem;
	}

	public Stat getAvgSimTechnicianFreeCount() {
		return avgSimTechnicianFreeCount;
	}

	public Stat getAvgSimMechanicFreeCount() {
		return avgSimMechanicFreeCount;
	}

	public Stat getAvgSimCarInSystemCount() {
		return avgSimCarInSystemCount;
	}

	public int getTotalCustomers() {
		return _agent_Surroundings.getCustomersIn();
	}

	public int getWaitingCustomersQuantity() {
		return _agent_Reception.getWaitingCustomersQuantity();
	}

	public int getWaitingNewCustomersQuantity() {
		return _agent_Reception.getWaitingNewCustomersQuantity();
	}

	public int getWaitingPayCustomersQuantity() {
		return _agent_Reception.getWaitingPayCustomersQuantity();
	}

	public int getPayingCustomersQuantity() {
		return _agent_Reception.getPayingCustomersQuantity();
	}

	public ArrayList<ArrayList<String>> getAllCustomers() {
		return _agent_Surroundings.getAllCustomers();
	}

	public ArrayList<ArrayList<String>> getAllTechnicians() {
		return _agent_Personnel.getAllTechnicians();
	}

	public ArrayList<ArrayList<String>> getAllMechanics() {
		return _agent_Personnel.getAllMechanics();
	}

	public ArrayList<ArrayList<String>> getAllInQueue() {
		return _agent_Reception.getAllInQueue();
	}

	public ArrayList<ArrayList<String>> getAllCarsWaiting() {
		return _agent_Parking.getAllCarsWaiting();
	}
	public int getCarWaitingCustomersQuantity() {
		return (Config.mechanicsQuantity-getAvailableMechanics()) + getCarsQuantityTransporting() + _agent_Parking.getWorkShopParking().size();
	}

	public int getTotalLeftCustomers() {
		return _agent_Surroundings.getCustomersOut();
	}

	public int getAvailableTechnicians() {
		return _agent_Personnel.getAvailableTechniciansQuantity();
	}

	public int getAvailableMechanics() {
		return _agent_Personnel.getAvailableMechanicsQuantity();
	}

	public int getWorkShopParkingCount() {
		return _agent_Parking.getWorkShopParking().size();
	}

	public int getCarsQuantityTransporting() {
		return _agent_Reception.getCarsTransportingQuantity();
	}

	public Stat getAvgCustomerTimeInSystem() {
		return _agent_Surroundings.getAvgCustomerTimeInSystem();
	}

	public Stat getAvgCustomerTimeInQueueCar() {
		return _agent_Reception.getAvgCustomerTimeInQueueCar();
	}

	public Stat getAvgSimCustomerTimeInPayQueue() {
		return avgSimCustomerTimeInPayQueue;
	}

	public Stat getAvgSimCustomerTimeWaitingCheck() {
		return avgSimCustomerTimeWaitingCheck;
	}

	public Weighted_Arithmetic_Mean getAvgCustomerCountQueueCar() {
		return _agent_Reception.getAvgCustomerCountQueueCar();
	}

	public Weighted_Arithmetic_Mean getAvgCustomerCountSystem() {
		return _agent_Surroundings.getAvgCustomerCountSystem();
	}

	public WStat getAvgTechnicianFreeCount() {
		return _agent_Personnel.getAvgTechnicianFreeCount();
	}

	public WStat getAvgMechanicFreeCount() {
		return _agent_Personnel.getAvgMechanicFreeCount();
	}

	public void setSimMode(SimulationMode simMode) {
		this.simMode = simMode;
	}

	public Stat getAvgSimCustomersInSystemCount() {
		return avgSimCustomersInSystemCount;
	}

	public int getCarsInSTKQuantity() {
		return _agent_Parking.getWorkShopParking().size() + _agent_Workshop.getCarsChecking()
				+ getCarsQuantityTransporting() + getPayingCustomersQuantity() + _agent_Reception.getWaitingPayCustomersQuantity();
	}

	public int getCustomersInSTKAtClosure(){
		return _agent_Parking.getWorkShopParking().size() + _agent_Workshop.getCarsChecking()
				+ getCarsQuantityTransporting() + getPayingCustomersQuantity()
				+ _agent_Reception.getWaitingCustomersQuantity();
	}

	private void endTimeInSystemToCustomers() {
		for (Actor customer : _agent_Surroundings.getCustomers()) {
			if (!(((Customer)customer).getState() == StateCustomer.LEFT)) {
				((Customer)customer).setEndTimeInSystem(Config.endTimeSTK);
				getAvgCustomerTimeInSystem().addSample(((Customer)customer).getWaitingTimeInSystem());
			}
		}
	}

	public int getCarsChecking() {
		return _agent_Workshop.getCarsChecking();
	}

	public Stat getAvgCustomerTimeInPayQueue() {
		return _agent_Reception.getAvgCustomerTimeInQueuePay();
	}

	public Stat getAvgCustomerTimeWaitingCheck() {
		return _agent_Reception.getAvgCustomerWaitingTimeCheck();
	}
}
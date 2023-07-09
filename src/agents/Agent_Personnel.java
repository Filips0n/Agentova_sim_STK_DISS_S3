package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import entities.Actor;
import entities.LicenceType;
import entities.Mechanic;
import entities.States.StateMechanic;
import entities.States.StateTechnician;
import entities.Technician;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;
import statistics.Weighted_Arithmetic_Mean;

import java.util.ArrayList;

//meta! id="5"
public class Agent_Personnel extends Agent
{
	private ArrayList<Actor> allTechnicians;
	private ArrayList<Actor> allMechanics;
	private SimQueue<Mechanic> availableMechanics;
	private SimQueue<Technician> availableTechnicians;
	public Weighted_Arithmetic_Mean availableMechanicsWAM;
	public Weighted_Arithmetic_Mean availableTechniciansWAM;

	public Agent_Personnel(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		allTechnicians = new ArrayList<>(Config.techniciansQuantity);
		allMechanics = new ArrayList<>(Config.mechanicsQuantity);
		availableTechnicians = new SimQueue<>(new WStat(mySim()));
		availableMechanics = new SimQueue<>(new WStat(mySim()));
		availableMechanicsWAM = new Weighted_Arithmetic_Mean(mySim());
		availableTechniciansWAM = new Weighted_Arithmetic_Mean(mySim());

		availableTechniciansWAM.add(Config.techniciansQuantity);
		for (int i = 0; i < Config.techniciansQuantity; i++) {
			Technician technician = new Technician(mySim());
			allTechnicians.add(technician);
			availableTechnicians.enqueue(technician);
		}

		if (Config.modificationsEnabled) {
			Config.mechanicsQuantity = Config.mechanics1Quantity + Config.mechanics2Quantity;
		}

		availableMechanicsWAM.add(Config.mechanicsQuantity);
		for (int i = 0; i < Config.mechanicsQuantity; i++) {
			Mechanic mechanic = new Mechanic(mySim());
			allMechanics.add(mechanic);
			availableMechanics.enqueue(mechanic);
		}

		if (Config.modificationsEnabled) {
			for (int i = 0; i < Config.mechanics1Quantity; i++) {
				((Mechanic)allMechanics.get(i)).setLicence(LicenceType.DELIVERY_PERSONAL);
			}
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new Manager_Personnel(Id.manager_Personnel, mySim(), this);
		new Process_Lunch_Break(Id.process_Lunch_Break, mySim(), this);
		new Adviser_Choose_Mechanic(Id.adviser_Choose_Mechanic, mySim(), this);
		addOwnMessage(Mc.allocateTechnician);
		addOwnMessage(Mc.isTechnicianAvailable);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.startLunchBreak);
		addOwnMessage(Mc.freeTechnician);
		addOwnMessage(Mc.lunchBreak);
		addOwnMessage(Mc.holdToLunchBreak);
		addOwnMessage(Mc.endLunchBreak);
		addOwnMessage(Mc.allocateMechanic);
		addOwnMessage(Mc.isMechanicAvailable);
		addOwnMessage(Mc.freeMechanic);
	}
	//meta! tag="end"
	public int getAvailableTechniciansQuantity() {
		return availableTechnicians.size();
	}

	public int getAvailableMechanicsQuantity() {
		return availableMechanics.size();
	}

	public Mechanic getMechanic() {
		for (Actor mechanic : allMechanics) {
			if (((Mechanic) mechanic).getState() == StateMechanic.FREE) {return (Mechanic) mechanic;}
		}
		return null;
	}
	public boolean isMechanicAvailable() {
		return availableMechanics.size() > 0;
	}
	public boolean isTechnicianAvailable() {
		return availableTechnicians.size() > 0;
	}
	public Technician getTechnician() {
		for (Actor technician : allTechnicians) {
			if (((Technician) technician).getState() == StateTechnician.FREE) {return (Technician)technician;}
		}
		return null;
	}

	public ArrayList<ArrayList<String>> getAllTechnicians() {
		return SimUtils.getStringList(allTechnicians);
	}

	public ArrayList<ArrayList<String>> getAllMechanics() {
		return SimUtils.getStringListMechanic(allMechanics);
	}

	public SimQueue<Mechanic> getAvailableMechanics() {
		return availableMechanics;
	}

	public SimQueue<Technician> getAvailableTechnicians() {
		return availableTechnicians;
	}

	public void updateStatsAfterReplication(){
		availableTechnicians.lengthStatistic().updateAfterReplication();
		availableMechanics.lengthStatistic().updateAfterReplication();
	}
	public boolean areTwoTechnicianAvailable() {
		return availableTechnicians.size() > 1;
	}

	public Mechanic getMechanicType2() {
		for (Actor mechanic : allMechanics) {
			if (((Mechanic) mechanic).getState() == StateMechanic.FREE
					&& ((Mechanic) mechanic).getLicence() == LicenceType.TRUCK_DELIVERY_PERSONAL) {return (Mechanic) mechanic;}
		}
		return null;
	}

	public Mechanic getMechanicType1() {
		for (Actor mechanic : allMechanics) {
			if (((Mechanic) mechanic).getState() == StateMechanic.FREE
					&& ((Mechanic) mechanic).getLicence() == LicenceType.DELIVERY_PERSONAL) {return (Mechanic) mechanic;}
		}
		return null;
	}

	public boolean isMechanicType2Available() {
		for (Actor mechanic : allMechanics) {
			if (((Mechanic) mechanic).getState() == StateMechanic.FREE
					&& ((Mechanic) mechanic).getLicence() == LicenceType.TRUCK_DELIVERY_PERSONAL) {return true;}
		}
		return false;
	}
	public boolean isMechanic1Available() {
		for (Actor mechanic : allMechanics) {
			if (((Mechanic) mechanic).getState() == StateMechanic.FREE
					&& ((Mechanic) mechanic).getLicence() == LicenceType.DELIVERY_PERSONAL) {return true;}
		}
		return false;
	}

	public ArrayList<Actor> allMechanics() {
		return allMechanics;
	}

	public ArrayList<Actor> allTechnicians() {
		return allTechnicians;
	}

	public WStat getAvgTechnicianFreeCount() {
		return availableTechnicians.lengthStatistic();
	}

	public WStat getAvgMechanicFreeCount() {
		return availableMechanics.lengthStatistic();
	}
}
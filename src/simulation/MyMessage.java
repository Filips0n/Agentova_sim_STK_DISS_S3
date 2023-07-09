package simulation;

import OSPABA.*;
import entities.*;

public class MyMessage extends MessageForm
{
	private Customer customer;
	private Technician technician;
	private Mechanic mechanic;
	private boolean isAllocatedParkingSlot;

	private Car parkedCar;
	private boolean twoAvailableTechnicians;
	private boolean newCarToMechanic;
	private LicenceType licence;

	public MyMessage(Simulation sim)
	{
		super(sim);

		this.customer = null;
		this.technician = null;
		this.mechanic = null;
		isAllocatedParkingSlot = false;
		this.parkedCar = null;
		this.licence = null;
	}

	public MyMessage(MyMessage original)
	{
		super(original);
		// copy() is called in superclass
		copy((MyMessage)original);
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		// Copy attributes
		this.customer = original.customer;
		this.mechanic = original.mechanic;
		this.technician = original.technician;
		this.isAllocatedParkingSlot = original.isAllocatedParkingSlot;
		this.parkedCar = original.parkedCar;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public void setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	public void setParkedCar(Car parkedCar) {
		this.parkedCar = parkedCar;
	}

	public void setAllocatedParkingSlot(boolean allocatedParkingSlot) {
		isAllocatedParkingSlot = allocatedParkingSlot;
	}

	public Customer getCustomer()
	{ return this.customer; }

	public Technician getTechnician()
	{ return technician; }

	public Mechanic getMechanic()
	{ return mechanic; }

	public Car getParkedCar()
	{ return parkedCar; }

	public boolean isAllocatedParkingSlot()
	{ return isAllocatedParkingSlot; }

	public void setTwoAvailableTechnicians(boolean value) {
		twoAvailableTechnicians = value;
	}

	public boolean getTwoAvailableTechnicians() {
		return twoAvailableTechnicians;
	}

	public void setMechanicLicence(LicenceType licenceType) {
		this.licence = licenceType;
	}

	public LicenceType getMechanicLicence() {
		return this.licence;
	}

}
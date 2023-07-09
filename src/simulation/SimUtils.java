package simulation;

import entities.Actor;
import entities.Mechanic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SimUtils {
    public static ArrayList<ArrayList<String>> getStringList(Collection<Actor> allActors) {
        ArrayList<ArrayList<String>> stringList = new ArrayList<>();

        for (Actor actor: allActors) {
            stringList.add(new ArrayList<>(Arrays.asList(actor.getName(), actor.getStateDesc(), actor.getCarDesc())));
        }

        return stringList;
    }

    public static ArrayList<ArrayList<String>> getStringListMechanic(Collection<Actor> allActors) {
        ArrayList<ArrayList<String>> stringList = new ArrayList<>();

        for (Actor actor: allActors) {
            stringList.add(new ArrayList<>(Arrays.asList(actor.getName(), actor.getStateDesc(), actor.getCarDesc(), ((Mechanic)actor).getLicenceDesc())));
        }

        return stringList;
    }

    public static String calculatePayment() {
        double value = (Config.techniciansQuantity * Config.technicianSalary) + (Config.mechanics1Quantity * Config.mechanicDeliveryCar) + (Config.mechanics2Quantity * Config.mechanicTruckDeliveryCar);
        return ""+value;
    }
}

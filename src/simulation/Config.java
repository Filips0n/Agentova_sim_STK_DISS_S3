package simulation;

public class Config {
    public static int maxParkingCapacity = 5;
    public static double startTimeSTK = 0;//seconds 32400-0
    public static double lunchBreakStartTime = 7200;//seconds 39600-7200
    public static double latestArrivalTime = 24300;//seconds 56700-24300
    public static double endTimeSTK = 28800;//seconds 61200-28800
    public static double lunchBreakTime = 1800;
    public static int techniciansQuantity=5;//4
    public static int mechanicsQuantity=17;//17
    public static int mechanics1Quantity=12;//17
    public static int mechanics2Quantity=7;//17
    public static int replicationQuantity=1000;

    public static double arrivalCustomerFlowChange=0;//%
    public static boolean modificationsEnabled = true;
    public static int technicianSalary = 1100;
    public static int mechanicTruckDeliveryCar = 2000;
    public static int mechanicDeliveryCar = 1500;
    public static double idealRatioOfMechanics = 0.631578947368421;

    public static double[][] DELIVERY_DISTRIBUTION = {
            {2100, 2220, 0.2},
            {2280, 2400, 0.35},
            {2460, 2820, 0.3},
            {2880, 3120, 0.15}
    };

    public static double[][] TRUCK_DISTRIBUTION = {
            {2220, 2520, 0.05},
            {2580, 2700, 0.1},
            {2760, 2820, 0.15},
            {2880, 3060, 0.4},
            {3120, 3300, 0.25},
            {3360, 3900, 0.05}
    };

    private Config() {};
}

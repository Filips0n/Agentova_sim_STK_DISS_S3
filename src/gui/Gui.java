package gui;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import org.jfree.chart.ChartPanel;
import simulation.Config;
import simulation.MySimulation;
import simulation.SimUtils;
import simulation.SimulationMode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Gui implements ISimDelegate {

    private final JFrame frame = new JFrame();
    private JButton startButton;
    private JButton resumeButton;
    private JButton pauseButton;
    private JButton terminateButton;

    private JLabel lblProbability;
    private JLabel lblAvgWaitTime;
    private Simulation simulation;

//    private JCheckBox cBsimulationMode = new JCheckBox("Zapnut rychly mod");
    private JLabel lblSimTimeValue = new JLabel("");
    private JTextField tfMechanicNumber;
    private JTextField tfReceptionNumber;
    private JTextField tfReplicationNumber;
    private JLabel lblBusyTechValue = new JLabel("");
    private JLabel lblFreeTechValue = new JLabel("");
    private JLabel lblBusyMechanicsValue = new JLabel("");
    private JLabel lblFreeMechanicsValue = new JLabel("");
    private JLabel lblCurrentParkingCapacityValue = new JLabel("");
    private JLabel lblCarsTransportingQuantityValue = new JLabel("");
    private JLabel lblWaitingCustomersQuantityValue = new JLabel("");
    private JLabel lblWaitingPayCustomersQuantityValue = new JLabel("");
    private JLabel lblCarsInMySimulationQuantityValue = new JLabel("");
    private JLabel lblPayingCustomersQuantityValue = new JLabel("");
    private JLabel lblCurrentReplicationValue = new JLabel("");
    private JLabel lblMoney = new JLabel("");
    private JPanel settingsPanelBeforeSim = new JPanel();
    private JPanel settingsPanelSim = new JPanel();
    private JPanel northPanel = new JPanel();
    private JPanel eastPanel = new JPanel();

    private JSlider timeScaleSlider;
    private JSpinner spinner;
    private JTable customerTable;
    private DefaultTableModel customerModel;
    private DefaultTableModel employeesModel;
    private DefaultTableModel carsModel;
    private DefaultTableModel statsModel;
    private DefaultTableModel statsModelSim;
    private JPanel outputPanelReplication;
    private JPanel outputPanelReplicationCenter;
    private JPanel outputPanelSimulation;
    private JComboBox<String> cbSimMode;
    private JPanel graphPanel;
    private MySpinnerNumberModel spinnerModel;
    private MyTableModel allCustomersModel;
    private MyTableModel allTechniciansModel;
    private MyTableModel allMechanicsModel;
    private MyTableModel allCarsWaitingModel;
    private MyTableModel allInQueueModel;
    private JPanel centerPanel = new JPanel();
    private JPanel buttonPanelGraphsGUI;
    private JPanel buttonPanelMain;
    private JPanel buttonPanelWrapper = new JPanel();
    private MyXYLineChart chart2;
    private MyXYLineChart chart;
    private JButton graphsGuiBtn1;
    private JButton graphsGuiBtn2;
    private JButton graphsGuiBtnEnd;
    private boolean graph1Active;
    private boolean endGraph = false;
    private JCheckBox cBModifications;
    private JTextField tfMechanicNumber1;
    private JTextField tfMechanicNumber2;
    private JTextField tfArrivalCustomerFlowChange;

    public Gui(MySimulation mySimulation){
        this.simulation = mySimulation;
        this.simulation.registerDelegate(this);
        graphsGui();
        simSettings();
        lblMoney.setFont(new Font("Calibri", Font.PLAIN, 16));
        lblMoney.setPreferredSize(new Dimension(100, 20));

        frame.add(eastPanel, BorderLayout.EAST);
        frame.add(centerPanel, BorderLayout.CENTER);
        simulationOutput();
        replicationOutput();//Replication output
        outputPanelSimulation.setVisible(false);
        graphPanel.setVisible(false);

        initView();

        buttons();
        // Set the frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(960,540));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void simulationOutput() {
        JLabel lblCurrentReplication = new JLabel("Aktualna replikacia: ");

        outputPanelSimulation = new JPanel();
        outputPanelSimulation.setLayout(new BoxLayout(outputPanelSimulation, BoxLayout.Y_AXIS));

        lblCurrentReplication.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblCurrentReplicationValue.setFont(new Font("Calibri", Font.PLAIN, 16));

        outputPanelSimulation.add(lblCurrentReplication);
        outputPanelSimulation.add(lblCurrentReplicationValue);

        //TABLE
        String[] columnNamesS = {"Statistika", "Hodnota", "Interval spolahlivosti"};
        Object[][] dataS = {
                {"Priem. cas zakaznika v prevadzke", "", ""},
                {"Priem. cas cakania na odovzdanie auta", "", ""},
                {"Priem. cas cakania na zaplatenie", "", ""},
                {"Priem. cas cakania na ukoncenie kontroly", "", ""},

                {"Priem. pocet cakajucich v rade na odovzdanie auta", "", ""},
                {"Priem. pocet zakaznikov v systeme", "", ""},
                {"Priem. pocet volnych pracovnikov sk. 1", "", ""},
                {"Priem. pocet volnych pracovnikov sk. 2", "", ""},
                {"Priem. pocet aut v prevadzke na konci dna", "", ""},
                {"Priem. pocet zak. v prevadzke na konci dna", "", ""},
        };

        statsModelSim = new DefaultTableModel(dataS, columnNamesS);
        JTable statsTable = new JTable(statsModelSim);
        JScrollPane scrollPaneS = new JScrollPane(statsTable);
        setWidthColumn(statsTable, 2, 500);
        setWidthColumn(statsTable, 1, 500);
        setWidthColumn(statsTable, 0, 500);
        //end

        //Table font
        statsTable.setFont(new Font("", Font.PLAIN, 16));
        statsTable.setRowHeight(25);
        statsTable.getPreferredSize().width = 1500;
        Dimension tableSizeT = scrollPaneS.getPreferredSize();
        tableSizeT.width = 1500;
        tableSizeT.height = 275;
        scrollPaneS.setPreferredSize(tableSizeT);
        //

        //Spacing
        Box container = Box.createVerticalBox();
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(scrollPaneS);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(lblMoney);
        //

        outputPanelSimulation.add(container);

        centerPanel.add(outputPanelSimulation);

    }

    private void replicationOutput() {
        replicationOutputCenter();

        JLabel lblSimTime = new JLabel("Simulacny cas: ");

        outputPanelReplication = new JPanel();
        outputPanelReplication.setLayout(new BoxLayout(outputPanelReplication, BoxLayout.Y_AXIS));

        lblSimTime.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblSimTimeValue.setFont(new Font("Calibri", Font.PLAIN, 16));

        outputPanelReplication.add(lblSimTime);
        outputPanelReplication.add(lblSimTimeValue);

        //TABLE
        String[] columnNames = {"Stav", "Pocet"};
        Object[][] data = {
                {"Celkovy pocet zakaznikov", ""},
                {"Celkovy pocet cak. v rade", ""},
                {"Pocet cak. na odovzdanie auta", ""},
                {"Pocet cakajucich na platenie", ""},
                {"Pocet platiacich zakaznikov", ""},
                {"Pocet cak. na koniec kontroly", ""},
                {"Pocet odidenych zakaznikov", ""},
        };

        customerModel = new DefaultTableModel(data, columnNames);
        JTable customersTable = new JTable(customerModel);

        int[] rows = {0, 1, 2, 4};
        Color[] bgColors = {Color.BLUE, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.LIGHT_GRAY};
        Color[] textColors = {Color.WHITE, Color.BLACK, Color.white, Color.BLACK};

        colorRows(customersTable, rows, bgColors, textColors);

        setWidthColumn(customersTable, 1, 25);
        setWidthColumn(customersTable, 0, 270);
//        outputPanel.add(customerTable);
        //end

        //TABLE
        String[] columnNamesE = {"Stav", "Pocet"};
        Object[][] dataE = {
                {"Pocet obsadenych technikov", ""},
                {"Pocet volnych technikov", ""},
                {"Pocet obsadenych mechanikov", ""},
                {"Pocet volnych mechanikov", ""},
        };

        employeesModel = new DefaultTableModel(dataE, columnNamesE);
        JTable employeesTable = new JTable(employeesModel);

        int[] rowsE = {0, 1, 2, 3}; // specify the rows to color
        Color[] bgColorsE = {Color.YELLOW, Color.GREEN, Color.YELLOW, Color.GREEN};
        Color[] textColorsE = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};

        colorRows(employeesTable, rowsE, bgColorsE, textColorsE);

        setWidthColumn(employeesTable, 1, 25);
        setWidthColumn(employeesTable, 0, 270);
//        outputPanel.add(employeesTable);
        //end

        //TABLE
        String[] columnNamesC = {"Stav", "Pocet"};
        Object[][] dataC = {
                {"Pocet aut v prevadzke", ""},
                {"Pocet kontrolovanych aut", ""},
                {"Aktualna kapacita parkoviska", ""},
                {"Pocet aut v preprave", ""},
        };

        carsModel = new DefaultTableModel(dataC, columnNamesC);
        JTable carsTable = new JTable(carsModel);

        int[] rowsC = {0, 1};
        Color[] bgColorsC = {Color.BLUE, Color.LIGHT_GRAY};
        Color[] textColorsC = {Color.WHITE, Color.BLACK};

        colorRows(carsTable, rowsC, bgColorsC, textColorsC);

        setWidthColumn(carsTable, 1, 25);
        setWidthColumn(carsTable, 0, 270);
//        outputPanel.add(employeesTable);
        //end

        //TABLE
        String[] columnNamesS = {"Statistika", "Hodnota"};
        Object[][] dataS = {
                {"Priem. cas zakaznika v prevadzke", ""},
                {"Priem. cas cakania na odovzdanie auta", ""},
                {"Priem. cas cakania na zaplatenie", ""},
                {"Priem. cas cakania na ukoncenie kont.", ""},
                {"Priem. pocet cak. v rade na odovz. auta", ""},
                {"Priem. pocet zak. v systeme", ""},
                {"Priem. pocet volnych prac. sk. 1", ""},
                {"Priem. pocet volnych prac. sk. 2", ""},
                {"Pocet zakaznikov v systeme", ""},
        };

        statsModel = new DefaultTableModel(dataS, columnNamesS);
        JTable statsTable = new JTable(statsModel);

        setWidthColumn(statsTable, 1, 60);
        setWidthColumn(statsTable, 0, 270);
//        outputPanel.add(employeesTable);
        //end

        //Table font
        customersTable.setFont(new Font("", Font.PLAIN, 16));
        employeesTable.setFont(new Font("", Font.PLAIN, 16));
        carsTable.setFont(new Font("", Font.PLAIN, 16));
        statsTable.setFont(new Font("", Font.PLAIN, 16));
        customersTable.setRowHeight(25);
        employeesTable.setRowHeight(25);
        carsTable.setRowHeight(25);
        statsTable.setRowHeight(25);
        //

        //Spacing
        Box container = Box.createVerticalBox();
        container.add(customersTable);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(employeesTable);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(carsTable);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(statsTable);
//        container.add(Box.createRigidArea(new Dimension(0, 10)));
//        container.add(lblMoney);
        //

        outputPanelReplication.add(container);

        eastPanel.add(outputPanelReplication);
    }

    private void replicationOutputCenter() {
        outputPanelReplicationCenter = new JPanel();

        allCustomersModel = new MyTableModel(new ArrayList<>(), new ArrayList<>(Arrays.asList("Zakaznik", "Stav", "Auto")));
        allInQueueModel = new MyTableModel(new ArrayList<>(), new ArrayList<>(Arrays.asList("Zakaznik", "Stav", "Auto")));
        allTechniciansModel = new MyTableModel(new ArrayList<>(), new ArrayList<>(Arrays.asList("Technik", "Stav", "Auto")));
        allMechanicsModel = new MyTableModel(new ArrayList<>(), new ArrayList<>(Arrays.asList("Mechanik", "Stav", "Auto", "L")));
        allCarsWaitingModel = new MyTableModel(new ArrayList<>(), new ArrayList<>(Arrays.asList("P", "Auto")));
        //Table
        JTable allCustomersTable = new JTable(allCustomersModel);
        JScrollPane scrollPane = new JScrollPane(allCustomersTable);
        allCustomersTable.setFont(new Font("", Font.PLAIN, 14));
        allCustomersTable.setRowHeight(20);
        setWidthColumn(allCustomersTable, 2, 165);
        setWidthColumn(allCustomersTable, 1, 300);
        setWidthColumn(allCustomersTable, 0, 185);

        Dimension tableSizeC = scrollPane.getPreferredSize();
        tableSizeC.height = 440;//890
        tableSizeC.width = 370;//390
        scrollPane.setPreferredSize(tableSizeC);
        //

        //Table
        JTable allInQueueTable = new JTable(allInQueueModel);
        JScrollPane scrollPaneQ = new JScrollPane(allInQueueTable);
        allInQueueTable.setFont(new Font("", Font.PLAIN, 14));
        allInQueueTable.setRowHeight(20);
        setWidthColumn(allInQueueTable, 2, 165);
        setWidthColumn(allInQueueTable, 1, 300);
        setWidthColumn(allInQueueTable, 0, 185);

        Dimension tableSizeQ = scrollPane.getPreferredSize();
        tableSizeQ.height = 440;
        tableSizeQ.width = 370;//390
        scrollPaneQ.setPreferredSize(tableSizeQ);
        //

        //Table
        JTable allTechniciansTable = new JTable(allTechniciansModel);
        JScrollPane scrollPaneT = new JScrollPane(allTechniciansTable);
        allTechniciansTable.setFont(new Font("", Font.PLAIN, 14));
        allTechniciansTable.setRowHeight(20);
        setWidthColumn(allTechniciansTable, 2, 315);
        setWidthColumn(allTechniciansTable, 1, 215);
        setWidthColumn(allTechniciansTable, 0, 185);

        Dimension tableSizeT = scrollPaneT.getPreferredSize();
        tableSizeT.height = 890;//890;
        tableSizeT.width = 320;//390
        scrollPaneT.setPreferredSize(tableSizeT);
        //

        //Table
        JTable allMechanicsTable = new JTable(allMechanicsModel);
        JScrollPane scrollPaneM = new JScrollPane(allMechanicsTable);
        allMechanicsTable.setFont(new Font("", Font.PLAIN, 14));
        allMechanicsTable.setRowHeight(20);
        setWidthColumn(allMechanicsTable, 3, 10);
        setWidthColumn(allMechanicsTable, 2, 300);
        setWidthColumn(allMechanicsTable, 1, 150);
        setWidthColumn(allMechanicsTable, 0, 185);

        Dimension tableSizeM = scrollPaneM.getPreferredSize();
        tableSizeM.height = 890;
        tableSizeM.width = 320;
        scrollPaneM.setPreferredSize(tableSizeM);
        //

        //Table
        JTable allCarsWaitingTable = new JTable(allCarsWaitingModel);
        JScrollPane scrollPaneCar = new JScrollPane(allCarsWaitingTable);
        allCarsWaitingTable.setFont(new Font("", Font.PLAIN, 14));
        allCarsWaitingTable.setRowHeight(20);
        setWidthColumn(allCarsWaitingTable, 1, 155);
        setWidthColumn(allCarsWaitingTable, 0, 23);

        Dimension tableSizeCar = scrollPaneCar.getPreferredSize();
        tableSizeCar.height = 890;
        tableSizeCar.width = 170;//200
        scrollPaneCar.setPreferredSize(tableSizeCar);
        //

        Box container = Box.createVerticalBox();
        container.add(scrollPaneQ);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(scrollPane);

        outputPanelReplicationCenter.add(scrollPaneT);
//        outputPanelReplicationCenter.add(scrollPane);
        outputPanelReplicationCenter.add(container);
        outputPanelReplicationCenter.add(scrollPaneCar);
        outputPanelReplicationCenter.add(scrollPaneM);

        int height = 690;
        scrollPaneT.setPreferredSize(new Dimension(scrollPaneT.getPreferredSize().width, height));
        container.setPreferredSize(new Dimension(container.getPreferredSize().width, height));
        scrollPaneCar.setPreferredSize(new Dimension(scrollPaneCar.getPreferredSize().width, height));
        scrollPaneM.setPreferredSize(new Dimension(scrollPaneM.getPreferredSize().width, height));


        centerPanel.add(outputPanelReplicationCenter);
    }

    private void buttons() {
        buttonPanelMain = new JPanel();
        buttonPanelMain.add(startButton);
        buttonPanelMain.add(pauseButton);
        buttonPanelMain.add(resumeButton);
        buttonPanelMain.add(terminateButton);
        buttonPanelWrapper.add(buttonPanelMain);
        frame.add(buttonPanelWrapper, BorderLayout.SOUTH);
    }

     private void simSettings() {
         simulation.onReplicationWillStart(sim->{
             if (((String) cbSimMode.getSelectedItem()).equals("Replikacia")) {
                 simulation.setSimSpeed(1, 1/(double)spinnerModel.getValue());
             } else {
                 simulation.setMaxSimSpeed();
             }
         });

         simulation.onSimulationWillStart(sim->{ ; });

         simulation.onSimulationDidFinish(sim->{
             if (((String) cbSimMode.getSelectedItem()).equals("Simulacia")) {
                 refreshSimulationEnd(simulation);
             } else if (((String) cbSimMode.getSelectedItem()).equals("Replikacia")) {
                 refreshReplication(simulation);
             } else if (((String) cbSimMode.getSelectedItem()).equals("Grafy")) {
                 refreshGraphs(simulation);
             }

         });

        cbSimMode = new JComboBox<>();
        cbSimMode.addItem("Replikacia");
        cbSimMode.addItem("Simulacia");
        cbSimMode.addItem("Grafy");

        JLabel lblReceptionNumber = new JLabel("Pocet technikov:");
        tfReceptionNumber = new JTextField();
        tfReceptionNumber.setColumns(2);
        tfReceptionNumber.setText(String.valueOf(Config.techniciansQuantity));

        JLabel lblMechanicNumber = new JLabel("Pocet mechanikov:");
        tfMechanicNumber = new JTextField();
        tfMechanicNumber.setColumns(2);
        tfMechanicNumber.setText(String.valueOf(Config.mechanicsQuantity));

        JLabel lblMechanicNumber1 = new JLabel("Pocet mechanikov (a+d):");
        tfMechanicNumber1 = new JTextField();
        tfMechanicNumber1.setColumns(2);
        tfMechanicNumber1.setText(String.valueOf(Config.mechanics1Quantity));

        JLabel lblMechanicNumber2 = new JLabel(", (a+d+n):");
        tfMechanicNumber2 = new JTextField();
        tfMechanicNumber2.setColumns(2);
        tfMechanicNumber2.setText(String.valueOf(Config.mechanics2Quantity));

        JLabel lblReplicationNumber = new JLabel("Pocet replikacii:");
        tfReplicationNumber = new JTextField();
        tfReplicationNumber.setColumns(7);
        tfReplicationNumber.setText(String.valueOf(Config.replicationQuantity));

         JLabel lblArrivalCustomerFlowChange = new JLabel("Zmena toku zak. %:");
         tfArrivalCustomerFlowChange = new JTextField();
         tfArrivalCustomerFlowChange.setColumns(3);
         tfArrivalCustomerFlowChange.setText(String.valueOf(Config.arrivalCustomerFlowChange));

        cBModifications = new JCheckBox("Zapnute modifikacie");
        cBModifications.setSelected(true);
        cBModifications.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (cBModifications.isSelected()) {
                    Config.modificationsEnabled = true;
                    tfMechanicNumber1.setVisible(true);
                    tfMechanicNumber2.setVisible(true);
                    lblMechanicNumber1.setVisible(true);
                    lblMechanicNumber2.setVisible(true);
                    tfMechanicNumber.setVisible(false);
                    lblMechanicNumber.setVisible(false);
                } else {
                    Config.modificationsEnabled = false;
                    tfMechanicNumber1.setVisible(false);
                    tfMechanicNumber2.setVisible(false);
                    lblMechanicNumber1.setVisible(false);
                    lblMechanicNumber2.setVisible(false);
                    tfMechanicNumber.setVisible(true);
                    lblMechanicNumber.setVisible(true);
                }
            }
        });

        lblReceptionNumber.setFont(new Font("Calibri", Font.PLAIN, 18));
        tfReceptionNumber.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblMechanicNumber.setFont(new Font("Calibri", Font.PLAIN, 18));
        tfMechanicNumber.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblMechanicNumber1.setFont(new Font("Calibri", Font.PLAIN, 18));
        tfMechanicNumber1.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblMechanicNumber2.setFont(new Font("Calibri", Font.PLAIN, 18));
        tfMechanicNumber2.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblReplicationNumber.setFont(new Font("Calibri", Font.PLAIN, 18));
        tfReplicationNumber.setFont(new Font("Calibri", Font.PLAIN, 18));
         lblArrivalCustomerFlowChange.setFont(new Font("Calibri", Font.PLAIN, 18));
         tfArrivalCustomerFlowChange.setFont(new Font("Calibri", Font.PLAIN, 18));
        cBModifications.setFont(new Font("Calibri", Font.PLAIN, 18));

        settingsPanelBeforeSim.add(lblReceptionNumber);
        settingsPanelBeforeSim.add(tfReceptionNumber);

        settingsPanelBeforeSim.add(lblMechanicNumber);
        settingsPanelBeforeSim.add(tfMechanicNumber);
        tfMechanicNumber.setVisible(false);
        lblMechanicNumber.setVisible(false);

        settingsPanelBeforeSim.add(lblMechanicNumber1);
        settingsPanelBeforeSim.add(tfMechanicNumber1);

        settingsPanelBeforeSim.add(lblMechanicNumber2);
        settingsPanelBeforeSim.add(tfMechanicNumber2);

        settingsPanelBeforeSim.add(lblReplicationNumber);
        settingsPanelBeforeSim.add(tfReplicationNumber);


         settingsPanelBeforeSim.add(cBModifications);
         settingsPanelBeforeSim.add(cbSimMode);

         spinnerModel = new MySpinnerNumberModel(1, 1/8.0, 10000);
         spinner = new JSpinner(spinnerModel);

         settingsPanelBeforeSim.add(lblArrivalCustomerFlowChange);
         settingsPanelBeforeSim.add(tfArrivalCustomerFlowChange);

         JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)spinner.getEditor();
        Font font = editor.getTextField().getFont();
        editor.getTextField().setFont(font.deriveFont(font.getSize() * 1.5f));

        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                simulation.setSimSpeed(1, 1/(double)spinnerModel.getValue());
            }
        });

        JLabel lblSimSpeed = new JLabel("Rychlost simulacie x-krat:");
        lblSimSpeed.setFont(new Font("Calibri", Font.PLAIN, 18));

        settingsPanelSim.add(lblSimSpeed);
        settingsPanelSim.add(spinner);
//---------------------------------------------------------------------------------
        northPanel.add(settingsPanelBeforeSim);
        northPanel.add(settingsPanelSim);
        frame.add(northPanel, BorderLayout.NORTH);
    }

    private void graphsGui() {
        chart = new MyXYLineChart("Priemerny pocet cakajucich v rade na odovzdanie auta na pocte prac. sk. 1", "Pocet pracovnikov skupiny 1", "Priemerny pocet cakajucich", "Priemerny pocet cakajucich");
        ChartPanel chartPanel = new ChartPanel(chart.getChart());

        chart2 = new MyXYLineChart("Priemerny cas straveny zakaznikmi v prevadzke na pocte prac. sk. 2", "Pocet pracovnikov skupiny 2", "Priemerny cas zakaznika v prevadzke (minuty)", "Priemerny cas zakaznika v prevadzke (minuty)");
        ChartPanel chartPanel2 = new ChartPanel(chart2.getChart());

        graphPanel = new JPanel(new GridLayout(1, 2));
        graphPanel.add(chartPanel);
        graphPanel.add(chartPanel2);

        Dimension graphPanelDim = graphPanel.getPreferredSize();
        graphPanelDim.height = 700;//900
        graphPanelDim.width = 1520;//1800
        graphPanel.setPreferredSize(graphPanelDim);

        // Create the buttons
        graphsGuiBtn1 = new JButton("Vykresli");
        graphsGuiBtn2 = new JButton("Vykresli");
        graphsGuiBtnEnd = new JButton("Ukonci");
        graphsGuiBtnEnd.setEnabled(false);
//        buttonPanelGraphsGUI = new JPanel(new FlowLayout(FlowLayout.CENTER, 820,0));
        buttonPanelGraphsGUI = new JPanel(new FlowLayout(FlowLayout.CENTER, 300,0));
        buttonPanelGraphsGUI.add(graphsGuiBtn1);
        buttonPanelGraphsGUI.add(graphsGuiBtnEnd);
        buttonPanelGraphsGUI.add(graphsGuiBtn2);
        buttonPanelGraphsGUI.setVisible(false);
        buttonPanelWrapper.add(buttonPanelGraphsGUI);

        centerPanel.add(graphPanel);
        frame.add(centerPanel, BorderLayout.CENTER);
    }

    private void initView() {
        // Create two buttons using Swing
        startButton = new JButton("Spusti");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                terminateButton.setEnabled(true);
                Thread simulationThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (cbSimMode.getSelectedItem().toString().equals("Simulacia")) {setPanelEnabled(northPanel, false);} else {setPanelEnabled(settingsPanelBeforeSim, false);}
//                        ((MySimulation) simulation).setAvailableReceptionTech(Integer.parseInt(tfReceptionNumber.getText()));
//                        ((MySimulation) simulation).setAvailableMechanics(Integer.parseInt(tfMechanicNumber.getText()));
                        Config.techniciansQuantity = Integer.parseInt(tfReceptionNumber.getText());
                        Config.mechanicsQuantity = Integer.parseInt(tfMechanicNumber.getText());
                        Config.mechanics1Quantity = Integer.parseInt(tfMechanicNumber1.getText());
                        Config.mechanics2Quantity = Integer.parseInt(tfMechanicNumber2.getText());
                        Config.arrivalCustomerFlowChange = Double.parseDouble(tfArrivalCustomerFlowChange.getText());
                        if (Config.modificationsEnabled) {
                            lblMoney.setText("Priem. mzdove naklady (€): "+ SimUtils.calculatePayment());
                        } else {
                            lblMoney.setText("");
                        }
                        ((MySimulation) simulation).setSimMode(cbSimMode.getSelectedItem().toString().equals("Simulacia") ? SimulationMode.TURBO : SimulationMode.NORMAL);
                        simulation.simulate(cbSimMode.getSelectedItem().toString().equals("Simulacia") ? Integer.parseInt(tfReplicationNumber.getText()) : 1 , Config.endTimeSTK);
                    }
                });
                simulationThread.start();
            }
        });

        pauseButton = new JButton("Zastav");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //((MySimulation)simulation).setSimStopped(true);
                ((MySimulation)simulation).pauseSimulation();
                terminateButton.setEnabled(false);
            }
        });

        resumeButton = new JButton("Pokracuj");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((MySimulation)simulation).resumeSimulation();
                terminateButton.setEnabled(true);
            }
        });

        terminateButton = new JButton("Ukonci");
        terminateButton.setEnabled(false);
        terminateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(true);
                terminateButton.setEnabled(false);
                if (cbSimMode.getSelectedItem().toString().equals("Simulacia")) {setPanelEnabled(northPanel, true);} else {setPanelEnabled(settingsPanelBeforeSim, true);}
//                simulation.setRunning(false);
                simulation.stopSimulation();
            }
        });

//        cBsimulationMode.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                if (cBsimulationMode.isSelected()) {
//                    outputPanelReplication.setVisible(false);
//                    outputPanelSimulation.setVisible(true);
//                    spinner.setEnabled(false);
//                } else {
//                    outputPanelReplication.setVisible(true);
//                    outputPanelSimulation.setVisible(false);
//                    spinner.setEnabled(true);
//                }
//            }
//        });

        cbSimMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) cbSimMode.getSelectedItem();
                switch(selected) {
                    case "Replikacia":
                        outputPanelReplication.setVisible(true);
                        outputPanelReplicationCenter.setVisible(true);
                        buttonPanelMain.setVisible(true);
                        outputPanelSimulation.setVisible(false);
                        graphPanel.setVisible(false);
                        buttonPanelGraphsGUI.setVisible(false);
                        break;
                    case "Simulacia":
                        outputPanelReplication.setVisible(false);
                        outputPanelReplicationCenter.setVisible(false);
                        outputPanelSimulation.setVisible(true);
                        buttonPanelMain.setVisible(true);
                        graphPanel.setVisible(false);
                        buttonPanelGraphsGUI.setVisible(false);
                        break;
                    case "Grafy":
                        outputPanelReplication.setVisible(false);
                        outputPanelReplicationCenter.setVisible(false);
                        outputPanelSimulation.setVisible(false);
                        buttonPanelMain.setVisible(false);
                        graphPanel.setVisible(true);
                        buttonPanelGraphsGUI.setVisible(true);
                        break;
                    default:
                        break;
                }
            }
        });

        graphsGuiBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphsGuiBtn2.setEnabled(false);
                graphsGuiBtn1.setEnabled(false);
                graphsGuiBtnEnd.setEnabled(true);
                chart.clearSeries();
                Thread simulationThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        graph1Active = true;
                        setPanelEnabled(northPanel, false);

                        for (int i = 1; i < 16; i++) {
//                            ((MySimulation) simulation).setAvailableReceptionTech(i+1);
//                            ((MySimulation) simulation).setAvailableMechanics(Integer.parseInt(tfMechanicNumber.getText()));
                            Config.techniciansQuantity = i+1;
                            Config.mechanicsQuantity = Integer.parseInt(tfMechanicNumber.getText());
                            Config.arrivalCustomerFlowChange = Double.parseDouble(tfArrivalCustomerFlowChange.getText());

                            Config.mechanics1Quantity = Integer.parseInt(tfMechanicNumber1.getText());
                            Config.mechanics2Quantity = Integer.parseInt(tfMechanicNumber2.getText());

                            ((MySimulation) simulation).setSimMode(SimulationMode.TURBO);
                            simulation.simulate(Integer.parseInt(tfReplicationNumber.getText()), Config.endTimeSTK);
                            if (endGraph) {break;}
                        }
                        graphsGuiBtn2.setEnabled(true);
                        graphsGuiBtn1.setEnabled(true);
                        graphsGuiBtnEnd.setEnabled(false);
                        endGraph = false;
                        setPanelEnabled(northPanel, true);
                    }
                });
                simulationThread.start();
            }
        });

        graphsGuiBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chart2.clearSeries();
                graphsGuiBtn2.setEnabled(false);
                graphsGuiBtn1.setEnabled(false);
                graphsGuiBtnEnd.setEnabled(true);
                Thread simulationThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        graph1Active = false;
                        setPanelEnabled(northPanel, false);
                        for (int i = 10; i < 26; i++) {
//                            ((MySimulation) simulation).setAvailableReceptionTech(Integer.parseInt(tfReceptionNumber.getText()));
//                            ((MySimulation) simulation).setAvailableMechanics(i);
                            Config.techniciansQuantity = Integer.parseInt(tfReceptionNumber.getText());
                            Config.mechanicsQuantity = i;
                            int workers[] = calculateWorkers(i, Config.idealRatioOfMechanics);
                            Config.mechanics1Quantity = workers[0];
                            Config.mechanics2Quantity = workers[1];
                            Config.arrivalCustomerFlowChange = Double.parseDouble(tfArrivalCustomerFlowChange.getText());
                            ((MySimulation) simulation).setSimMode(SimulationMode.TURBO);
                            simulation.simulate(Integer.parseInt(tfReplicationNumber.getText()), Config.endTimeSTK);
                            if (endGraph) {break;}
                        }
                        graphsGuiBtn2.setEnabled(true);
                        graphsGuiBtn1.setEnabled(true);
                        graphsGuiBtnEnd.setEnabled(false);
                        endGraph = false;
                        setPanelEnabled(northPanel, true);
                    }
                });
                simulationThread.start();
            }
        });

        graphsGuiBtnEnd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                simulation.setRunning(false);
                simulation.stopSimulation();
                endGraph = true;
            }
        });

        buttonHover(graphsGuiBtn2, tfReceptionNumber);

        buttonHover(graphsGuiBtn1, tfMechanicNumber);
        buttonHover(graphsGuiBtn1, tfMechanicNumber1);
        buttonHover(graphsGuiBtn1, tfMechanicNumber2);
    }



    public static int[] calculateWorkers(int numberOfWorkers, double ratioOfWorkers) {
        int[] result = new int[2];

        result[0] = (int) Math.round(ratioOfWorkers * numberOfWorkers);
        result[1] = (int) Math.round((1-ratioOfWorkers) * numberOfWorkers);

        if (result[0] + result[1] != numberOfWorkers) {
            int missingWorkers = numberOfWorkers - result[0] + result[1];
            result[1] += missingWorkers;
        }

        return result;
    }

    private void buttonHover(JButton graphsGuiBtn2, JTextField tfReceptionNumber) {
        graphsGuiBtn2.addMouseListener(new MouseListener() {
            Border defaultBorder = tfReplicationNumber.getBorder();
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {
                Border border = BorderFactory.createLineBorder(Color.RED, 3);
                tfReplicationNumber.setBorder(border);
                tfReceptionNumber.setBorder(border);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                tfReplicationNumber.setBorder(defaultBorder);
                tfReceptionNumber.setBorder(defaultBorder);
            }
        });
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {

    }

    @Override
    public void refresh(Simulation sim) {
        String selected = (String) cbSimMode.getSelectedItem();
        switch(selected) {
            case "Replikacia":
                refreshReplication(sim);
                break;
            case "Simulacia":
                refreshSimulation(sim);
                break;
            case "Grafy":
                refreshGraphs(sim);
                break;
            default:
                break;
        }
    }

    private void refreshGraphs(Simulation sim) {
        MySimulation MySimulation = (MySimulation) sim;

        if (!MySimulation.isRunning() && graph1Active) {
            chart.addPoint(chart.getItemCount()+1, MySimulation.getAvgSimCustomerCountQueueCar().mean());
        }

        if (!MySimulation.isRunning() && !graph1Active) {
            chart2.addPoint(chart2.getItemCount()+10, roundNumber(MySimulation.getAvgSimCustomerTimeInSystem().mean()/60, 2));
        }
    }
    private void refreshSimulation(Simulation sim) {
        MySimulation MySimulation = (MySimulation) sim;

        if (((MySimulation.replicationCount()) % (Integer.valueOf(tfReplicationNumber.getText())/10)) == 0) {
            lblCurrentReplicationValue.setText(""+(MySimulation.replicationCount()));
        }
    }
    private void refreshSimulationEnd(Simulation simulation) {
        MySimulation MySimulation = (MySimulation) simulation;
        lblCurrentReplicationValue.setText(MySimulation.replicationCount()+"");

        statsModelSim.setValueAt((MySimulation.getAvgSimCustomerTimeInSystem().mean()/60)  + " min", 0, 1);
        statsModelSim.setValueAt((MySimulation.getAvgSimCustomerTimeInQueueCar().mean()/60)  + " min", 1, 1);
        statsModelSim.setValueAt((MySimulation.getAvgSimCustomerTimeInPayQueue().mean()/60)  + " min", 2, 1);//todo new
        statsModelSim.setValueAt((MySimulation.getAvgSimCustomerTimeWaitingCheck().mean()/60)  + " min", 3, 1);//todo new

        statsModelSim.setValueAt((MySimulation.getAvgSimCustomerCountQueueCar().mean()), 4, 1);
        statsModelSim.setValueAt((MySimulation.getAvgSimCustomerCountSystem().mean()), 5, 1);
        statsModelSim.setValueAt((MySimulation.getAvgSimTechnicianFreeCount().mean()), 6, 1);
        statsModelSim.setValueAt((MySimulation.getAvgSimMechanicFreeCount().mean()), 7, 1);
        statsModelSim.setValueAt((MySimulation.getAvgSimCarInSystemCount().mean()), 8, 1);
        statsModelSim.setValueAt((MySimulation.getAvgSimCustomersInSystemCount().mean()), 9, 1);

        //CI
        double [] valuesTimeInSystem = MySimulation.getAvgSimCustomerTimeInSystem().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesTimeInSystem[0]/60, 5)  + "; " + roundNumber(valuesTimeInSystem[1]/60, 5) + ") min", 0, 2);
        double [] valuesTimeInQueueCar = MySimulation.getAvgSimCustomerTimeInQueueCar().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesTimeInQueueCar[0]/60, 5)  + "; " + roundNumber(valuesTimeInQueueCar[1]/60, 5) + ") min", 1, 2);
        double [] valuesTimeInQueuePay = MySimulation.getAvgSimCustomerTimeInPayQueue().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesTimeInQueuePay[0]/60, 5)  + "; " + roundNumber(valuesTimeInQueuePay[1]/60, 5) + ") min", 2, 2);
        double [] valuesTimeWaitingCheck = MySimulation.getAvgSimCustomerTimeWaitingCheck().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesTimeWaitingCheck[0]/60, 5)  + "; " + roundNumber(valuesTimeWaitingCheck[1]/60, 5) + ") min", 3, 2);

        double [] valuesCountInQueueCar = MySimulation.getAvgSimCustomerCountQueueCar().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesCountInQueueCar[0], 5)  + "; " + roundNumber(valuesCountInQueueCar[1], 5) + ")", 4, 2);
        double [] valuesCountInSystem = MySimulation.getAvgSimCustomerCountSystem().confidenceInterval_95();
        statsModelSim.setValueAt("(" + roundNumber(valuesCountInSystem[0], 5)  + "; " + roundNumber(valuesCountInSystem[1], 5) + ")    95%-ný", 5, 2);
        double [] valuesTechnicianFreeCount = MySimulation.getAvgSimTechnicianFreeCount().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesTechnicianFreeCount[0], 5)  + "; " + roundNumber(valuesTechnicianFreeCount[1], 5) + ")", 6, 2);
        double [] valuesMechanicFreeCount = MySimulation.getAvgSimMechanicFreeCount().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesMechanicFreeCount[0], 5)  + "; " + roundNumber(valuesMechanicFreeCount[1], 5) + ")", 7, 2);
        double [] valuesCarInSystemCount = MySimulation.getAvgSimCarInSystemCount().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesCarInSystemCount[0], 5)  + "; " + roundNumber(valuesCarInSystemCount[1], 5) + ")", 8, 2);
        double [] valuesCustomerInSystemCount = MySimulation.getAvgSimCustomersInSystemCount().confidenceInterval_90();
        statsModelSim.setValueAt("(" + roundNumber(valuesCustomerInSystemCount[0], 5)  + "; " + roundNumber(valuesCustomerInSystemCount[1], 5) + ")", 9, 2);
        //
        statsModelSim.fireTableDataChanged();
    }
    private void refreshReplication(Simulation sim) {
        MySimulation MySimulation = (MySimulation) sim;
        lblSimTimeValue.setText(simTimeToHHMMSS(MySimulation.currentTime()+Config.startTimeSTK+32400));

        customerModel.setValueAt(MySimulation.getTotalCustomers(), 0, 1);
        customerModel.setValueAt(MySimulation.getWaitingCustomersQuantity(), 1, 1);
        customerModel.setValueAt(MySimulation.getWaitingNewCustomersQuantity(), 2, 1);
        customerModel.setValueAt(MySimulation.getWaitingPayCustomersQuantity(), 3, 1);
        customerModel.setValueAt(MySimulation.getPayingCustomersQuantity(), 4, 1);
        customerModel.setValueAt(MySimulation.getCarWaitingCustomersQuantity(), 5, 1);
        customerModel.setValueAt(MySimulation.getTotalLeftCustomers(), 6, 1);

        customerModel.fireTableDataChanged();

        employeesModel.setValueAt(Integer.parseInt(tfReceptionNumber.getText()) - MySimulation.getAvailableTechnicians(), 0, 1);
        employeesModel.setValueAt(MySimulation.getAvailableTechnicians(), 1, 1);
//        employeesModel.setValueAt(Integer.parseInt(tfMechanicNumber.getText()) - MySimulation.getAvailableMechanics(), 2, 1);
        employeesModel.setValueAt(Config.mechanicsQuantity - MySimulation.getAvailableMechanics(), 2, 1);
        employeesModel.setValueAt(MySimulation.getAvailableMechanics(), 3, 1);

        employeesModel.fireTableDataChanged();

        carsModel.setValueAt(MySimulation.getCarsInSTKQuantity(), 0, 1);
        carsModel.setValueAt(MySimulation.getCarsChecking(), 1, 1);
        carsModel.setValueAt(MySimulation.getWorkShopParkingCount(), 2, 1);
        carsModel.setValueAt(MySimulation.getCarsQuantityTransporting(), 3, 1);

        carsModel.fireTableDataChanged();

        statsModel.setValueAt(roundNumber(MySimulation.getAvgCustomerTimeInSystem().mean()/60, 2)  + "m", 0, 1);
        statsModel.setValueAt(roundNumber(MySimulation.getAvgCustomerTimeInQueueCar().mean()/60, 2)  + "m", 1, 1);

        statsModel.setValueAt(roundNumber(MySimulation.getAvgCustomerTimeInPayQueue().mean()/60, 2)  + "m", 2, 1);
        statsModel.setValueAt(roundNumber(MySimulation.getAvgCustomerTimeWaitingCheck().mean()/60, 2)  + "m", 3, 1);

        statsModel.setValueAt(roundNumber(MySimulation.getAvgCustomerCountQueueCar().getWeightedMean(), 2), 4, 1);
        statsModel.setValueAt(roundNumber(MySimulation.getAvgCustomerCountSystem().getWeightedMean(), 2), 5, 1);
        statsModel.setValueAt(roundNumber(MySimulation.getAvgTechnicianFreeCount().mean(), 2), 6, 1);
        statsModel.setValueAt(roundNumber(MySimulation.getAvgMechanicFreeCount().mean(), 2), 7, 1);
        statsModel.setValueAt(roundNumber(MySimulation.getCustomersInSTKAtClosure(), 2), 8, 1);

        statsModel.fireTableDataChanged();

        allCustomersModel.setData(MySimulation.getAllCustomers());
        allCustomersModel.fireTableDataChanged();

        allInQueueModel.setData(MySimulation.getAllInQueue());
        allInQueueModel.fireTableDataChanged();

        allTechniciansModel.setData(MySimulation.getAllTechnicians());
        allTechniciansModel.fireTableDataChanged();

        allMechanicsModel.setData(MySimulation.getAllMechanics());
        allMechanicsModel.fireTableDataChanged();

        allCarsWaitingModel.setData(MySimulation.getAllCarsWaiting());
        allCarsWaitingModel.fireTableDataChanged();

    }

    private String simTimeToHHMMSS(double simTime) {
        int hours = (int)simTime / 3600;
        int minutes = (int)(simTime % 3600) / 60;
        int seconds = (int)simTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component instanceof JPanel) {
                setPanelEnabled((JPanel) component, isEnabled);
            }
            component.setEnabled(isEnabled);
        }
    }

    private void setWidthColumn(JTable table, int i, int value) {
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(i);
        column.setPreferredWidth(value);
        column.setResizable(false);
    }
    private void colorRows(JTable table, int[] rows, Color[] bgColors, Color[] textColors) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                for (int i = 0; i < rows.length; i++) {
                    if (row == rows[i]) {
                        cell.setBackground(bgColors[i]);
                        cell.setForeground(textColors[i]);
                        break;
                    }
                }
                return cell;
            }
        });
    }
    private double roundNumber(double number, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("decimalPlaces must be non-negative");
        }
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(number * factor) / factor;
    }
}

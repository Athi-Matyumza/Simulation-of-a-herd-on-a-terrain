import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;

public class GUIStyler_handler {
    // right panel objects
    private static JSlider slSeperate;
    private static JSlider slCohesion;
    private static JSlider slAlign;
    private static JSlider slvRange;
    private static JComboBox<String> cmbSpecie;
    private static JComboBox<String> cmbBoundary;
    private static JSpinner spnSpawn;

    // left panel objects
    private static JCheckBox cbxTrail;
    private static JRadioButton water;
    private static JRadioButton trees;
    private static Timer simRuntime;
    private static JFormattedTextField ftmRuntime;
    private static JCheckBox drawingCheck;

    // bottom panel objects
    private static JButton btnSave;
    private static JButton btnBackward;
    private static JButton btnplay;
    private static Icon[] pause_or_play = new ImageIcon[2]; // Acts as a Queue that juggles which icon must be displayed
    // between pause or play
    private static int plpsStatus = 1; // keeps track of which button between pause or play was pressed
    private static JButton btnForward;
    private static JButton btnClear;
    private static BoidsSimulation simulation = new BoidsSimulation();

    private static int seconds = 0;

    private static final ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnClear) {
                // rhs components
                slSeperate.setValue(slSeperate.getMinimum());
                slCohesion.setValue(slCohesion.getMinimum());
                slAlign.setValue(slAlign.getMinimum());
                slvRange.setValue(slvRange.getMinimum());
                cmbSpecie.getModel().setSelectedItem(cmbSpecie.getItemAt(0));
                cmbBoundary.getModel().setSelectedItem(cmbBoundary.getItemAt(0));
                spnSpawn.setValue(0);

                // bottom components
                if (plpsStatus == 0) {
                    btnplay.doClick();
                    plpsStatus = 1;
                }

                seconds = 0;
                simulation.Clear();
                cmbSpecie.setEnabled(true);
                trees.setSelected(true);
                ftmRuntime.setEnabled(true);
                ftmRuntime.setText("##mins ##s");
                drawingCheck.setSelected(false);
                cbxTrail.setSelected(false);
                btnplay.setEnabled(true);
            }
            else if (e.getSource() == btnBackward) {
                System.out.println("Hello backward button");
            }
            else if (e.getSource() == btnplay) {
                Icon icon = pause_or_play[0];
                pause_or_play[0] = pause_or_play[1];
                pause_or_play[1] = icon;
                btnplay.setIcon(pause_or_play[0]);
                plpsStatus = (plpsStatus + 1) % 2;
                simulation.pause_playSim(plpsStatus);
            }
            else if (e.getSource() == btnForward) {
                System.out.println("Hello forward button");
            }
            else if (e.getSource() == btnSave) {
                System.out.println("Hello save button");
            }
            else if (e.getSource() == cmbSpecie) {
                if (simRuntime != null)
                    cmbSpecie.setEnabled(false);

                if (cmbSpecie.getSelectedItem().equals("Elephant"))
                    simulation.setAnimalColour(new Color(234, 6, 6));
                else if (cmbSpecie.getSelectedItem().equals("Cow")) {
                    simulation.setAnimalColour(new Color(252, 154, 34));
                }else if (cmbSpecie.getSelectedItem().equals("Sheep")) {
                    simulation.setAnimalColour(new Color(170, 2, 243));

                    System.out.println("Selected item: " + cmbSpecie.getSelectedItem());
                }
            }
            else if (e.getSource() == cmbBoundary) {
                    if ((cmbBoundary.getSelectedItem().equals("wrap"))) {
                        simulation.setDRAW_TRAIL(false);
                        cbxTrail.setEnabled(false);
                    } else if ((cbxTrail != null) && (!cbxTrail.isEnabled())) {
                        cbxTrail.setSelected(false);
                        cbxTrail.setEnabled(true);
                    }

                    simulation.setBoundaryOpt(cmbBoundary.getSelectedItem().toString());
                }
            else if (e.getSource() == cbxTrail) {
                    if (!(cmbBoundary.getSelectedItem().equals("wrap")))
                        simulation.setDRAW_TRAIL(cbxTrail.isSelected());
                    System.out.println(cbxTrail.isSelected());
                }
            else if (e.getSource() == trees) {
                    simulation.setSelectedObs("trees");
                }
            else if (e.getSource() == water) {
                    simulation.setSelectedObs("water");
                }
            else if (e.getSource() == ftmRuntime) {
                    ftmRuntime.setEnabled(false);
                    if (simRuntime == null)
                        startTimer(ftmRuntime.getText());
                }
            else if (e.getSource() == simRuntime) {
                    seconds--;
                    if (seconds > 0)
                        System.out.printf("Lapsed time: %d\n", seconds);
                    else {
                        simRuntime.stop();
                        btnplay.doClick();
                        ftmRuntime.setEnabled(true);
                        simRuntime = null;
                        btnplay.setEnabled(false);
                        System.out.println("Time's up!!!");
                    }

                }
        }
    };

    private static final ChangeListener changelistener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == spnSpawn) {
                simulation.setNumBoids((int) spnSpawn.getValue());
            }
            else if (e.getSource() == slSeperate) {
                simulation.setSeparation(slSeperate.getValue() / 1000.0);
            }
            else if (e.getSource() == slCohesion) {
                simulation.setCohesion(slCohesion.getValue() / 100.0);
            }
            else if (e.getSource() == slAlign) {
                simulation.setAlignment(slAlign.getValue() / 100.0);
            }
            else if (e.getSource() == slvRange) {
                simulation.setVisualRange(slvRange.getValue());
            }
        }
    };

    static BoidsSimulation getSimulation() {
        return simulation;
    }

    static JPanel rightPanel(Dimension screen) {
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(new Color(73, 125, 138));
        rightPanel.setPreferredSize(new Dimension(300, screen.height - 100));

        // Create and add components to rightPanel
        JLabel flockCtrl = new JLabel("Flock Controls");
        JLabel lblSeperation = new JLabel("Seperation Force");
        JLabel lblCohesion = new JLabel("Cohesion Force");
        JLabel lblAlign = new JLabel("Alignment Force");
        JLabel lblvRange = new JLabel("Visual Range");
        JLabel lblspecie = new JLabel("Select specie");
        JLabel lblBoundary = new JLabel("Boundary handling");
        JLabel lblSpawn = new JLabel("Animals Spawn Count");

        flockCtrl.setFont(new Font("Arial", Font.BOLD, 30));
        lblSeperation.setFont(new Font("Arial", Font.BOLD, 17));
        lblCohesion.setFont(new Font("Arial", Font.BOLD, 17));
        lblAlign.setFont(new Font("Arial", Font.BOLD, 17));
        lblspecie.setFont(new Font("Arial", Font.BOLD, 17));
        lblBoundary.setFont(new Font("Arial", Font.BOLD, 17));
        lblSpawn.setFont(new Font("Arial", Font.BOLD, 17));
        lblvRange.setFont(new Font("Arial", Font.BOLD, 17));
        flockCtrl.setForeground(Color.WHITE);
        lblSeperation.setForeground(Color.WHITE);
        lblCohesion.setForeground(Color.WHITE);
        lblAlign.setForeground(Color.WHITE);
        lblspecie.setForeground(Color.WHITE);
        lblBoundary.setForeground(Color.WHITE);
        lblSpawn.setForeground(Color.WHITE);
        lblvRange.setForeground(Color.WHITE);

        Object[] temp = {JSlider.HORIZONTAL, 0,50,0};
        slCohesion = new JSlider(JSlider.HORIZONTAL, (int)temp[1],(int)temp[2],(int)temp[3]);
        slCohesion.addChangeListener(changelistener);
        slSeperate = new JSlider(JSlider.HORIZONTAL, (int)temp[1],(int)temp[2],(int)temp[3]);
        slSeperate.addChangeListener(changelistener);
        slAlign = new JSlider(JSlider.HORIZONTAL, (int)temp[1],(int)temp[2],(int)temp[3]);
        slAlign.addChangeListener(changelistener);
        slvRange = new JSlider(JSlider.HORIZONTAL, 0,100,0);
        slvRange.addChangeListener(changelistener);
        cmbSpecie = new JComboBox<>();
        cmbSpecie.addActionListener(actionListener);
        cmbBoundary = new JComboBox<>();
        cmbBoundary.addActionListener(actionListener);
        spnSpawn = new JSpinner(new SpinnerNumberModel(0,0,100,1));
        spnSpawn.addChangeListener(changelistener);

        flockCtrl.setBounds(30, 10, 300, 50);
        lblSeperation.setBounds(25, flockCtrl.getY() + 60, 200, 50);
        slSeperate.setBounds(lblSeperation.getX(), lblSeperation.getY() + 40, 250, 25);
        lblCohesion.setBounds(lblSeperation.getX(), slSeperate.getY() + 30, 200, 50);
        slCohesion.setBounds(lblSeperation.getX(), lblCohesion.getY() + 40, 250, 25);
        lblAlign.setBounds(lblSeperation.getX(), slCohesion.getY() + 30, 200, 50);
        slAlign.setBounds(lblSeperation.getX(), lblAlign.getY() + 40, 250, 25);
        lblvRange.setBounds(lblSeperation.getX(), slAlign.getY() + 30, 250, 30);
        slvRange.setBounds(lblSeperation.getX(), lblvRange.getY() + 30, 250, 30);
        lblspecie.setBounds(lblSeperation.getX(), slvRange.getY() + 40, 250, 30);
        cmbSpecie.setBounds(lblSeperation.getX(), lblspecie.getY() + 30, 250, 30);
        lblBoundary.setBounds(lblSeperation.getX(), cmbSpecie.getY() + 90, 250, 30);
        cmbBoundary.setBounds(lblSeperation.getX(), lblBoundary.getY() + 30, 250, 30);
        lblSpawn.setBounds(lblSeperation.getX(), cmbBoundary.getY() + 85, 250, 30);
        spnSpawn.setBounds(lblSeperation.getX(), lblSpawn.getY() + 30, 250, 30);
        cmbSpecie.addItem("Elephant");
        cmbSpecie.addItem("Cow");
        cmbSpecie.addItem("Sheep");
        cmbBoundary.addItem("bounce");
        cmbBoundary.addItem("wrap");
        cmbBoundary.addItem("reflect");

        rightPanel.add(flockCtrl);
        rightPanel.add(lblSeperation);
        rightPanel.add(slSeperate);
        rightPanel.add(lblCohesion);
        rightPanel.add(slCohesion);
        rightPanel.add(lblAlign);
        rightPanel.add(slAlign);
        rightPanel.add(lblvRange);
        rightPanel.add(slvRange);
        rightPanel.add(lblspecie);
        rightPanel.add(cmbSpecie);
        rightPanel.add(lblBoundary);
        rightPanel.add(cmbBoundary);
        rightPanel.add(lblSpawn);
        rightPanel.add(spnSpawn);

        return rightPanel;
    }

    static JPanel leftPanel(Dimension screen) throws ParseException {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(73, 125, 138));
        leftPanel.setPreferredSize(new Dimension(300, screen.height - 100));
        leftPanel.setLayout(null);

        // Create and add components to leftPanel
        JLabel lblMap = new JLabel("Map Controls");
        JLabel lblelvData = new JLabel("Select Elevation Data");
        JButton btnMapLoader = new JButton("Load Map");
        btnMapLoader.addActionListener(e -> {

            final JFileChooser fc = new JFileChooser();
            //In response to a button click:
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                simulation.loadMapFile(file.getAbsolutePath());
            }
        });

        drawingCheck = new JCheckBox("Disable map dragging");

        drawingCheck.addActionListener(e -> simulation.drawing =  ((JCheckBox)e.getSource()).isSelected());

        JLabel lblChooseObs = new JLabel("Choose obstacle to draw:");
        trees = new JRadioButton("Trees");
        trees.setSelected(true);
        trees.addActionListener(actionListener);
        water = new JRadioButton("Water");
        water.addActionListener(actionListener);
        cbxTrail = new JCheckBox("Draw animal footprints", false);
        cbxTrail.addActionListener(actionListener);
        ButtonGroup obstaclesType = new ButtonGroup();
        JLabel lblSimCtrl = new JLabel("Sim Controls");
        JLabel lblSimRuntime = new JLabel("Enter simulation run time");

        MaskFormatter mask = new MaskFormatter("##min ##s");//the # is for numeric values
        mask.setPlaceholderCharacter('0');

        ftmRuntime = new JFormattedTextField(mask);
        ftmRuntime.addActionListener(actionListener);

        lblMap.setBounds(30, 25, 300, 50);
        lblMap.setFont(new Font("Arial", Font.BOLD, 30));
        lblMap.setForeground(Color.WHITE);
        lblelvData.setBounds(25, lblMap.getY() + 50, 250, 50);
        lblelvData.setFont(new Font("Arial", Font.BOLD, 17));
        lblelvData.setForeground(Color.WHITE);
        btnMapLoader.setBounds(lblelvData.getX(), lblelvData.getY() + 40, 250, 30);

        drawingCheck.setBounds(lblelvData.getX(), btnMapLoader.getY() + 35, 250, 50);
        drawingCheck.setFont(new Font("Arial", Font.BOLD, 17));
        drawingCheck.setForeground(Color.WHITE);
        lblChooseObs.setBounds(lblelvData.getX(), drawingCheck.getY() + 50, 250, 50);
        lblChooseObs.setFont(new Font("Arial", Font.BOLD, 17));
        lblChooseObs.setForeground(Color.WHITE);

        trees.setBounds(85, lblChooseObs.getY() + 40, 200, 50);
        trees.setBackground(new Color(34, 110, 2, 110));
        trees.setFont(new Font("Arial", Font.BOLD, 17));
        trees.setForeground(Color.WHITE);

        water.setBounds(trees.getX(), trees.getY() + 40, 200, 50);
        water.setBackground(new Color(34, 110, 2, 110));
        water.setFont(new Font("Arial", Font.BOLD, 17));
        water.setForeground(Color.WHITE);

        cbxTrail.setBounds(lblChooseObs.getX(), water.getY() + 60, 250,50);
        cbxTrail.setFont(new Font("Arial", Font.BOLD, 17));
        cbxTrail.setForeground(Color.WHITE);

        lblSimCtrl.setBounds(30, cbxTrail.getY() + 120, 300, 50);
        lblSimCtrl.setFont(new Font("Arial", Font.BOLD, 30));
        lblSimCtrl.setForeground(Color.WHITE);

        lblSimRuntime.setBounds(25, lblSimCtrl.getY() + 50, 250, 50);
        lblSimRuntime.setFont(new Font("Arial", Font.BOLD, 17));
        lblSimRuntime.setForeground(Color.WHITE);

        ftmRuntime.setBounds(25, lblSimRuntime.getY() + 40, 250, 30);

        obstaclesType.add(trees);
        obstaclesType.add(water);

        leftPanel.add(lblMap);
        leftPanel.add(lblelvData);
        leftPanel.add(btnMapLoader);
        leftPanel.add(drawingCheck);
        leftPanel.add(lblChooseObs);
        leftPanel.add(trees);
        leftPanel.add(water);
        leftPanel.add(cbxTrail);

        leftPanel.add(lblSimCtrl);
        leftPanel.add(lblSimRuntime);
        leftPanel.add(ftmRuntime);

        return leftPanel;
    }

    static JPanel bottomPanel(Dimension screen) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setBackground(new Color(73, 125, 138));
        bottomPanel.setPreferredSize(new Dimension(screen.width, 100));

        // Create and add components to bottomPanel
        btnClear = new JButton("Clear");
        btnClear.addActionListener(actionListener);
        Image scaledIcon = (new ImageIcon("./icons/backward.png")).getImage();
        scaledIcon = scaledIcon.getScaledInstance(60, 40, Image.SCALE_SMOOTH);
        Icon backIcon = new ImageIcon(scaledIcon);

        scaledIcon = (new ImageIcon("./icons/play.png")).getImage();
        scaledIcon = scaledIcon.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        Icon playIcon = new ImageIcon(scaledIcon);
        pause_or_play[0] = playIcon;

        scaledIcon = (new ImageIcon("./icons/pause.png")).getImage();
        scaledIcon = scaledIcon.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        playIcon = new ImageIcon(scaledIcon);

        pause_or_play[1] = playIcon;

        scaledIcon = (new ImageIcon("./icons/forward.png")).getImage();
        scaledIcon = scaledIcon.getScaledInstance(60, 40, Image.SCALE_SMOOTH);
        Icon forwardIcon = new ImageIcon(scaledIcon);

        btnBackward = new JButton(backIcon);
        btnBackward.addActionListener(actionListener);
        btnplay = new JButton(pause_or_play[0]);
        btnplay.addActionListener(actionListener);
        btnForward = new JButton(forwardIcon);
        btnForward.addActionListener(actionListener);
        btnSave = new JButton("Save as");
        btnSave.addActionListener(actionListener);
        btnClear.setBounds(300, 15, 150, 40);
        btnSave.setBounds(btnClear.getX() + 1150, btnClear.getY(), 150, 40);
        btnBackward.setBounds(btnClear.getX() + 400, 10, 150, 50);
        btnplay.setBounds((btnBackward.getWidth() + btnBackward.getX()) + 50, 1, 65, 70);
        btnForward.setBounds((btnplay.getWidth() + btnplay.getX()) + 50, btnBackward.getY(), 150, 50);

        bottomPanel.add(btnClear);
        bottomPanel.add(btnBackward);
        bottomPanel.add(btnplay);
        bottomPanel.add(btnForward);
        bottomPanel.add(btnSave);

        return bottomPanel;
    }

    static void startTimer(String time) {
        time = time.replace("min", "").replace("s", "").trim();
        seconds = Integer.parseInt(time.split(" ")[0]) * 60 + Integer.parseInt(time.split(" ")[1]);
        simRuntime = new Timer(1000, actionListener);
        simRuntime.start();
    }

}
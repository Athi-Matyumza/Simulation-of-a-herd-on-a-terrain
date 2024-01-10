import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import java.util.ArrayList;

public class BoidsSimulation extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {
    // Define canvas dimensions and simulation parameters
    // Modify canvas width here
    // Modify canvas height here

    private int numBoids = 0; // Modify number of boids here
    protected ArrayList<Boid> boids = new ArrayList<>();
    protected ArrayList<ArrayList<Point>> trees = new ArrayList<>(); // Stores obstacle shapes
    protected ArrayList<ArrayList<Point>> water = new ArrayList<>(); // Stores obstacle shapes

    private String selectedObs = "trees";

    private String boundaryOpt = "bounce"; // Boundary behavior: "bounce," "wrap," or "reflect"
    private Behaviour behaviour; // Behaviour object responsible for boid behaviour
    // private Simulation simulation; // Simulation object responsible for drawing obstacles and boids
    private ArrayList<Point> currentObstacle = new ArrayList<>(); // Track current obstacle points
    private final Timer timer;
    public boolean drawing = false;

    // Map data
    public HeightMaps heightMaps;
    public int xOffset = 0;
    public int yOffset = 0;
    public float zoom = 1f;
    public float maxZoom = 1f;
    private int prevMouseX, prevMouseY;
    private final BoidPanel boidPanel;

    private int frameCount;
    private BufferedWriter AnimalPosWriter;
    private BufferedWriter obstacleWriter;

    public BoidsSimulation() {
        try {
            AnimalPosWriter = new BufferedWriter(new FileWriter(getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            obstacleWriter = new BufferedWriter(new FileWriter(getObstacleFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setLayout( new OverlayLayout(this) );
        this.setBackground(Color.GRAY);

        heightMaps = new HeightMaps();

        // Initialize simulation and event listeners
        boidPanel = new BoidPanel(this, heightMaps, behaviour);
        this.add(boidPanel, BorderLayout.CENTER);

        MapPanel mapPanel = new MapPanel(heightMaps, this);
        this.add(mapPanel, BorderLayout.CENTER);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        timer = new Timer(90, this); // Controls frame rate for animation
        timer.start(); // Start the animation loop
    }
    // Initialize boids at a specific position
    public boolean loadMapFile(String filename){
        if(!heightMaps.loaded){
            heightMaps.LoadFile(filename);
            xOffset = 0;
            yOffset = 0;
            // repaint();
            // return(true);
            // Modify visual range for boids
            int visualRange = 50;
            // Obstacle avoidance distance
            int obstacleAvoidanceDistance = 5;
            behaviour = new Behaviour(boids, trees, heightMaps.width, heightMaps.height, visualRange, obstacleAvoidanceDistance);
        }
        repaint();
        return(false);
    }

    private void initBoids(int x, int y) {
        for (int i = 0; i < numBoids; i++) {
            Boid boid = new Boid(x, y,
                    Math.random() * 10 - 5, Math.random() * 10 - 5);
            boids.add(boid);
        }
    }
    // Update canvas dimensions (Could be used for zooming feature)

    // Animation loop for updating boid behavior

    private void animationLoop() {
        for (Boid boid : boids) {
            behaviour.flyTowardsCenter(boid);
            behaviour.avoidOthers(boid);
            behaviour.matchVelocity(boid);
            behaviour.limitSpeed(boid);
            behaviour.avoidObstacles(boid);

            if (boundaryOpt.equals("bounce")) {
                behaviour.keepWithinBounds(boid);
            } else if (boundaryOpt.equals("wrap")) {
                behaviour.wrapOnBounds(boid);
            } else {
                behaviour.reflectOnBounds(boid);
            }

            //write frames
            try {
                AnimalPosWriter.close();
                AnimalPosWriter = new BufferedWriter(new FileWriter(getFileName()));
                logBoidPositions();
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            boid.x += boid.dx;
            boid.y += boid.dy;
            boid.history.add(new Point((int) boid.x, (int) boid.y));
            while (boid.history.size() > 20) {
                boid.history.remove(0);
            }
        }
        frameCount ++;
        repaint();
        // repaint(); // Redraw the simulation

    }

    private void logBoidPositions() throws IOException {
        AnimalPosWriter.write(boids.size() + " " + frameCount + "\n");   //write the num of animals and framecount on the first
        for (Boid boid : boids) {
            AnimalPosWriter.write(boid.x + "," + boid.y + "\n");
        }
    }

    private void ObstaclePositions(int x, int y) throws IOException {
        obstacleWriter.write(frameCount + " \n");   //write the num of animals and framecount on the first
        
                obstacleWriter.write(x+ "," + y + " ");
        
        
    }
    private String getFileName() {
        
        return "./Data/boid_positions_" + frameCount + ".txt";
    }

    private String getObstacleFileName() {
        
        return "./Obstacles/Obstacle_positions_" + frameCount + ".txt";
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Graphics2D g2d = (Graphics2D) g;

        // // Draw obstacles
        // simulation.drawObstacles(g2d, trees, 3, new Color(80, 164, 57)); // Pass the entire obstacles list
        // simulation.drawObstacles(g2d, water, 20, new Color(60, 124, 222, 194)); // Pass the entire obstacles list

        // // Draw boids
        // for (Boid boid : boids) {
        //     simulation.drawBoid(g2d, boid);
        // }
    }

    // ActionListener method for animation updates

    @Override
    public void actionPerformed(ActionEvent e) {
        animationLoop();
    }
    // MouseListener methods for spawning boids

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            initBoids(relToRealX(e.getX()), relToRealY(e.getY()));
    }
    // MouseListener methods for drawing obstacles

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            currentObstacle = new ArrayList<>(); // Start a new obstacle
            currentObstacle.add(getRelPoint(e.getX(), e.getY())); // Add the initial point
            try {
                obstacleWriter.close();
                obstacleWriter = new BufferedWriter(new FileWriter(getObstacleFileName()));
                ObstaclePositions(e.getX(),e.getY());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (selectedObs.equals("trees"))
                trees.add(currentObstacle); // Add the completed obstacle
            else
                water.add(currentObstacle);
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!currentObstacle.isEmpty()) {
                if (selectedObs.equals("trees"))
                    trees.add(currentObstacle); // Add the completed obstacle
                else
                    water.add(currentObstacle); // Add the completed obstacle
                currentObstacle = new ArrayList<>(); // Reset the current obstacle
            }
        }
        prevMouseX = 0;
        prevMouseY = 0;
    }

    // MouseMotionListener methods for handling obstacle drawing

    @Override
    public void mouseDragged(MouseEvent e) {
        if(drawing){
            currentObstacle.add(getRelPoint(e.getX(), e.getY())); // Add point to the current obstacle
            // repaint();
        }
        else if(prevMouseX!=0 || prevMouseY!=0){
            int tempX = xOffset + prevMouseX - e.getX();
            int tempY = yOffset + prevMouseY - e.getY();

            // Limit offset to edges
            // tempX = Math.min(Math.max(0, tempX),1500);
            // tempY = Math.min(Math.max(0, tempY),1500);

            // System.out.println(tempY/zoom);
            // System.out.println(heightMaps.GetHeight()*zoom - screenHeight);
            // System.out.println(screenHeight);
            // System.out.println(screenHeight);

            xOffset = tempX;
            yOffset = tempY;

            // xOffset = Math.min(xOffset, (int)Math.ceil(heightMaps.GetWidth()*zoom-xOffset));
            // 
        }
        prevMouseX = e.getX();
        prevMouseY = e.getY();
        repaint();
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // Experimental function for now
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
//         if (e.getWheelRotation() == -1){
// //            timer.stop();
// //            numBoids += 1;
//             visualRange += 50;
//             behaviour.setVisualRange(visualRange);
//         } else if (e.getWheelRotation() == 1) {
// //            timer.start();
// //            numBoids -= 1;
//             visualRange -= 50;
//             behaviour.setVisualRange(visualRange);
//         }
        if (e.getWheelRotation() == -1){
//            timer.stop();
//            numBoids += 1;
            // visualRange += 50;
            // behaviour.setVisualRange(visualRange);
            
            zoom  += 0.1f;
            zoom = Math.max(zoom, maxZoom);
            // xOffset *= zoom/oldZoom;
            // yOffset *= zoom/oldZoom;
            System.out.println(zoom);
        } else if (e.getWheelRotation() == 1) {
//            timer.start();
//            numBoids -= 1;
            // visualRange -= 50;
            // behaviour.setVisualRange(visualRange);
            zoom  -= 0.1f;
            zoom = Math.max(zoom, maxZoom);
            System.out.println(zoom);
        }
        repaint();
    }
    public void pause_playSim(int i) {
        if (i == 0) timer.stop();
        else timer.start();
    }

    public void setNumBoids(int numBoids) {
        this.numBoids = numBoids;
    }
    public void setBoundaryOpt(String boundaryOpt) {
        this.boundaryOpt = boundaryOpt;
    }

    public void setDRAW_TRAIL(boolean DRAW_TRAIL) {
        // Set to true to draw boid trails
        boidPanel.setTrail(DRAW_TRAIL);
    }

    public void setSeparation(double value){
        behaviour.setSeperationForce(value);
    }

    public void setCohesion(double value){
        behaviour.setCohesionForce(value);
    }

    public void setAlignment(double value){
        behaviour.setAlignmentForce(value);
    }
    public void setVisualRange(double value){
        behaviour.setVisualRange(value);
    }

    public void setSelectedObs(String selectedObs) {
        this.selectedObs = selectedObs;
    }

    public int relToRealX(int pos){
        return (int)Math.ceil((pos+xOffset)/zoom);
    }
    public int relToRealY(int pos){
        return (int)Math.ceil((pos+yOffset)/zoom);
    }
    public Point getRelPoint(int x, int y){
        return new Point((int)Math.ceil((x+xOffset)/zoom), (int)Math.ceil((y+yOffset)/zoom));
    }

    public void Clear(){
        timer.stop();
        boids = new ArrayList<>();
        trees = new ArrayList<>();
        water = new ArrayList<>();
        heightMaps = new HeightMaps();
        repaint();
    }

    public void setAnimalColour(Color animalColour) {
       boidPanel.setAnimalColour(animalColour);
    }
}

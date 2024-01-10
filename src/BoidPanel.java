
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class BoidPanel extends JPanel{
    // Define canvas dimensions and simulation parameters
    private boolean Trail;
    private BoidsSimulation parentPanel;
    public HeightMaps heightMaps;
    public Animal[] animals;

    private Color AnimalColour;

    public BoidPanel() {
        // Initialize simulation and event listeners\
        this.setBackground(new Color(0,0,0,0));
    }

    public BoidPanel(BoidsSimulation parentPanel, HeightMaps heightMaps, Behaviour behaviour) {
        // Initialize simulation and event listeners\
        this.setBackground(new Color(0,0,0,0));
        this.heightMaps = heightMaps;
        this.parentPanel = parentPanel;

        // boids =
    }
    public void setAnimals(Animal[] animal){
        this.animals = animal;
    }
    public void drawObstacles(Graphics2D ctx, ArrayList<ArrayList<Point>> obstacles, int size, Color obsColor) {
        ctx.setColor(obsColor); // Set green color for obstacles
        ctx.setStroke(new BasicStroke(size)); // Set the stroke width for drawing

        for (ArrayList<Point> obstacle : obstacles) {
            int numPoints = obstacle.size();

            // Draw individual line segments between consecutive points to form the obstacle shape/ line
            for (int i = 0; i < numPoints - 1; i++) {
                int x1 = (int) obstacle.get(i).getX();
                int y1 = (int) obstacle.get(i).getY();
                int x2 = (int) obstacle.get(i + 1).getX();
                int y2 = (int) obstacle.get(i + 1).getY();
                ctx.drawLine(relativeXPos(x1), relativeYPos(y1), relativeXPos(x2), relativeYPos(y2));
            }
        }

    }

    // Method to draw a Boids on the canvas

    public void drawBoid(Graphics2D g2d, Boid boid) {
        double angle = Math.atan2(boid.dy, boid.dx); // Calculate the angle of the boid's velocity

        // Translate, rotate, and translate back to position the boid correctly
        g2d.translate(relativeXPos(boid.x), relativeYPos(boid.y));
        g2d.rotate(angle);
        g2d.translate(-relativeXPos(boid.x), -relativeYPos(boid.y));
        g2d.setColor(AnimalColour); // Set blue color for the boid

        // Define the points for drawing the boid as a polygon
        int[] xPoints = {relativeXPos(boid.x), relativeXPos(boid.x - 1.5), relativeXPos(boid.x - 1.5), relativeXPos(boid.x)};
        int[] yPoints = {relativeYPos(boid.y), relativeYPos(boid.y + 0.5), relativeYPos(boid.y - 0.5), relativeYPos(boid.y)};

        // Fill the polygon to represent the boid
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Reset the transformation to its original state
        g2d.setTransform(new AffineTransform());

        // Draw a trailing path if enabled
        if (Trail) {
            g2d.setColor(new Color(85, 140, 244, 102)); // Set a semi-transparent blue color
            g2d.setStroke(new BasicStroke(1));
            int numPoints = boid.history.size();
            int[] trailXPoints = new int[numPoints];
            int[] trailYPoints = new int[numPoints];

            // Extract x and y coordinates from the boid's history
            for (int i = 0; i < numPoints; i++) {
                trailXPoints[i] = (int) relativeXPos(boid.history.get(i).getX());
                trailYPoints[i] = (int) relativeYPos(boid.history.get(i).getY());
            }

            // Draw the trailing path using a polyline
            g2d.drawPolyline(trailXPoints, trailYPoints, numPoints);
        }
    }
    public void setTrail(boolean trail) {
        Trail = trail;
    }


    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(heightMaps.loaded){
            Graphics2D g2d = (Graphics2D) graphics;

            drawObstacles(g2d, parentPanel.trees, 3, new Color(80, 164, 57)); // Pass the entire obstacles list
            drawObstacles(g2d, parentPanel.water, 20, new Color(60, 124, 222, 194)); // Pass the entire obstacles list

            // Draw boids
            for (Boid boid : parentPanel.boids) {
                drawBoid(g2d, boid);
            }
            g2d.setColor(Color.red);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect((int)Math.ceil(0*parentPanel.zoom - parentPanel.xOffset),(int)Math.ceil(0*parentPanel.zoom - parentPanel.yOffset),(int)Math.ceil(parentPanel.zoom*heightMaps.width), (int)Math.ceil(parentPanel.zoom*heightMaps.height));
        }
    }

    public int relativeXPos(float pos){
        return((int)Math.ceil(pos*parentPanel.zoom - parentPanel.xOffset));
    }
    public int relativeXPos(double pos){
        return((int)Math.ceil(pos*parentPanel.zoom - parentPanel.xOffset));
    }
    public int relativeYPos(float pos){
        return((int)Math.ceil(pos*parentPanel.zoom - parentPanel.yOffset));
    }
    public int relativeYPos(double pos){
        return((int)Math.ceil(pos*parentPanel.zoom - parentPanel.yOffset));
    }


    public void setAnimalColour(Color animalColour) {
        AnimalColour = animalColour;
    }
}

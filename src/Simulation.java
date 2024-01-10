import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Simulation {
    private boolean Trail;

    // Constructor to initialize the Simulation object
    public Simulation(boolean DRAW_TRAIL) {
        this.Trail = DRAW_TRAIL;
    }
    // Method to draw obstacles on the canvas
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
                ctx.drawLine(x1, y1, x2, y2);
            }
        }

    }

    // Method to draw a Boids on the canvas
    public void drawBoid(Graphics2D g2d, Boid boid) {
        double angle = Math.atan2(boid.dy, boid.dx); // Calculate the angle of the boid's velocity

        // Translate, rotate, and translate back to position the boid correctly
        g2d.translate(boid.x, boid.y);
        g2d.rotate(angle);
        g2d.translate(-boid.x, -boid.y);
        g2d.setColor(new Color(85, 140, 244)); // Set blue color for the boid

        // Define the points for drawing the boid as a polygon
        int[] xPoints = {(int) boid.x, (int) (boid.x - 15), (int) (boid.x - 15), (int) boid.x};
        int[] yPoints = {(int) boid.y, (int) (boid.y + 5), (int) (boid.y - 5), (int) boid.y};

        // Fill the polygon to represent the boid
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Reset the transformation to its original state
        g2d.setTransform(new AffineTransform());

        // Draw a trailing path if enabled
        if (Trail) {
            g2d.setColor(new Color(85, 140, 244, 102)); // Set a semi-transparent blue color
            g2d.setStroke(new BasicStroke(3));
            int numPoints = boid.history.size();
            int[] trailXPoints = new int[numPoints];
            int[] trailYPoints = new int[numPoints];

            // Extract x and y coordinates from the boid's history
            for (int i = 0; i < numPoints; i++) {
                trailXPoints[i] = (int) boid.history.get(i).getX();
                trailYPoints[i] = (int) boid.history.get(i).getY();
            }

            // Draw the trailing path using a polyline
            g2d.drawPolyline(trailXPoints, trailYPoints, numPoints);
        }
    }

    public void setTrail(boolean trail) {
        Trail = trail;
    }
}

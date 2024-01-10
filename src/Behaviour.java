import java.awt.*;
import java.util.ArrayList;

public class Behaviour {

    private ArrayList<Boid> boids; // ArrayList to store Boids objects and obstacle information
    private ArrayList<ArrayList<Point>> trees; // ArrayList to store obstacle information
    private float width, height;
    private double visualRange, cohesionForce, seperationForce, alignmentForce;
    private int obstacleAvoidanceDistance;

    // Constructor to initialize the Behavior object
    public Behaviour(ArrayList<Boid> boids, ArrayList<ArrayList<Point>> trees,
                     float width, float height, int visualRange, int obstacleAvoidanceDistance) {
        this.boids = boids;
        this.trees = trees;
        this.width = width;
        this.height = height;
        this.visualRange = visualRange;
        this.obstacleAvoidanceDistance = obstacleAvoidanceDistance;
        this.seperationForce = 0.015;
        this.cohesionForce =  0.005;
        this.alignmentForce = 0.05;
    }

    // Methods for updating simulation dimensions
    public void updateWidth(float newWidth) {
        this.width = newWidth;
    }

    // Methods for updating simulation dimensions
    public void updateHeight(float newHeight) {
        this.height = newHeight;
    }

    // Helper method to calculate the Euclidean distance between two boids
    public double distance(Boid boid1, Boid boid2) {
        return Math.sqrt(
                Math.pow((boid1.x - boid2.x), 2) +
                        Math.pow((boid1.y - boid2.y), 2)
        );
    }

    // Steering behavior methods:
    // Method to keep boids within simulation bounds
    public void keepWithinBounds(Boid boid) {
        int margin = 1;
        double turnFactor = 1;

        if (boid.x < margin) {
            boid.dx += turnFactor;
        }
        if (boid.x > width - margin) {
            boid.dx -= turnFactor;
        }
        if (boid.y < margin) {
            boid.dy += turnFactor;
        }
        if (boid.y > height - margin) {
            boid.dy -= turnFactor;
        }
    }

    // Method to wrap boids around simulation bounds
    public void wrapOnBounds(Boid boid) {
        int margin = 1;

        if (boid.x < -margin) {
            boid.x = width + margin;
        } else if (boid.x > width + margin) {
            boid.x = -margin;
        }

        if (boid.y < -margin) {
            boid.y = height + margin;
        } else if (boid.y > height + margin) {
            boid.y = -margin;
        }
    }

    // Method to reflect boids off simulation bounds
    public void reflectOnBounds(Boid boid) {
        int margin = 1;

        if (boid.x < margin) {
            boid.x = margin;
        } else if (boid.x > width - margin) {
            boid.x = width - margin;
        }

        if (boid.y < margin) {
            boid.y = margin;
        } else if (boid.y > height - margin) {
            boid.y = height - margin;
        }
    }

    // Method for boids to move towards the center of nearby boids (cohesion behavior)
    public void flyTowardsCenter(Boid boid) {
        double centeringFactor = cohesionForce;
        double centerX = 0;
        double centerY = 0;
        int numNeighbors = 0;

        for (Boid otherBoid : boids) {
            if (distance(boid, otherBoid) < visualRange) {
                centerX += otherBoid.x;
                centerY += otherBoid.y;
                numNeighbors += 1;
            }
        }

        if (numNeighbors > 0) {
            centerX /= numNeighbors;
            centerY /= numNeighbors;

            boid.dx += (centerX - boid.x) * centeringFactor;
            boid.dy += (centerY - boid.y) * centeringFactor;
        }
    }

    // Method for boids to avoid collisions with other nearby boids (separation behavior)
    public void avoidOthers(Boid boid) {
        double minDistance = visualRange;
        double avoidFactor = seperationForce;
        double moveX = 0;
        double moveY = 0;

        for (Boid otherBoid : boids) {
            if (otherBoid != boid) {
                if (distance(boid, otherBoid) < minDistance) {
                    moveX += boid.x - otherBoid.x;
                    moveY += boid.y - otherBoid.y;
                }
            }
        }

        boid.dx += moveX * avoidFactor;
        boid.dy += moveY * avoidFactor;
    }

    // Method for boids to align their velocity with nearby boids (alignment behavior)
    public void matchVelocity(Boid boid) {
        double matchingFactor = alignmentForce;
        double avgDX = 0;
        double avgDY = 0;
        int numNeighbors = 0;

        for (Boid otherBoid : boids) {
            if (distance(boid, otherBoid) < visualRange) {
                avgDX += otherBoid.dx;
                avgDY += otherBoid.dy;
                numNeighbors += 1;
            }
        }

        if (numNeighbors > 0) {
            avgDX /= numNeighbors;
            avgDY /= numNeighbors;

            boid.dx += (avgDX - boid.dx) * matchingFactor;
            boid.dy += (avgDY - boid.dy) * matchingFactor;
        }
    }

    // Method to limit the speed of boids
    public void limitSpeed(Boid boid) {
        double speedLimit = 0.1;

        double speed = Math.sqrt(boid.dx * boid.dx + boid.dy * boid.dy);
        if (speed > speedLimit) {
            boid.dx = (boid.dx / speed) * speedLimit;
            boid.dy = (boid.dy / speed) * speedLimit;
        }
    }

    // Method to find the closest point on a line segment to a given Boids position
    public Point closestPointOnLine(Point lineStart, Point lineEnd, Point point) {
        double lineDeltaX = lineEnd.getX() - lineStart.getX();
        double lineDeltaY = lineEnd.getY() - lineStart.getY();
        double lineLengthSq = lineDeltaX * lineDeltaX + lineDeltaY * lineDeltaY;

        if (lineLengthSq == 0) {
            return lineStart;
        }

        double t = ((point.getX() - lineStart.getX()) * lineDeltaX + (point.getY() - lineStart.getY()) * lineDeltaY) / lineLengthSq;

        if (t < 0) {
            return lineStart;
        } else if (t > 1) {
            return lineEnd;
        }

        double x = lineStart.getX() + t * lineDeltaX;
        double y = lineStart.getY() + t * lineDeltaY;
        return new Point((int) x, (int) y);
    }

    // Method for boids to avoid obstacles
    public void avoidObstacles(Boid boid) {
        // Define the avoidance radius around the Boids
        double avoidanceRadius = obstacleAvoidanceDistance;
        // Define the maximum force to apply for obstacle avoidance
        double maxForce = 0.1;

        // Initialize a vector to represent the avoidance force
        Vector2D avoidanceForce = new Vector2D(0, 0);

        // Iterate through the list of obstacles (line segments)
        for (ArrayList<Point> obstacle : trees) {
            // Iterate through each point in the obstacle to create line segments
            for (int i = 1; i < obstacle.size(); i++) {
                Point lineStart = obstacle.get(i - 1);
                Point lineEnd = obstacle.get(i);

                // Find the closest point on the line segment to the boid's position
                Point closestPoint = closestPointOnLine(lineStart, lineEnd, new Point((int) boid.x, (int) boid.y));
                // Calculate the distance between the boid and the closest point
                double distToObstacle = distance(boid, new Boid(closestPoint.getX(), closestPoint.getY(), 0, 0));

                // Check if the boid is within the avoidance radius of the obstacle
                if (distToObstacle < avoidanceRadius) {
                    // Calculate the vector from the boid to the closest point on the obstacle
                    double moveX = boid.x - closestPoint.getX();
                    double moveY = boid.y - closestPoint.getY();
                    // Calculate the squared distance to the obstacle
                    double distToObstacleSq = moveX * moveX + moveY * moveY;

                    // Normalize the vector
                    double length = Math.sqrt(distToObstacleSq);
                    moveX /= length;
                    moveY /= length;

                    // Calculate the force magnitude based on the distance to the obstacle
                    double forceMagnitude = maxForce * (1 - distToObstacle / avoidanceRadius);

                    // Add the avoidance force components to the total avoidance force
                    avoidanceForce.x += moveX * forceMagnitude;
                    avoidanceForce.y += moveY * forceMagnitude;
                }
            }
        }

        // Apply the avoidance force smoothly to steer away from obstacles
        double maxSteerForce = 0.9; // Maximum steering force
        double desiredSpeed = 4;   // Desired speed of the boid

        // Calculate the desired velocity (direction and speed)
        Vector2D desiredVelocity = new Vector2D(boid.dx, boid.dy);
        desiredVelocity.add(avoidanceForce); // Include avoidance force
        desiredVelocity.normalize();
        desiredVelocity.scale(desiredSpeed);

        // Calculate the steering force
        Vector2D steeringForce = new Vector2D(desiredVelocity.x - boid.dx, desiredVelocity.y - boid.dy);
        if (steeringForce.length() > maxSteerForce) {
            steeringForce.normalize();
            steeringForce.scale(maxSteerForce);
        }

        // Apply the steering force to adjust the boid's direction smoothly
        boid.dx += steeringForce.x;
        boid.dy += steeringForce.y;
    }

    public void setVisualRange(double visualRange) {
        this.visualRange = visualRange;
    }

    public void setCohesionForce(double cohesionForce) {
        this.cohesionForce = cohesionForce;
    }

    public void setSeperationForce(double seperationForce) {
        this.seperationForce = seperationForce;
    }

    public void setAlignmentForce(double alignmentForce) {
        this.alignmentForce = alignmentForce;
    }

}



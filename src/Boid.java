import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Boid {
    double x, y, dx, dy;
    ArrayList<Point> history = new ArrayList<>();
    Color boidColor;

    Boid(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.boidColor = new Color(85, 140, 244);
    }
    Boid(){}

    public Color getBoidColor() {
        return boidColor;
    }
    public void setBoidColor(Color boidColor) {
        this.boidColor = boidColor;
    }
}
public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double length = length();
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }

    public void scale(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    public void add(Vector2D other) {
        x += other.x;
        y += other.y;
    }

    public void subtract(Vector2D other) {
        x -= other.x;
        y -= other.y;
    }

    public double dotProduct(Vector2D other) {
        return x * other.x + y * other.y;
    }

    public double angle(Vector2D other) {
        double dot = dotProduct(other);
        double magProduct = length() * other.length();
        return Math.acos(dot / magProduct);
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

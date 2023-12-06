package clockGame;

// check if any point overlaps the given circle and rectangle (based on circle's center point and radius and any two diagonal points on rectangle)
public class CollisionDetection{
    private int radius;
    private int cX;
    private int cY;
    private int X1;
    private int Y1;
    private int X2;
    private int Y2;

    //https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
    public CollisionDetection(int R, int Xc, int Yc, int X1, int Y1, int X2, int Y2) {
        this.radius = R;
        this.cX = Xc;
        this.cY = Yc;
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
    }

    // Function to check if any point
// overlaps the given circle
// and rectangle
    boolean checkOverlap() {
        // Finds the nearest point on the rectangle to the center of the circle
        int Xn = Math.max(X1, Math.min(cX, X2));
        int Yn = Math.max(Y1, Math.min(cY, Y2));

        // Find the distance between the nearest point and the center of the circle
        // Distance between 2 points, (x1, y1) & (x2, y2) in 2D Euclidean space is ((x1-x2)**2 + (y1-y2)**2)**0.5
        int Dx = Xn - cX;
        int Dy = Yn - cY;
        return (Dx * Dx + Dy * Dy) <= radius * radius;
    }

    void updateDetection(int Xc, int Yc, int X1, int Y1, int X2, int Y2) {
        this.cX = Xc;
        this.cY = Yc;
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
    }

}

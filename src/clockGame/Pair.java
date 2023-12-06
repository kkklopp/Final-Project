package clockGame;

public class Pair {
    public double x;
    public double y;

    public Pair(double initX, double initY){
        x = initX;
        y = initY;
    }

    public Pair add(Pair toAdd){
        return new Pair(x + toAdd.x, y + toAdd.y);
    }

    public Pair divide(double denom){
        return new Pair(x / denom, y / denom);
    }

    public Pair times(double val){
        return new Pair(x * val, y * val);
    }

    public void flipX(){
        x = -x;
    }

    public void flipY(){
        y = -y;
    }
}
package clockGame;

import java.awt.*;

public abstract class Object {
    Pair position;
    Pair velocity;
    Pair acceleration;
    double height;
    double width;

    public Object() {
    }

    public abstract void draw(Graphics g);

    public abstract void verticalTopBounce(); //different bounds for every object

    public abstract void verticalBottomBounce();

    public abstract void horizontalLBounce();

    public abstract void horizontalRBounce();

    public void bounce() {
        verticalTopBounce();
        verticalBottomBounce();
        horizontalLBounce();
        horizontalRBounce();
    }

    public void update(double time) { //add world w, double time
        bounce();
        position = position.add(velocity.times(time));
        velocity = velocity.add(acceleration.times(time));
        //bounce();
    }


    public Pair peekPosition() {
        return position;
    }

    public void setPosition(Pair xyz) {
        position = xyz;
    }


    public Pair peekVelocity() {
        return velocity;
    }

    public void setVelocity(Pair xyz) {
        velocity = xyz;
    }

    public Pair peekAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Pair xyz) {
        acceleration = xyz;
    }

    public double peekHeight() {
        return height;
    }

    public void setHeight(double xyz) {
        height = xyz;
    }

    public double peekWidth() {
        return width;
    }

    public void setWidth(double xyz) {
        width = xyz;
    }

}

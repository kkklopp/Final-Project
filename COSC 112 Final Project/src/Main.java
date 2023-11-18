import AlarmClock.Alarm;

public class Main {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;
    public static final int FPS = 60;

    public static void main(String[] args) {
        new Alarm(WIDTH, HEIGHT, FPS);
        //new Game();
    }

}

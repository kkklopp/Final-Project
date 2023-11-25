<<<<<<< HEAD:Alarm.java
=======
package AlarmClock;

>>>>>>> main:COSC 112 Final Project/src/AlarmClock/Alarm.java
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;
import MathTest.*;


public class Alarm {
    private int width;
    private int height;
    private int fps;
    private JFrame frame;
    private JPanel panel;
    private JButton setButton;
    private JButton onOffButton;
    private JTextField hourField;
    private JTextField minuteField;
    private boolean alarmOn = false;
    private LocalTime alarmTime;
<<<<<<< HEAD:Alarm.java
    public int alertCount = 0;
    private Clip clip;
=======
    public int AlertCount = 0;
    boolean gameComplete = false;
    static DisplayMathScreen mathProblem;
>>>>>>> main:COSC 112 Final Project/src/AlarmClock/Alarm.java

    public Alarm(int initWidth, int initHeight, int initFPS) {
        width = initWidth;
        height = initHeight;
        fps = initFPS;
        frame = new JFrame("Alarm Clock");
        panel = new JPanel();
        setButton = new JButton("Set Alarm");
        onOffButton = new JButton("Turn On");
        hourField = new JTextField(2);
        minuteField = new JTextField(2);

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAlarm();
            }
        });

        onOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageAlarm();
            }
        });

        panel.add(new JLabel("Set Alarm (HH:mm):"));
        panel.add(hourField);
        panel.add(new JLabel(":"));
        panel.add(minuteField);
        panel.add(setButton);
        panel.add(onOffButton);

        frame.add(panel);
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void setAlarm() {
        try {
            int hour = Integer.parseInt(hourField.getText());
            int minute = Integer.parseInt(minuteField.getText());
            if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                alarmTime = LocalTime.of(hour, minute);
                System.out.println("Alarm set for " + alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid time values.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values.");
        }
    }

    private void manageAlarm() {
        if (alarmTime != null) {
            alarmOn = !alarmOn;
            //onOffButton.setText(alarmOn ? "Turn Off" : "Turn On");
            if (alarmOn) {
                startAlarm();
            } else {
                System.out.println("Alarm OFF");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please set the alarm first.");
        }
    }

    private void startAlarm() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            getClass().getResourceAsStream("sound/alarm.wav"));
                    clip.open(inputStream);
                    while (alarmOn) {
                        LocalTime currentTime = LocalTime.now();
                        if (currentTime.getHour() == alarmTime.getHour() &&
                                currentTime.getMinute() == alarmTime.getMinute()) {
<<<<<<< HEAD:Alarm.java
                            alertCount = alertCount + 1;
                            if (alertCount <= 1) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        int option = JOptionPane.showOptionDialog(
                                                frame,
                                                "Wake Up!",
                                                "Alarm",
                                                JOptionPane.DEFAULT_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE,
                                                null,
                                                new Object[]{"Stop Alarm"},
                                                "Stop Alarm");

                                        if (option == 0) {
                                            stopAlarm();
                                        }
                                    }
=======
                            AlertCount = AlertCount + 1;
                            if (AlertCount <= 1){
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    int option = JOptionPane.showOptionDialog(
                                            frame,
                                            "Wake Up!",
                                            "Alarm",
                                            JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            new Object[]{"Play to Stop!"},
                                            "Play to Stop!");

                                    stopAlarm(clip);
                                }
>>>>>>> main:COSC 112 Final Project/src/AlarmClock/Alarm.java
                                });

                                clip.loop(Clip.LOOP_CONTINUOUSLY);
                            }
                        }

<<<<<<< HEAD:Alarm.java
=======
/*
>>>>>>> main:COSC 112 Final Project/src/AlarmClock/Alarm.java
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

 */
                    }

                    clip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

<<<<<<< HEAD:Alarm.java
    public void stopAlarm() {
        alarmOn = false;
        onOffButton.setText("Turn On");
        clip.stop();
    }

    public static void main(String[] args) {
        new Alarm();
=======
    private void stopAlarm(Clip sound) {
        mathProblem = new DisplayMathScreen(width,height,fps);
        if(mathProblem.mathInstance.complete) {
            alarmOn = false;
            onOffButton.setText("Turn On");
            sound.stop();
        }
>>>>>>> main:COSC 112 Final Project/src/AlarmClock/Alarm.java
    }
}
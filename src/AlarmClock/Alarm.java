package AlarmClock;

import javax.swing.*;
import java.awt.*;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;
import MathTest.*;
import clockGame.*;
import Wordle.*;

public class Alarm {
    private int width;
    private int height;
    private int fps;
    private int hammerGoal;
    private int highScore;
    private JFrame frame;
    private JPanel panel;
    private JButton setButton;
    private JButton onOffButton;
    private JTextField hourField;
    private JTextField minuteField;
    private int numAlarms = 0;
    private boolean alarmOn = false;
    private LocalTime alarmTime;
    public String select;
    public int AlertCount = 0;
    static DisplayMathScreen mathProblem;
    static DisplayGameScreen gameScreen;
    static DisplayWordleScreen wordleScreen;
    Clip clip;
    private JComboBox<String> selectComboBox;


    public Alarm(int initWidth, int initHeight, int initFPS) {
        width = initWidth;
        height = initHeight;
        fps = initFPS;
        frame = new JFrame("Alarm Clock");
        panel = new JPanel();
        setButton = new JButton("SET ALARM");
        setButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        onOffButton = new JButton("TURN ON");
        onOffButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
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

        panel.setBackground(Color.BLACK);
        setButton.setForeground(Color.BLACK);

        JLabel label = new JLabel("SET ALARM (HH:MM):");
        label.setForeground(Color.WHITE);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        label.setBounds(230, 100, 500, 40);
        panel.setLayout(null);
        panel.add(label);
        hourField.setBounds(330, 200, 90, 70);
        hourField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        panel.add(hourField);

        JLabel colonLabel = new JLabel(":");
        colonLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        colonLabel.setForeground(Color.WHITE);
        colonLabel.setBounds(425, 210, 20, 40);
        panel.add(colonLabel);

        minuteField.setBounds(450, 200, 90, 70);
        minuteField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        panel.add(minuteField);

        setButton.setBounds(240, 350, 180, 60);
        setButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        onOffButton.setBounds(450, 350, 180, 60);
        onOffButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        panel.add(setButton);
        panel.add(onOffButton);

        String[] gameOptions = {"Choose Method", "Math Questions", "Flappy Clock", "Wordle"};
        selectComboBox = new JComboBox<>(gameOptions);
        selectComboBox.setBounds(340, 300, 180, 30);
        panel.add(selectComboBox);


        frame.add(panel);
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void setAlarm() {
        try {
            int hour = Integer.parseInt(hourField.getText());
            int minute = Integer.parseInt(minuteField.getText());
            if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                if (!isAlarmAlreadySet(alarmTime)) {
                    alarmTime = LocalTime.of(hour, minute);
                    select = (String) selectComboBox.getSelectedItem();
                    System.out.println("Alarm set for " + alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                } else {
                    JOptionPane.showMessageDialog(frame, "Alarm is already set for this time.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid time values.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values.");
        }
    }


    private boolean isAlarmAlreadySet(LocalTime newAlarmTime) {
        for (int i = 0; i < numAlarms; i++) {
            if (alarmTime.equals(newAlarmTime)) {
                return true;
            }
        }
        return false;
    }

    private void manageAlarm() {
        if (alarmTime != null) {
            alarmOn = !alarmOn;
            if (alarmOn) {
                System.out.println("Alarm ON");
                startAlarm();
            } else {
                System.out.println("Alarm OFF");
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Please set the alarm first.");
        }
    }


    private void startAlarm() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            getClass().getResourceAsStream("sound/alarm.wav"));
                    clip.open(inputStream);

                    while (true) {
                        LocalTime currentTime = LocalTime.now();
                        if (currentTime.getHour() == alarmTime.getHour() &&
                                currentTime.getMinute() == alarmTime.getMinute()) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    int option = JOptionPane.showOptionDialog(
                                            panel,
                                            "Wake Up!",
                                            "Alarm",
                                            JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            new Object[]{"Play to Stop!"},
                                            "Play to Stop!");

                                    if (option == 0) {
                                        stopAlarm(clip);
                                        System.out.println("option");
                                    }
                                }
                            });

                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                            break;  // Exit the loop once the alarm starts ringing
                        }

                        // Sleep for a short duration before checking again
                        Thread.sleep(1000);  // Sleep for 1 second (adjust as needed)
                    }

                    clip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void stopAlarm(Clip sound) {
        System.out.println("stoppp");
        System.out.println(select);
        if (select == "Math Questions") {
            mathProblem = new DisplayMathScreen(width, height, fps);
            if (mathProblem.mathInstance.complete) {
            sound.stop();
            alarmOn = false;
            }
        } else if (select == "Flappy Clock") {
            gameScreen = new DisplayGameScreen(width, height, fps, hammerGoal, highScore, frame);
            if (gameScreen.gameInstance.ifGameComplete()) {
                sound.stop();
                alarmOn = false;
            }
       } else if (select == "Wordle") {
            wordleScreen = new DisplayWordleScreen(frame);
            if (wordleScreen.wordleInstance.ifWordleComplete()) {
                sound.stop();
                alarmOn = false;
            }
        }
    }

    public static void main(String[] args) {
        new Alarm(800, 600, 60);
    }
}

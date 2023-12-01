package AlarmClock;
import javax.swing.*;
import java.awt.*;
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
    private JLabel[] alarmLabels = new JLabel[10]; // Assuming a fixed size of 10 alarms for this example
    private JButton[] alarmButtons = new JButton[10]; // Buttons for turning individual alarms on/off
    private LocalTime[] setAlarms = new LocalTime[10];
    private boolean[] alarmStates = new boolean[10]; // Track the on/off state of each alarm
    private int numAlarms = 0;
    private boolean alarmOn = false;
    private LocalTime alarmTime;
    public int AlertCount = 0;
    boolean gameComplete = false;
    static DisplayMathScreen mathProblem;

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
        onOffButton.setForeground(Color.BLACK);

        for (int i = 0; i < 10; i++) {
            alarmLabels[i] = new JLabel();
            alarmLabels[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            alarmLabels[i].setForeground(Color.WHITE);
            alarmLabels[i].setBounds(240, 420 + i * 20, 300, 20);
            panel.add(alarmLabels[i]);

            alarmButtons[i] = new JButton("ON");
            alarmButtons[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            alarmButtons[i].setBounds(550, 420 + i * 20, 70, 20);
            final int index = i;
            alarmButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleAlarm(index);
                }
            });
            panel.add(alarmButtons[i]);
        }

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
                alarmTime = LocalTime.of(hour, minute);
                if (!isAlarmAlreadySet(alarmTime)) {
                    setAlarms[numAlarms] = alarmTime;
                    alarmStates[numAlarms] = false; // Initialize the alarm state to off
                    numAlarms++;
                    updateAlarmLabels();
                    updateAlarmButtons();
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
            if (setAlarms[i].equals(newAlarmTime)) {
                return true;
            }
        }
        return false;
    }

    private void updateAlarmLabels() {
        for (int i = 0; i < numAlarms; i++) {
            alarmLabels[i].setText("Alarm " + (i + 1) + ": " + setAlarms[i].format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    private void updateAlarmButtons() {
        for (int i = 0; i < numAlarms; i++) {
            alarmButtons[i].setText(alarmStates[i] ? "OFF" : "ON");
        }
    }

    private void toggleAlarm(int index) {
        alarmStates[index] = !alarmStates[index];
        alarmButtons[index].setText(alarmStates[index] ? "OFF" : "ON");
    }

    private void manageAlarm() {
        if (alarmTime != null) {
            alarmOn = !alarmOn;
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
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            getClass().getResourceAsStream("sound/alarm.wav"));
                    clip.open(inputStream);
                    while (alarmOn) {
                        LocalTime currentTime = LocalTime.now();
                        for (int i = 0; i < numAlarms; i++) {
                            if (alarmStates[i] && currentTime.getHour() == setAlarms[i].getHour() &&
                                    currentTime.getMinute() == setAlarms[i].getMinute()) {
                                AlertCount = AlertCount + 1;
                                if (AlertCount <= 1) {
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
                                    });

                                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                                }
                            }
                        }
                    }

                    clip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stopAlarm(Clip sound) {
        mathProblem = new DisplayMathScreen(width, height, fps);
        if (mathProblem.mathInstance.complete) {
            alarmOn = false;
            onOffButton.setText("Turn On");
            sound.stop();
        }
    }

    public static void main(String[] args) {
        new Alarm(800, 600, 60);
    }
}
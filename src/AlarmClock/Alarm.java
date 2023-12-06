package AlarmClock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;
import MathTest.*;
import Wordle.*;
import clockGame.*;

public class Alarm {
    private int width;
    private int height;
    private int fps;
    private JFrame frame;
    private JPanel panel;
    private JButton setButton;
    private JTextField hourField;
    private JTextField minuteField;
    private JLabel[] alarmLabels = new JLabel[10];
    private JButton[] alarmButtons = new JButton[10];
    private LocalTime[] setAlarms = new LocalTime[10];
    private int numAlarms = 0;
    private boolean alarmOn = false;
    private LocalTime alarmTime;

    public String select;

    private LocalTime currentTime;
    public int AlertCount = 0;
    boolean gameComplete = false;
    static DisplayMathScreen mathProblem;

    static DisplayGameScreen gameScreen;

    static DisplayWordleScreen wordleScreen;
    Clip clip;

    public Alarm(int initWidth, int initHeight, int initFPS) {
        width = initWidth;
        height = initHeight;
        fps = initFPS;
        frame = new JFrame("Alarm Clock");
        panel = new JPanel();
        setButton = new JButton("SET ALARM");
        setButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        hourField = new JTextField(2);
        minuteField = new JTextField(2);

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAlarm();
            }
        });

        panel.setBackground(Color.BLACK);
        setButton.setForeground(Color.BLACK);

        for (int i = 0; i < numAlarms; i++) {
            final int index = i;
            alarmLabels[i] = new JLabel();
            alarmLabels[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            alarmLabels[i].setForeground(Color.WHITE);
            alarmLabels[i].setBounds(240, 420 + i * 20, 300, 20);
            panel.add(alarmLabels[i]);

            alarmButtons[i] = new JButton("OFF");
            alarmButtons[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            alarmButtons[i].setBounds(550, 420 + i * 20, 70, 20);
            panel.add(alarmButtons[i]);
            alarmButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleAlarm(index);
                }
            });
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

        setButton.setBounds(340, 350, 180, 60);
        setButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        panel.add(setButton);

        f.setLayout(new FlowLayout());

        // array of string containing cities
        String s1[] = { "Math Game", "Wordle", "Flappy Alarm"};

        // create checkbox
        c1 = new JComboBox(s1);

        // add ItemListener
        c1.addItemListener(s);

        // create labels
        l = new JLabel("select the game that you want to play");
        l1 = new JLabel("None Selected");

        // set color of text
        l.setForeground(Color.red);
        l1.setForeground(Color.blue);

        // create a new panel
        JPanel p = new JPanel();

        p.add(l);

        // add combobox to panel
        p.add(c1);

        p.add(l1);

        // add panel to frame
        f.add(p);

        // set the size of frame
        f.setSize(400, 300);

        f.show();
    }
    public void itemStateChanged(ItemEvent e)
    {
        // if the state combobox is changed
        if (e.getSource() == c1) {
            l1.setText(c1.getSelectedItem() + " selected");
        }
    }



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
                    alarmTime = LocalTime.of(hour, minute);
                    numAlarms++;

                    for (int i = 0; i < numAlarms; i++) {
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

                    updateAlarmLabels();
                    updateAlarmButtons();
                    System.out.println("Alarm set for " + alarmTime);
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
            alarmButtons[i].setText("ON");
        }
    }

    private void toggleAlarm(int index) {
        System.out.println(alarmTime);
        System.out.println(setAlarms[numAlarms - 1]);
        alarmOn = !alarmOn;
        if (alarmOn) {
            currentTime = LocalTime.now();
            currentTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            System.out.println(currentTime);
            if (alarmTime.equals(setAlarms[numAlarms - 1])) {
                startAlarm(index);
            } else {
                System.out.println("whyyy");
            }
        } else {
            System.out.println("Alarm OFF");
        }
    }



    private void startAlarm(int index) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            getClass().getResourceAsStream("sound/alarm.wav"));
                    clip.open(inputStream);
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

                                stopAlarm(clip, index);
                            }
                        });

                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    }

                    clip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stopAlarm(Clip sound, int index) {
        if (select == "math") {
            mathProblem = new DisplayMathScreen(width, height, fps);
            if (mathProblem.mathInstance.complete) {
            sound.stop();
            alarmOn = false;
            updateAlarmButtons();
            }
        } else if (select == "flappy") {
            gameScreen = new DisplayGameScreen(width, height, fps, hammerGoal, highScore, frame);
            if (gameScreen.gameInstance.ifGameComplete()) {
                sound.stop();
                alarmOn = false;
                updateAlarmButtons();
            }
        } else if (select == "wordle") {
            wordleScreen = new DisplayWordleScreen(width, height, fps);
            if (wordleScreen.wordleInstance.ifWordleComplete()) {
                sound.stop();
                alarmOn = false;
                updateAlarmButtons();
            }
        }
    }

    public static void main(String[] args) {
        new Alarm(800, 600, 60);
    }
}

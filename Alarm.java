import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;

public class Alarm {
    private JPanel panel;
    private JTextField hourField;
    private JTextField minuteField;
    private JButton onOffButton;
    private boolean alarmOn = false;
    private LocalTime alarmTime;
    private Clip clip;
    private MathProblems mathProblems;  
    public int stop=0;

    public Alarm() {
        panel = createPanel();
        mathProblems = new MathProblems(2);  
    }

    public JPanel getPanel() {
        return panel;
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel();
        JButton setButton = new JButton("Set Alarm");
        onOffButton = new JButton("Turn On");
        hourField = new JTextField(2);
        minuteField = new JTextField(2);

        setButton.addActionListener(e -> setAlarm());

        onOffButton.addActionListener(e -> manageAlarm());

        panel.add(new JLabel("Set Alarm (HH:mm):"));
        panel.add(hourField);
        panel.add(new JLabel(":"));
        panel.add(minuteField);
        panel.add(setButton);
        panel.add(onOffButton);

        return panel;
    }

    private void setAlarm() {
        try {
            int hour = Integer.parseInt(hourField.getText());
            int minute = Integer.parseInt(minuteField.getText());
            if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                alarmTime = LocalTime.of(hour, minute);
                System.out.println("Alarm set for " + alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                JOptionPane.showMessageDialog(panel, "Invalid input. Please enter valid time values.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Invalid input. Please enter numeric values.");
        }
    }

    private void manageAlarm() {
        if (alarmTime != null) {
            alarmOn = !alarmOn;
            onOffButton.setText(alarmOn ? "Turn Off" : "Turn On");
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
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        getClass().getResourceAsStream("alarm.wav"));
                clip.open(inputStream);
                while (alarmOn) {
                    LocalTime currentTime = LocalTime.now();
                    if (currentTime.getHour() == alarmTime.getHour() &&
                            currentTime.getMinute() == alarmTime.getMinute()) {
                                if (stop==0)
                                stop=stop+1;
                                SwingUtilities.invokeLater(() -> {
                                    int option = JOptionPane.showOptionDialog(
                                    panel,
                                    "Wake Up!",
                                    "Alarm",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    new Object[]{"Stop Alarm"},
                                    "Stop Alarm");

                            if (option == 0) {
                                stopAlarm();
                                mathProblems.runMathProblems(); 
                            }
                        });

                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                clip.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void stopAlarm() {
        alarmOn = false;
        onOffButton.setText("Turn On");
        clip.stop();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Alarm");
            Alarm alarm = new Alarm();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(alarm.getPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

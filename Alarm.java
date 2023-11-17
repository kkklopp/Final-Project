
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;

public class Alarm {
    private JFrame frame;
    private JPanel panel;
    private JButton setButton;
    private JButton onOffButton;
    private JTextField hourField;
    private JTextField minuteField;
    private boolean alarmOn = false;
    private LocalTime alarmTime;
    public int alertCount = 0; 

    public Alarm() {
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
            onOffButton.setText(alarmOn ? "Turn Off" : "Turn On");
            if (alarmOn) {
                System.out.println("Alarm ON");
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
                            getClass().getResourceAsStream("alarm.wav"));
                    clip.open(inputStream);
                    while (alarmOn) {
                        LocalTime currentTime = LocalTime.now();
                        if (currentTime.getHour() == alarmTime.getHour() &&
                                currentTime.getMinute() == alarmTime.getMinute()) {
                            alertCount = alertCount + 1;
                            if (alertCount <= 1){
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
                                        alarmOn = false;
                                        onOffButton.setText("Turn On");
                                        clip.stop();  
                                    }
                                }
                                });
    
                            clip.loop(Clip.LOOP_CONTINUOUSLY);  };
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
            }
        }).start();
    }
    
    public static void main(String[] args) {
        new Alarm();
    }
}

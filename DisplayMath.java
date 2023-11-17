import javax.swing.JFrame;
import java.awt.Dimension;
import java.lang.Math;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;
import java.util.Scanner;

public class DisplayMath{
    public DisplayMath(){
    JFrame frame = new JFrame("Math Problems!");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Math mainInstance = new Math(2);

    
    frame.setContentPane(mainInstance);
    frame.pack();
    frame.setVisible(true);

    }
    
}

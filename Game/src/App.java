import java.awt.Image;

import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int broadWith=1000;
        int broadHeight=630;
        JFrame frame= new JFrame("Flappy bird");
        

        frame.setSize(broadWith,broadHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Flappy_bird flappy_bird= new Flappy_bird();
        frame.add(flappy_bird);
        frame.pack();
        flappy_bird.requestFocus();
        frame.setVisible(true);
        
    }
}

import javax.swing.*;

/**
 * Created by jason on 1/19/2016.
 */

public class Main {
    static private int frameRate;
    static private int speed;

    public static void createAndShowGUI(){
        GameFrame f = new GameFrame(frameRate, speed); // jframe is the app window
        Splashscreen splashScreen = new Splashscreen(f);
        f.setContentPane(splashScreen);
        //f.setContentPane(game); // add canvas to jframe
        f.setVisible(true); // show the window
        f.setTitle("Super-awesome BreakOut!");



    }
    public static void main(String[] args) {
        if (args.length < 2){
            frameRate = 60;
            speed = 2;
        }
        else if (args.length == 2){
            frameRate = Integer.parseInt(args[0]);
            speed = Integer.parseInt(args[1]);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }
}

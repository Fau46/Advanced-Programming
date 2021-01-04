import javax.swing.*;
import java.io.Serializable;

public class BusBoard extends JFrame implements Serializable {
    public static void main(String[] args) {
        Bus bus = new Bus();
        bus.addVetoableChangelistenerNumPassenger(new CovidController());
        bus.setNumPassenger(0);
        bus.setNumPassenger(10);

        JFrame window = new JFrame("BusBoard");
        window.setSize(800,600);
        window.setLocation(100,100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);



    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

public class BusBoard extends JFrame implements Serializable, PropertyChangeListener, ActionListener {

    private JFrame window;
    private JLabel numPassenger;
    private JLabel doorOpen;
    private JTextField textField;
    private JButton button;
    private Bus bus;

    public static void main(String[] args) {
        BusBoard busBoard = new BusBoard();

    }

    public BusBoard(){
        bus = new Bus();
        bus.addVetoableChangelistenerNumPassenger(new CovidController());
        bus.addPropertyChangeListenerDoorOpen(this);
        bus.addPropertyChangeListenerNumPassenger(this);

        window = new JFrame("BusBoard");
        window.setSize(800,600);
        window.setLocation(100,100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridLayout(2,2));

        numPassenger = new JLabel(String.valueOf(bus.getNumPassenger()));
        doorOpen = new JLabel(String.valueOf(bus.getDoorOpen()));
        JPanel panel1 = new JPanel();
        panel1.add(numPassenger);
        panel1.add(doorOpen);
        window.add(panel1);

        textField = new JTextField("1",16);
        button = new JButton("ENTER");
        button.addActionListener(this);
        JPanel panel2 = new JPanel();
        panel2.add(textField);
        panel2.add(button);
        window.add(panel2);

        window.setVisible(true);
    }

    public void setDoorOpen(String doorOpen) {
        System.out.println("son qui"+ doorOpen);
        this.doorOpen.setText(doorOpen);
    }

    public void setNumPassenger(String numPassenger) {
        this.numPassenger.setText(numPassenger);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName() == "numPassenger") this.setNumPassenger(String.valueOf(evt.getNewValue()));
        else {
            this.setDoorOpen(String.valueOf(evt.getNewValue()));
        }
        System.out.println("new event: "+evt.getPropertyName()+" "+evt.getOldValue()+" "+evt.getNewValue());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        bus.setNumPassenger(Integer.valueOf(textField.getText()).intValue());
    }
}

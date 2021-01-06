import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;


public class BusBoard extends JFrame implements PropertyChangeListener, ActionListener {

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

        //Create the frame
        window = new JFrame("BusBoard");
        window.setSize(800,600);
        window.setLocation(100,100);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridLayout(3,1));

        JLabel numPassengerLabel = new JLabel("Number of passenger: ");
        numPassenger = new JLabel(String.valueOf(bus.getNumPassenger()));
        JPanel panel1 = new JPanel();
        panel1.add(numPassengerLabel);
        panel1.add(numPassenger);
        window.add(panel1);


        JLabel doorOpenLabel = new JLabel("Door opened: ");
        doorOpen = new JLabel(String.valueOf(bus.getDoorOpen()));
        JPanel panel2 = new JPanel();
        panel2.add(doorOpenLabel);
        panel2.add(doorOpen);
        window.add(panel2);

        textField = new JTextField("1",16);
        button = new JButton("ENTER");
        button.addActionListener(this);
        JPanel panel3 = new JPanel();
        panel3.add(textField);
        panel3.add(button);
        window.add(panel3);

        window.setVisible(true);
//        bus.activate();
    }


    public void setDoorOpen(String doorOpen) {
        this.doorOpen.setText(doorOpen);
    }


    public void setNumPassenger(String numPassenger) {
        this.numPassenger.setText(numPassenger);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("numPassenger")) {
            this.setNumPassenger(String.valueOf(evt.getNewValue()));
        }
        else {
            this.setDoorOpen(String.valueOf(evt.getNewValue()));
        }
        System.out.println("[EVENT] Name: "+evt.getPropertyName()+" | Old value: "+evt.getOldValue()+" | New value: "+evt.getNewValue());
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        button.setBackground(Color.RED);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int newValue;
                try{
                    newValue = Integer.valueOf(textField.getText()).intValue();
                }catch (NumberFormatException e){
                    System.out.println("[INFO] Input not valid");
                    button.setBackground(null);
                    textField.setText("1");
                    return;
                }

                if(newValue < 1 || newValue > 5){
                    System.out.println("[INFO] Input must be between 1 and 5");
                    button.setBackground(null);
                    textField.setText("1");
                    return;
                }

                bus.setNumPassenger(bus.getNumPassenger()+newValue);
                button.setBackground(null);
                textField.setText("1");
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task,2000L);
    }
}

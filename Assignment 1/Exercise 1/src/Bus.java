import java.beans.*;
import java.io.Serializable;

public class Bus implements Serializable {
    private int capacity;
    private int numPassenger;
    private boolean doorOpen;
    private PropertyChangeSupport listenerDoorOpen;
    private PropertyChangeSupport listenerNumPassenger;
    private VetoableChangeSupport vetosNumPassenger;

    public Bus(){
        capacity = 50;
        doorOpen = false;
        numPassenger = 20;
        listenerDoorOpen = new PropertyChangeSupport(this);
        listenerNumPassenger = new PropertyChangeSupport(this);
        vetosNumPassenger = new VetoableChangeSupport(this);
    }

    public void setDoorOpen(boolean newDoorOpen){
        boolean oldDoorOpen = doorOpen;
        doorOpen = newDoorOpen;
        listenerDoorOpen.firePropertyChange("doorOpen", oldDoorOpen, doorOpen);
    }


    public void setNumPassenger(int newNumPassenger){
        int oldNumPassenger = numPassenger;
        try {
            vetosNumPassenger.fireVetoableChange("numPassenger", numPassenger, newNumPassenger);
        } catch (PropertyVetoException e) {
            System.out.println("[numPassenger] Change rejected: "+e.getMessage());
            return;
        }

        if(newNumPassenger > capacity){
            System.out.println("The number of passenger is more than the bus capacity");
        }
        else if(newNumPassenger < 0){
            System.out.println("Value not accepted");
        }
        else{
            if (newNumPassenger > oldNumPassenger){
                setDoorOpen(true);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setDoorOpen(false);
            }

            numPassenger = newNumPassenger;
            listenerNumPassenger.firePropertyChange("numPassenger", oldNumPassenger, numPassenger);
        }
    }


    public boolean getDoorOpen(){
        return this.doorOpen;
    }


    public int getNumPassenger() {
        return this.numPassenger;
    }

    //Method that add listener to listenerDoorOpen
    public void addPropertyChangeListenerDoorOpen(PropertyChangeListener l){
        listenerDoorOpen.addPropertyChangeListener(l);
    }


    //Method that remove listener from listenerDoorOpen
    public void removePropertyChangeListenerDoorOpen(PropertyChangeListener l){
        listenerDoorOpen.removePropertyChangeListener(l);
    }


    //Method that add listener to listenerNumPassenger
    public void addPropertyChangeListenerNumPassenger(PropertyChangeListener l){
        listenerNumPassenger.addPropertyChangeListener(l);
    }


    //Method that remove listener from listenerNumPassenger
    public void removePropertyChangeListenerNumPassenger(PropertyChangeListener l){
        listenerNumPassenger.removePropertyChangeListener(l);
    }


    //Method that add listener to vetosNumPassenger
    public void addVetoableChangelistenerNumPassenger(VetoableChangeListener l){
        vetosNumPassenger.addVetoableChangeListener(l);
    }


    //Method that remove listener from vetosNumPassenger
    public void removeVetoableChangelistenerNumPassenger(VetoableChangeListener l){
        vetosNumPassenger.removeVetoableChangeListener(l);
    }
}
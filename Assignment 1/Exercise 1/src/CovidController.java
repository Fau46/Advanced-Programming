import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.Serializable;

public class CovidController implements Serializable, PropertyChangeListener, VetoableChangeListener {
    private int reducedCapacity;

    public CovidController(){
        reducedCapacity = 25;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName()+" has changed: "+evt.getNewValue());
    }


    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        int newValue = (int) evt.getNewValue();
        if(newValue > reducedCapacity){
            throw new PropertyVetoException("New value exceeded the reduced capacity", evt);
        }
    }
}

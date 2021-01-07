import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class XMLSerializer {
    public static void serialize(Object [ ] arr, String fileName){
        Class c = arr[0].getClass();
        if(!c.isAnnotationPresent(XMLable.class)) return; //Check if the annotation is present

        StringBuilder aux = new StringBuilder();
        for(int i=0; i< arr.length; i++){
            aux.append(serializeObj(c, arr[i]));
        }

        try {
            writeToFile(aux.toString(), fileName+".xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Serialization completed");
    }

    private static String serializeObj(Class objClass, Object obj){
        StringBuilder serializedString = new StringBuilder();
        String nameClass = objClass.getName();
        serializedString.append("<"+nameClass+">\n");

        for(Field field : objClass.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(XMLfield.class)){//Check if the annotation is present for the serialization
                XMLfield annotation = field.getAnnotation(XMLfield.class);
                String serializedField = null;
                String tagName = !annotation.name().equals("")? annotation.name() : field.getName();
                try {
                    serializedField = "   <"+ tagName + " type=\""+annotation.type()+"\">" + field.get(obj) + "</"+ tagName +">\n";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                serializedString.append(serializedField);
            }
        }

        serializedString.append("</"+nameClass+">\n");
        return serializedString.toString();
    }

    private static void writeToFile(String serializedString, String fileName) throws IOException {
        BufferedWriter buf = new BufferedWriter(new FileWriter(fileName));
        buf.write(serializedString);
        buf.close();
    }
}

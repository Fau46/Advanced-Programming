import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class XMLDeserializer {
    public XMLDeserializer(){}

    public static Object[] deserialize(String path) {
        if(!path.endsWith(".xml")) new IllegalArgumentException().printStackTrace(); //Accept only xml files

        File xmlfile = new File(path);
        BufferedReader reader;
        ArrayList<Object> objectsInstance= new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(xmlfile));

            String line = reader.readLine(); //Read the class name
            String className = line.replaceAll("[<>]","");

            Class class1 = Class.forName(className);
            if(!class1.isAnnotationPresent(XMLable.class)) return null; //Check if the class is serializable

            while (line != null){
                ArrayList<String> instanceVariables = new ArrayList<>();

                while(!line.equals("</"+className+">")){ //Read all instance variables
                    line = reader.readLine();
                    if(!line.equals("</"+className+">")) instanceVariables.add(line);
                }
                objectsInstance.add(deserializeObject(class1, className, instanceVariables));

                line = reader.readLine(); //Read next class name e.g. <Test> or EOF
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return objectsInstance.toArray();
    }

    private static Object deserializeObject(Class cl, String className, List<String> instanceVariables) {
        Object object = null;
        try {
            object = cl.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String line = null;
        for(String l : instanceVariables){
            line = l.substring(4); //Delete tab and < characters
            String[] fields = line.split(" "); //Split for retrieve the field's name
            String tagName = fields[0];
            Field field = retrieveField(cl, tagName);

            String[] typeAndValue = fields[1].split(">"); //split the type and the value in two string
            String type = typeAndValue[0].replaceAll("type=\"",""); //Clean the string from unimportant characters for retrieve the field's type
            type = type.replaceAll("[\"]","");
            String value = typeAndValue[1].replaceAll("</"+tagName, ""); //Clean the string from unimportant characters for retrieve the field's value

            try {
                switch (type){
                    case "boolean":
                        field.setBoolean(object, Boolean.parseBoolean(value));
                        break;
                    case "byte":
                        field.setByte(object, Byte.parseByte(value));
                        break;
                    case "char":
                        field.setChar(object, value.charAt(0));
                        break;
                    case "double":
                        field.setDouble(object, Double.parseDouble(value));
                        break;
                    case "float":
                        field.setFloat(object, Float.parseFloat(value));
                        break;
                    case "int":
                        field.setInt(object, Integer.parseInt(value));
                        break;
                    case "long":
                        field.setLong(object, Long.parseLong(value));
                        break;
                    case "short":
                        field.setShort(object, Short.parseShort(value));
                        break;
                    default:
                        field.set(object,value);
                        break;

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return object;

    }

    private static Field retrieveField(Class cl, String fieldString) {
        Field f = null;
        try {
            f = cl.getDeclaredField(fieldString);
            f.setAccessible(true);
        } catch (NoSuchFieldException e) { //Manage the case that the field is stored with a different name
            for(Field field : cl.getDeclaredFields()){
                field.setAccessible(true);

                if(field.isAnnotationPresent(XMLfield.class)){ //Check if the annotation is present for the serialization
                    XMLfield annotation = field.getAnnotation(XMLfield.class);
                    String tagName = !annotation.name().equals("")? annotation.name() : field.getName();
                    if(tagName.equals(fieldString)) f = field;
                }
            }

            if(f == null) new NoSuchFieldException().printStackTrace(); //Manage the case when the fieldString is not associated to a field
        }
        
        return f;
    }


}

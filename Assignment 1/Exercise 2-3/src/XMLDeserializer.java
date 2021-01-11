import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class XMLDeserializer {
    public XMLDeserializer(){}

    public static Object[] deserialize(String path) {
        if(!path.endsWith(".xml")) new IllegalArgumentException().printStackTrace();

        File xmlfile = new File(path);
        BufferedReader reader;
        ArrayList<Object> objectsInstance= new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(xmlfile));

            String line = reader.readLine();
            String className = line.replaceAll("[<>]","");

            Class class1 = Class.forName(className);
            if(!class1.isAnnotationPresent(XMLable.class)) return null;

            while (line != null){
                ArrayList<String> instanceVariables = new ArrayList<>();
                while(!line.equals("</"+className+">")){
                    line = reader.readLine();
                    if(!line.equals("</"+className+">")) instanceVariables.add(line);
                }
                objectsInstance.add(deserializeObject(class1, className, instanceVariables));

                line = reader.readLine();
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
            line = l.substring(4);
            String[] fields = line.split(" ");
            String tagName = fields[0];
            Field field = retrieveField(cl, tagName);

            String[] typeAndValue = fields[1].split(">");
            String type = typeAndValue[0].replaceAll("type=\"","");
            type = type.replaceAll("[\"]","");
            String value = typeAndValue[1].replaceAll("</"+tagName, "");
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
        } catch (NoSuchFieldException e) {
            for(Field field : cl.getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(XMLfield.class)){//Check if the annotation is present for the serialization
                    XMLfield annotation = field.getAnnotation(XMLfield.class);
                    String tagName = !annotation.name().equals("")? annotation.name() : field.getName();
                    if(tagName.equals(fieldString)) f = field;
                }
            }
            if(f == null) new NoSuchFieldException().printStackTrace();
        }
        
        return f;
    }


}

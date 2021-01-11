import Util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class InvertedIndex extends MapReduce<String, String, List<String >, Pair<String,Integer>, String>{

    public InvertedIndex(){}


    @Override
    protected Stream<Pair<String, List<String>>> read() {

        System.out.print("[INVERTED INDEX]\nInsert the absolute path of your directory: ");
        Scanner input = new Scanner(System.in);
        String path = input.nextLine();
        Reader reader = new Reader(Path.of(path));
        try {
            return reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected Stream<Pair<String,Pair<String,Integer>>> map(Stream<Pair<String,List<String>>> input) {
        List<Pair<String,Pair<String,Integer>>> newList = new ArrayList<>();

        input.forEach(x ->{
            List<String> list = x.getValue(); //Get the list with all the lines of the file
            String key = x.getKey();
            for(int i = 0; i<list.size(); i++){
                String[] strings = list.get(i).split(" "); //Split each line
                for (String string : strings){
                    if(
                        string.startsWith("'") ||
                        string.startsWith("—") ||
                        string.startsWith("‘") ||
                        string.startsWith("’")
                    ){
                        string = string.substring(1);
                    }
                    string = string.replaceAll("[“.,!?”()_\"]", "");
                    if(string.length() > 3) { //Get only the strings with a length > of 3
                        Pair<String, Integer> value = new Pair<>(key, i);
                        newList.add(new Pair<>(string, value));
                    }
                }
            }
        });

        return newList.stream();
    }


    @Override
    protected int compare(String a, String b) {
        return a.compareTo(b);
    }


    @Override
    protected Stream<Pair<String, String>> reduce(Stream<Pair<String, List<Pair<String, Integer>>>> input) {
        List<Pair<String,String>> newList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        input.forEach(x ->{
            List<Pair<String,Integer>> values = x.getValue(); //Get the list with pair objects
            String key = x.getKey();
            values //Stream of Pair object
                .stream()
                .sorted((z,y) -> compareTo(z,y))
                .forEach( w -> {
                    String string = w.getKey()+", "+ w.getValue().toString();
                    stringList.add(key+"!"+string); //Merge key and value and create a single string, used for distinct method below
                }

            );
        });

        stringList.stream().distinct().forEach(x ->{
            String[] strings = x.split("!"); //Split the string for retrieve the key and the value
            String key = strings[0];
            String value = strings[1];
            newList.add(new Pair<>(key,value));
        });

        return newList.stream();
    }

    @Override
    protected void write(Stream<Pair<String, String>> input) {
        File file = new File("output-ii.csv");
        try {
            Writer.writeString(file, input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int compareTo(Pair<String,Integer> v1, Pair<String,Integer> v2){
        String key1 = v1.getKey();
        String key2 = v2.getKey();
        int comp = compare(key1, key2);
        if(comp != 0) return comp;
        else{
            Integer value1 = v1.getValue();
            Integer value2 = v2.getValue();
            return value1.compareTo(value2);
        }
    }

}

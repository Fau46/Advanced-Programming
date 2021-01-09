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

        System.out.print("[INVERTED INDEX]\nInsert the absolute path of your txt file: ");
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
            List<String> list = x.getValue();
            String key = x.getKey();
            for(int i = 0; i<list.size(); i++){
                String[] strings = list.get(i).split(" ");
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
                    if(string.length() > 3) {
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
        List<Pair<String,String>> newListNoDuplicates = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        input.forEach(x ->{
            List<Pair<String,Integer>> values = x.getValue();
            String key = x.getKey();
            values
                .stream()
                .sorted((z,y) -> compareTo(z,y))
                .forEach( w -> {
                    String string = w.getKey()+", "+ w.getValue().toString();
//                    newList.add(new Pair<>(key,string));
                    stringList.add(key+"!"+string);
                }

            );
        });

        stringList.stream().distinct().forEach(x ->{
            String[] strings = x.split("!");
            String key = strings[0];
            String value = strings[1];
            newList.add(new Pair<>(key,value));
        });
//        newList.stream().distinct().forEach(x -> newListNoDuplicates.add(new Pair<>(x.getKey(), x.getValue())));
//        newListNoDuplicates = filter(newList); //TODO mettere filter nel return direttamente
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

    private List<Pair<String, String>> filter(List<Pair1<String, String>> lst) {
        List<Pair<String,String>> aux = new ArrayList<>();
        for(int i=0; i<lst.size()-1; i++){
            Pair1<String,String> p1 = lst.get(i);
            Pair1<String,String> p2 = lst.get(i+1);
            if(!p1.equals(p2)) aux.add(new Pair<>(p1.getKey(),p2.getValue()));
        }

//        Pair1<String,String> p1 = lst.get(lst.size()-2);
//        Pair1<String,String> p2 = lst.get(lst.size()-1);
//        if(!p2.equals(p1)) aux.add(new Pair<>(p2.getKey(), p2.getValue()));
        return aux;
    }
}

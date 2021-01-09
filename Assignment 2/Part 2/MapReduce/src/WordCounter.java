import Util.Pair;
import Util.Reader;
import Util.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class WordCounter extends MapReduce<String, String, List<String>, Integer, Integer>{

    public WordCounter(){}

    @Override
    protected Stream<Pair<String, List<String>>> read() {

        System.out.print("[COUNTING WORDS]\nInsert the absolute path of your txt file: ");
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
    protected Stream<Pair<String, Integer>> map( Stream<Pair<String, List<String>>> input){

        List<Pair<String, Integer>> newList = new ArrayList<>();

        input.forEach( x -> {
            List<String> list = x.getValue();
            for (int i = 0; i < list.size(); i++) {
                String[] strings = list.get(i).split(" ");
                Map<String, Integer> auxMap = new HashMap<>();
                for (String string : strings) {
                    if(
                            string.startsWith("'") ||
                            string.startsWith("—") ||
                            string.startsWith("‘") ||
                            string.startsWith("’")
                    ){
                        string = string.substring(1);
                    }
                    string = string.replaceAll("[“;.,!?”()_\"]", "");
                    if (string.length() > 3) {
                        if (!auxMap.containsKey(string)) auxMap.put(string, 1);
                        else auxMap.put(string, auxMap.get(string) + 1);
                    }
                }
                auxMap.forEach((k, v) -> newList.add(new Pair<>(k, v)));
            }
        });

        return newList.stream();
    }

    @Override
    protected int compare(String a, String b) {
        return a.compareTo(b);
    }

    @Override
    protected Stream<Pair<String, Integer>> reduce(Stream<Pair<String, List<Integer>>> input) {
        List<Pair<String,Integer>> newList = new ArrayList<>();

        input.forEach(x->{
            List<Integer> list = x.getValue();
            int accum = 0;
            for(Integer element : list){
                accum += element;
            }
            newList.add(new Pair<>(x.getKey(), accum));
        });

        return newList.stream();
    }

    @Override
    protected void write(Stream<Pair<String, Integer>> input) {
        File file = new File("output-wc.csv");
        try {
            Writer.write(file, input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

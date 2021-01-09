import Util.*;

import java.util.*;
import java.util.stream.Stream;

public abstract class MapReduce<K1,K2,V1,V2,V3>{

    public MapReduce(){}

    public final void run(){
        Stream<Pair<K1,V1>> stage_1 = read();
        Stream<Pair<K2,V2>> stage_2 = map(stage_1);
        Stream<Pair<K2,List<V2>>> stage_3 = groupAndSort(stage_2);
        Stream<Pair<K2,V3>> stage_4 = reduce(stage_3);
        write(stage_4);

    }

    private Stream<Pair<K2,List<V2>>> groupAndSort(Stream<Pair<K2,V2>> input){
        Map<K2, List<V2>> list = new HashMap<>();

        input.forEach((Pair<K2,V2> element) -> {
            K2 key = element.getKey();
            V2 value = element.getValue();
            if(!list.containsKey(key)) list.put(key, new ArrayList<>());
            list.get(key).add(value);
        });

        return list.entrySet()
                .stream()
                .map(x -> new Pair<>(x.getKey(),x.getValue()))
                .sorted((x,y) -> this.compare(x.getKey(), y.getKey()));
    }


    protected abstract Stream<Pair<K1,V1>> read();
    protected abstract Stream<Pair<K2,V2>> map(Stream<Pair<K1,V1>> input);
    protected abstract int compare(K2 a, K2 b);
    protected abstract Stream<Pair<K2,V3>> reduce(Stream<Pair<K2,List<V2>>> input);
    protected abstract void write(Stream<Pair<K2,V3>> input);


}

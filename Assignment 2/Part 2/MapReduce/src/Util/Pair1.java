package Util;

public class Pair1<K,V> extends Pair<K,V> {
    public Pair1(K key, V value) {
        super(key, value);
    }

    public boolean equals(Pair1<K,V> val) {
        return  (this.getKey().equals(val.getKey())) && (this.getValue().equals(val.getValue()));
    }

}

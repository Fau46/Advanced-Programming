package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.stream.Stream;

public class Writer {
    public static void write (File dst, Stream<Pair<String, Integer>> res) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(dst, false));
        res.sorted(Comparator.comparing(Pair::getKey))
            .forEach(p -> ps.println(p.getKey() + ", " + p.getValue()));
        ps.close();
    }

    public static void writeString (File dst, Stream<Pair<String, String>> res) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(dst, false));
        res.sorted(Comparator.comparing(Pair::getKey))
                .forEach(p -> ps.println(p.getKey() + ", " + p.getValue()));
        ps.close();
    }
}

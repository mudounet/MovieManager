package ui;


import java.io.Serializable;
import java.util.Comparator;


public final class MovieObject implements Comparator, Serializable {
    private static final long serialVersionUID = 1L;

    public static String getName() {
       return "test implementation";
    }

    public int compare(Object o1, Object o2) {
        return 0;
    }
}


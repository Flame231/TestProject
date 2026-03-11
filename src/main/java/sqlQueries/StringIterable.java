package sqlQueries;

import java.util.Iterator;

public class StringIterable implements Iterable<String> {
    private final String[] string;

    public StringIterable(String[] string) {
        this.string = string;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < string.length;
            }

            @Override
            public String next() {
                return string[cursor++];
            }
        };
    }
}

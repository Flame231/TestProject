package sqlQueries;

import java.util.Iterator;

public class QueryBuilder {
    StringIterable stringIterable;
    StringBuffer stringBuffer;

    public QueryBuilder(StringIterable stringIterable, StringBuffer stringBuffer) {
        this.stringIterable = stringIterable;
        this.stringBuffer = stringBuffer;
    }

    public String getQuery() {
        Iterator<String> iterator = stringIterable.iterator();
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String string = iterator.next();
            stringBuffer.append(string);
            if (iterator.hasNext()) {
                stringBuffer.append(",");
            } else {
                break;
            }

        }
        return stringBuffer.toString();
    }
}

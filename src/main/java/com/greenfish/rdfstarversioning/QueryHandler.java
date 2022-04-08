package com.greenfish.rdfstarversioning;

import java.text.MessageFormat;

public class QueryHandler {

    public static String timestampQuery(String query) {
        //TODO: transform query into timestamped query via query algebra injection
        //return "Select * where {?s ?p ?o. } limit 5";
        return "Select * where {?s ?p ?o. } limit 10";
    }

    public static String timestampInsert(String updateStmt) {
        String c = "default graph";
        String s = "<http://example.com/s1>";
        String p = "<http://example.com/p1>";
        String o = "\"o22\"";

        return  MessageFormat.format(Utils.readAllBytes("timestampedInsertTemplate"), c, s, p, o);

    }

    public static String timestampDelete(String updateStmt) {
        String c = "default graph";
        String s = "<http://example.com/s1>";
        String p = "<http://example.com/p1>";
        String o = "\"o22\"";

        return  MessageFormat.format(Utils.readAllBytes("timestampedDeleteTemplate"), c, s, p, o);
    }

}

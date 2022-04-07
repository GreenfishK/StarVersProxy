package com.greenfish.rdfstarversioning;

public class QueryHandler {

    public static String timestampQuery(String query) {
        //TODO: transform query into timestamped query via query algebra injection
        //return "Select * where {?s ?p ?o. } limit 5";
        return "Select * where {?s ?p ?o. } limit 10";
    }

}

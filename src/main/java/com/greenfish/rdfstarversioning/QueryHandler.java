package com.greenfish.rdfstarversioning;

import org.eclipse.rdf4j.query.Dataset;
import org.eclipse.rdf4j.query.algebra.DeleteData;
import org.eclipse.rdf4j.query.algebra.InsertData;
import org.eclipse.rdf4j.query.algebra.UpdateExpr;
import org.eclipse.rdf4j.query.parser.ParsedUpdate;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QueryHandler {

    static SPARQLParser parser = new SPARQLParser();

    public static String timestampQuery(String query) {
        //TODO: transform query into timestamped query via query algebra injection
        //return "Select * where {?s ?p ?o. } limit 5";
        return "Select * where {?s ?p ?o. } limit 10";
    }

    public static String timestampUpdate(String updateStmt) throws Exception {
        ParsedUpdate update = parser.parseUpdate(updateStmt, null);
        String context = "default graph";
        List<UpdateExpr> updateExprs = update.getUpdateExprs();
        UpdateExpr expr = updateExprs.get(0);
        if (expr instanceof InsertData) {
            InsertData insertdata = ((InsertData) expr);
            String dataBlock = insertdata.getDataBlock();
            Scanner scanner = new Scanner(dataBlock);
            for (int i = 0; i < insertdata.getLineNumberOffset(); i++)
                scanner.nextLine();
            StringBuilder timestampedInsertBlock = new StringBuilder();
            while (scanner.hasNext()) {
                timestampedInsertBlock.append("(").append(scanner.nextLine()).append(")\n");
            }
            return  MessageFormat.format(Utils.readAllBytes("timestampedInsertTemplate"), context, timestampedInsertBlock);

        } else if (expr instanceof DeleteData) {
            DeleteData deleteData = ((DeleteData) expr);
            String dataBlock = deleteData.getDataBlock();
            Scanner scanner = new Scanner(dataBlock);
            for (int i = 0; i < deleteData.getLineNumberOffset(); i++)
                scanner.nextLine();
            StringBuilder timestampedInsertBlock = new StringBuilder();
            while (scanner.hasNext()) {
                timestampedInsertBlock.append("(").append(scanner.nextLine()).append(")\n");
            }
            return  MessageFormat.format(Utils.readAllBytes("timestampedDeleteTemplate"), context, timestampedInsertBlock);

        }
        else throw new Exception("Update statement not covered yet");

    }


}

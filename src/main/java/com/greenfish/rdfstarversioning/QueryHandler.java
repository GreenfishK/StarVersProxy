package com.greenfish.rdfstarversioning;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.algebra.*;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.ParsedTupleQuery;
import org.eclipse.rdf4j.query.parser.ParsedUpdate;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.queryrender.sparql.experimental.SparqlQueryRenderer;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.rio.turtle.TurtleParser;

import java.io.StringReader;
import java.text.MessageFormat;
import java.util.*;

public class QueryHandler {
    //TODO: Handle queries and updates to a named graph.
    //TODO: Cover more complex queries (visit method)

    static SPARQLParser parser = new SPARQLParser();

    public static String timestampQuery(String query) throws Exception {
        ParsedQuery originalQuery =  parser.parseQuery(query, null);
        if (originalQuery instanceof ParsedTupleQuery) {
            TupleExpr queryTree = originalQuery.getTupleExpr();
            queryTree.visit(getTimestampingModel());
            originalQuery.setTupleExpr(queryTree);
            return new SparqlQueryRenderer().render(originalQuery);
        } else {
            System.out.println("There is no solution yet for timestamping queries other than tuple queries.");
            return query;
        }
    }

    private static QueryModelVisitor<Exception> getTimestampingModel() {

        return new QueryModelVisitor<Exception>() {
            int stmtCnt = 0;

            @Override
            public void meet(QueryRoot queryRoot) throws Exception {
            }

            @Override
            public void meet(Add add) throws Exception {

            }

            @Override
            public void meet(And and) throws Exception {

            }

            @Override
            public void meet(ArbitraryLengthPath arbitraryLengthPath) throws Exception {

            }

            @Override
            public void meet(Avg avg) throws Exception {

            }

            @Override
            public void meet(BindingSetAssignment bindingSetAssignment) throws Exception {

            }

            @Override
            public void meet(BNodeGenerator bNodeGenerator) throws Exception {

            }

            @Override
            public void meet(Bound bound) throws Exception {

            }

            @Override
            public void meet(Clear clear) throws Exception {

            }

            @Override
            public void meet(Coalesce coalesce) throws Exception {

            }

            @Override
            public void meet(Compare compare) throws Exception {

            }

            @Override
            public void meet(CompareAll compareAll) throws Exception {

            }

            @Override
            public void meet(CompareAny compareAny) throws Exception {

            }

            @Override
            public void meet(DescribeOperator describeOperator) throws Exception {

            }

            @Override
            public void meet(Copy copy) throws Exception {

            }

            @Override
            public void meet(Count count) throws Exception {

            }

            @Override
            public void meet(Create create) throws Exception {

            }

            @Override
            public void meet(Datatype datatype) throws Exception {

            }

            @Override
            public void meet(DeleteData deleteData) throws Exception {

            }

            @Override
            public void meet(Difference difference) throws Exception {

            }

            @Override
            public void meet(Distinct distinct) throws Exception {

            }

            @Override
            public void meet(EmptySet emptySet) throws Exception {

            }

            @Override
            public void meet(Exists exists) throws Exception {

            }

            @Override
            public void meet(Extension extension) throws Exception {

            }

            @Override
            public void meet(ExtensionElem extensionElem) throws Exception {

            }

            @Override
            public void meet(Filter filter) throws Exception {
            }

            @Override
            public void meet(FunctionCall functionCall) throws Exception {

            }

            @Override
            public void meet(Group group) throws Exception {

            }

            @Override
            public void meet(GroupConcat groupConcat) throws Exception {

            }

            @Override
            public void meet(GroupElem groupElem) throws Exception {

            }

            @Override
            public void meet(If anIf) throws Exception {

            }

            @Override
            public void meet(In in) throws Exception {

            }

            @Override
            public void meet(InsertData insertData) throws Exception {

            }

            @Override
            public void meet(Intersection intersection) throws Exception {

            }

            @Override
            public void meet(IRIFunction iriFunction) throws Exception {

            }

            @Override
            public void meet(IsBNode isBNode) throws Exception {

            }

            @Override
            public void meet(IsLiteral isLiteral) throws Exception {

            }

            @Override
            public void meet(IsNumeric isNumeric) throws Exception {

            }

            @Override
            public void meet(IsResource isResource) throws Exception {

            }

            @Override
            public void meet(IsURI isURI) throws Exception {

            }

            @Override
            public void meet(Join join) throws Exception {
                join.visitChildren(this);

            }

            @Override
            public void meet(Label label) throws Exception {

            }

            @Override
            public void meet(Lang lang) throws Exception {

            }

            @Override
            public void meet(LangMatches langMatches) throws Exception {

            }

            @Override
            public void meet(LeftJoin leftJoin) throws Exception {

            }

            @Override
            public void meet(Like like) throws Exception {

            }

            @Override
            public void meet(Load load) throws Exception {

            }

            @Override
            public void meet(LocalName localName) throws Exception {

            }

            @Override
            public void meet(MathExpr mathExpr) throws Exception {

            }

            @Override
            public void meet(Max max) throws Exception {

            }

            @Override
            public void meet(Min min) throws Exception {

            }

            @Override
            public void meet(Modify modify) throws Exception {

            }

            @Override
            public void meet(Move move) throws Exception {

            }

            @Override
            public void meet(MultiProjection multiProjection) throws Exception {

            }

            @Override
            public void meet(Namespace namespace) throws Exception {

            }

            @Override
            public void meet(Not not) throws Exception {

            }

            @Override
            public void meet(Or or) throws Exception {

            }

            @Override
            public void meet(Order order) throws Exception {

            }

            @Override
            public void meet(OrderElem orderElem) throws Exception {

            }

            @Override
            public void meet(Projection projection) throws Exception {
                projection.visitChildren(this);
            }

            @Override
            public void meet(ProjectionElem projectionElem) throws Exception {

            }

            @Override
            public void meet(ProjectionElemList projectionElemList) throws Exception {

            }

            @Override
            public void meet(Reduced reduced) throws Exception {

            }

            @Override
            public void meet(Regex regex) throws Exception {

            }

            @Override
            public void meet(SameTerm sameTerm) throws Exception {

            }

            @Override
            public void meet(Sample sample) throws Exception {

            }

            @Override
            public void meet(Service service) throws Exception {

            }

            @Override
            public void meet(SingletonSet singletonSet) throws Exception {

            }

            @Override
            public void meet(Slice slice) throws Exception {
                slice.visitChildren(this);
            }

            @Override
            public void meet(StatementPattern statementPattern) throws Exception {
                stmtCnt++;
                ValueFactory valueFactory = SimpleValueFactory.getInstance();

                Var nested = new Var();
                nested.setName("_const1_");
                nested.setValue(valueFactory.createIRI(String.format("<<<%s %s %s>> <http://example.com/metadata/versioning#valid_from> ?valid_from%s>",
                        Utils.entityToString(statementPattern.getSubjectVar()),
                        Utils.entityToString(statementPattern.getPredicateVar()),
                        Utils.entityToString(statementPattern.getObjectVar()),
                        stmtCnt)));
                //System.out.println(statementPattern.getSubjectVar().getValue().getClass());

                Var valid_until_iri = new Var();
                valid_until_iri.setName("_const_66d5ccde_uri");
                valid_until_iri.setValue(valueFactory.createIRI("http://example.com/metadata/versioning#valid_until"));
                valid_until_iri.setConstant(true);
                valid_until_iri.setAnonymous(true);

                StatementPattern stmt1 = new StatementPattern(
                        nested,
                        valid_until_iri,
                        new Var("valid_until" + stmtCnt));

                Filter timestampFilter = new Filter();
                timestampFilter.setCondition(new And(
                        new Compare( new Var("valid_from" + stmtCnt), new Var("tsBGP"), Compare.CompareOp.LE),
                        new Compare( new Var("tsBGP"), new Var("valid_until" + stmtCnt), Compare.CompareOp.LT)));
                ExtensionElem extElem = new ExtensionElem();
                extElem.setName("tsBGP");
                extElem.setExpr(new FunctionCall("NOW"));
                timestampFilter.setArg(new Extension(stmt1, extElem));

                statementPattern.replaceWith(timestampFilter);
            }

            @Override
            public void meet(Str str) throws Exception {

            }

            @Override
            public void meet(Sum sum) throws Exception {

            }

            @Override
            public void meet(Union union) throws Exception {

            }

            @Override
            public void meet(ValueConstant valueConstant) throws Exception {

            }

            @Override
            public void meet(ListMemberOperator listMemberOperator) throws Exception {

            }

            @Override
            public void meet(Var var) throws Exception {

            }

            @Override
            public void meet(ZeroLengthPath zeroLengthPath) throws Exception {

            }

            @Override
            public void meetOther(QueryModelNode queryModelNode) throws Exception {

            }
        };
    }

    public static String timestampUpdate(String updateStmt) throws Exception {
        ParsedUpdate update = parser.parseUpdate(updateStmt, null);
        String context = "default graph";
        List<UpdateExpr> updateExprs = update.getUpdateExprs();
        UpdateExpr expr = updateExprs.get(0);

        if (expr instanceof InsertData) {
            String valuesInsertBlock = prepareValueBlock(expr);
            return  MessageFormat.format(Utils.readAllBytes("timestampedInsertTemplate"), context, valuesInsertBlock);
        } else if (expr instanceof DeleteData) {
            String valuesDeleteBlock = prepareValueBlock(expr);
            return  MessageFormat.format(Utils.readAllBytes("timestampedDeleteTemplate"), context, valuesDeleteBlock);

        }
        else throw new Exception("Update statement not covered yet");

    }

    private static String prepareValueBlock(Object expr) throws Exception {
        Scanner scanner = null;
        if (expr instanceof InsertData) {
            //Get rid of first few lines with prefixes
            InsertData insertdata = ((InsertData) expr);
            String dataBlock = insertdata.getDataBlock();
            scanner = new Scanner(dataBlock);
            for (int i = 0; i < insertdata.getLineNumberOffset(); i++)
                scanner.nextLine();
        } else if (expr instanceof DeleteData) {
            DeleteData deleteData = ((DeleteData) expr);
            String dataBlock = deleteData.getDataBlock();
            scanner = new Scanner(dataBlock);
            for (int i = 0; i < deleteData.getLineNumberOffset(); i++)
                scanner.nextLine();
        } else {
            throw new Exception("Expr is neither InsertData nor DeleteData.");
        }

        //Get triple statements
        StringBuilder insertBlock = new StringBuilder();
        while (scanner.hasNext())
            insertBlock.append(scanner.nextLine());
        scanner.close();
        TurtleParser triplesParser = new TurtleParser();
        Model model = new LinkedHashModel();
        triplesParser.setRDFHandler(new StatementCollector(model));
        triplesParser.parse(new StringReader(insertBlock.toString()));

        //TODO: Fix bug with triple statements that do not end with a dot.

        //Bring triple statements into the format suitable for the VALUES block
        StringBuilder valuesBlock = new StringBuilder();
        for (Statement stmt : model) {
            valuesBlock
                    .append('(')
                    .append(Utils.entityToString(stmt.getSubject()))
                    .append(Utils.entityToString(stmt.getPredicate()))
                    .append(Utils.entityToString(stmt.getObject()))
                    .append(')').append('\n');
        }
        return valuesBlock.toString();

    }


}

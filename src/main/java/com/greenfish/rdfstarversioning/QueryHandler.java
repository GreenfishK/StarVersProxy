package com.greenfish.rdfstarversioning;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.algebra.*;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.ParsedTupleQuery;
import org.eclipse.rdf4j.query.parser.ParsedUpdate;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.queryrender.sparql.experimental.SparqlQueryRenderer;
import org.eclipse.rdf4j.repository.sail.SailTupleQuery;
import java.text.MessageFormat;
import java.util.*;

public class QueryHandler {

    static SPARQLParser parser = new SPARQLParser();

    public static String timestampQuery(String query) throws Exception {
        //TODO: transform query into timestamped query via query algebra injection
        ParsedQuery originalQuery =  parser.parseQuery(query, null);
        if (originalQuery instanceof ParsedTupleQuery) {
            TupleExpr queryTree = originalQuery.getTupleExpr();
            queryTree.visit(getTimestampingModel());
            originalQuery.setTupleExpr(queryTree);
            String transformedQuery = new SparqlQueryRenderer().render(originalQuery);

            System.out.println(queryTree);
            System.out.println(transformedQuery);

        } else {
            System.out.println("There is no solution yet for timestamping queries other than tuple queries.");
            return query;
        }

        return "Select * where {?s ?p ?o. } limit 10";
    }


    private static QueryModelVisitor<Exception> getTimestampingModel() {
        return new QueryModelVisitor<Exception>() {
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
                /*
                   Projection
                      ProjectionElemList
                         ProjectionElem "s"
                         ProjectionElem "o"
                      StatementPattern
                         Var (name=s)
                         Var (name=_const_48d4a287_uri, value=http://example.com/queries/predicate1, anonymous)
                         Var (name=o)

                 -->

                   Projection
                      ProjectionElemList
                         ProjectionElem "s"
                         ProjectionElem "o"
                         ProjectionElem "valid_from"
                         ProjectionElem "valid_until"
                         ProjectionElem "tsBGP"
                      Filter (new scope)
                         And
                            Compare (<=)
                               Var (name=valid_from)
                               Var (name=tsBGP)
                            Compare (<)
                               Var (name=tsBGP)
                               Var (name=valid_until)
                         Extension
                            Join
                               Join
                                  TripleRef
                                     Var (name=s)
                                     Var (name=_const_48d4a287_uri, value=http://example.com/queries/predicate1, anonymous)
                                     Var (name=o)
                                     Var (name=_anon_c0b09526_077d_4115_ad32_560302225777, anonymous)
                                  TripleRef
                                     Var (name=_anon_c0b09526_077d_4115_ad32_560302225777, anonymous)
                                     Var (name=_const_55df1a2a_uri, value=http://example.com/metadata/versioning#valid_from, anonymous)
                                     Var (name=valid_from)
                                     Var (name=_anon_f3c9edb8_91a2_48e0_90f4_f3200a72ea62, anonymous)
                               StatementPattern
                                  Var (name=_anon_f3c9edb8_91a2_48e0_90f4_f3200a72ea62, anonymous)
                                  Var (name=_const_66d5ccde_uri, value=http://example.com/metadata/versioning#valid_until, anonymous)
                                  Var (name=valid_until)
                            ExtensionElem (tsBGP)
                               FunctionCall (NOW)
                */
                System.out.println("meet statementPattern");
                ValueFactory valueFactory = SimpleValueFactory.getInstance();
                final Var anonymous1 = new Var("_anon_" + UUID.randomUUID().toString().replaceAll("-",
                        "_"));
                anonymous1.setAnonymous(true);
                final Var anonymous2 = new Var("_anon_" + UUID.randomUUID().toString().replaceAll("-",
                        "_"));
                anonymous1.setAnonymous(true);

                TripleRef ref1 = new TripleRef();
                ref1.setSubjectVar(new Var(statementPattern.getSubjectVar().getName()));
                ref1.setPredicateVar(new Var(statementPattern.getPredicateVar().getName()));
                ref1.setObjectVar(new Var(statementPattern.getObjectVar().getName()));
                ref1.setExprVar(anonymous1);

                TripleRef ref2 = new TripleRef();
                ref2.setSubjectVar(anonymous1);
                Var predicateVar2 = new Var();
                predicateVar2.setName("_const_55df1a2a_uri");
                predicateVar2.setValue(valueFactory.createIRI("http://example.com/metadata/versioning#valid_from"));
                predicateVar2.setConstant(true);
                predicateVar2.setAnonymous(true);
                ref2.setPredicateVar(predicateVar2);
                ref2.setObjectVar(new Var("valid_from"));
                ref2.setExprVar(anonymous2);

                Var predicateVar3 = new Var();
                predicateVar3.setName("_const_66d5ccde_uri");
                predicateVar3.setValue(valueFactory.createIRI("http://example.com/metadata/versioning#valid_until"));
                predicateVar3.setConstant(true);
                predicateVar3.setAnonymous(true);
                StatementPattern stmt1 = new StatementPattern(anonymous2, predicateVar3, new Var("valid_until"));

                Filter timestampFilter = new Filter();
                timestampFilter.setCondition(new And(
                        new Compare( new Var("valid_from"), new Var("tsBGP"), Compare.CompareOp.LE),
                        new Compare( new Var("tsBGP"), new Var("valid_until"), Compare.CompareOp.LT)));
                ExtensionElem extElem = new ExtensionElem();
                extElem.setName("tsBGP");
                extElem.setExpr(new FunctionCall("NOW"));
                timestampFilter.setArg(new Extension(new Join(new Join(ref1, ref2), stmt1),extElem));

                //StatementPattern test = new StatementPattern(new Var("s"), new Var("p"), new Var("o"));
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

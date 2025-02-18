package tools.refinery.geometric_refinery;

import tools.refinery.logic.dnf.Query;
import tools.refinery.logic.literal.Literals;
import tools.refinery.store.map.Version;
import tools.refinery.store.model.Interpretation;
import tools.refinery.store.model.Model;
import tools.refinery.store.model.ModelStore;
import tools.refinery.store.model.ModelStoreBuilder;
import tools.refinery.store.model.internal.ModelStoreBuilderImpl;
import tools.refinery.store.query.interpreter.QueryInterpreterAdapter;
import tools.refinery.store.query.resultset.ResultSet;
import tools.refinery.store.query.view.FunctionView;
import tools.refinery.store.representation.Symbol;
import tools.refinery.store.tuple.Tuple;

import java.util.List;
import java.util.Random;

public class UnitDistanceGraphCalculator {
    public static void main(String[] args) {
        // Symbols to use in the problem (i.e. the labels in the graph)
        Symbol<Coordinate> coordinate = new Symbol<>("coordinate", 1, Coordinate.class, null);
        Symbol<Boolean> edge = new Symbol<>("edge", 2, Boolean.class, false);
        var coordinateView = new FunctionView<>(coordinate);

        var q = Query.of("twoNodesAreConnected", (builder, n1, n2) -> builder.clause(
                Coordinate.class, Coordinate.class, (c1, c2) -> List.of(
                        n1.notEquivalent(n2),
                        coordinateView.call(n1, c1),
                        coordinateView.call(n2, c2),
                        Literals.check(new DistanceComparisonTerm(c1, c2))
                )));

        // Create the infrastructure to build several graphs with the symbols
        ModelStoreBuilder builder = new ModelStoreBuilderImpl();
        ModelStore store = builder
                .symbols(coordinate, edge)
                .with(QueryInterpreterAdapter.builder()
                        .queries(q)
                ).build();

        // Create a single graph from the store
        Model model = store.createEmptyModel();
        Interpretation<Coordinate> coordinateInterpretation = model.getInterpretation(coordinate);
        Interpretation<Boolean> edgeInterpretation = model.getInterpretation(edge);
        QueryInterpreterAdapter queryAdapter = model.getAdapter(QueryInterpreterAdapter.class);
        ResultSet<Boolean> resultSet = queryAdapter.getResultSet(q);

        Version lastVersion = model.commit();

        Random r = new Random(0);
        for (int i = 0; i < 100; i++) {
            int newEdges = AddNodes(r, coordinateInterpretation, edgeInterpretation, queryAdapter, resultSet);
            System.out.println("new edges = " + newEdges);
            if (newEdges > 5) {
                lastVersion = model.commit();
            } else {
                model.restore(lastVersion);
            }
        }
    }

    private static int AddNodes(Random r,
                                Interpretation<Coordinate> coordinateInterpretation,
                                Interpretation<Boolean> edgeInterpretation,
                                QueryInterpreterAdapter queryAdapter,
                                ResultSet<Boolean> resultSet) {

        for (int i = 0; i < 1000; i++) {
            Tuple node = Tuple.of(i);
            Coordinate c = new Coordinate(r.nextDouble() * 2 - 1, r.nextDouble() * 2 - 1);
            coordinateInterpretation.put(node, c);
        }
        queryAdapter.flushChanges();
        var cursor = resultSet.getAll();

        int edgesAdded = resultSet.size();
        System.out.println(edgesAdded);
        return edgesAdded;
    }
}

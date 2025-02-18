package tools.refinery.geometric_refinery;

import tools.refinery.store.map.Version;
import tools.refinery.store.model.Interpretation;
import tools.refinery.store.model.Model;
import tools.refinery.store.model.ModelStore;
import tools.refinery.store.model.ModelStoreBuilder;
import tools.refinery.store.model.internal.ModelStoreBuilderImpl;
import tools.refinery.store.representation.Symbol;
import tools.refinery.store.tuple.Tuple;

import java.util.Random;

public class UnitDistanceGraphCalculator {
    public static void main(String[] args) {
        // Symbols to use in the problem (i.e. the labels in the graph)
        Symbol<Coordinate> coordinate = new Symbol<>("coordinate", 1, Coordinate.class, null);
        Symbol<Boolean> edge = new Symbol<>("edge", 2, Boolean.class, false);

        // Create the infrastructure to build several graphs with the symbols
        ModelStoreBuilder builder = new ModelStoreBuilderImpl();
        ModelStore store = builder.symbols(coordinate, edge).build();

        // Create a single graph from the store
        Model model = store.createEmptyModel();
        Interpretation<Coordinate> coordinateInterpretation = model.getInterpretation(coordinate);
        Interpretation<Boolean> edgeInterpretation = model.getInterpretation(edge);

        Version lastVersion = model.commit();

        Random r = new Random(0);
        for (int i = 0; i < 100; i++) {
            int newEdges = AddNodes(r, coordinateInterpretation, edgeInterpretation);
            System.out.println("new edges = " + newEdges);
            if (newEdges > 5) {
                lastVersion = model.commit();
            } else {
                model.restore(lastVersion);
            }
        }
    }

    private static int AddNodes(Random r, Interpretation<Coordinate> coordinateInterpretation, Interpretation<Boolean> edgeInterpretation) {
        int edgesAdded = 0;
        for (int i = 0; i < 1000; i++) {
            Tuple node = Tuple.of(i);
            Coordinate c = new Coordinate(r.nextDouble() * 2 - 1, r.nextDouble() * 2 - 1);

            var cursor = coordinateInterpretation.getAll();
            while (cursor.move()) {
                Tuple otherNode = cursor.getKey();
                Coordinate otherC = cursor.getValue();

                if (c.isUnitDistance(otherC)) {
                    edgeInterpretation.put(Tuple.of(node.get(0), otherNode.get(0)), true);
                    System.out.println("huhu él " + node.get(0) + " " + otherNode.get(0));
                    edgesAdded++;
                }
            }
            coordinateInterpretation.put(node, c);
        }
        return edgesAdded;
    }
}

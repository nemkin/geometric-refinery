package tools.refinery.geometric_refinery;

import tools.refinery.logic.term.cardinalityinterval.CardinalityInterval;

public record Coordinate(
        double xMin, double xMax,
        double yMin, double yMax) {
}

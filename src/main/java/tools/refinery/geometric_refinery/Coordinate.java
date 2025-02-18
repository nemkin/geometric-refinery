package tools.refinery.geometric_refinery;

import static java.lang.Math.abs;

public record Coordinate(double x, double y) {

    double squareDistance(Coordinate other)
    {
        return (other.x-this.x)*(other.x-this.x) + (other.y-this.y)*(other.y-this.y);
    }

    boolean isUnitDistance(Coordinate other)
    {
        var sqD = this.squareDistance(other);
        double eps = 0.001;
        return (abs(sqD-1) <= eps);
    }
}

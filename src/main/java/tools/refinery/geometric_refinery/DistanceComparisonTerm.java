package tools.refinery.geometric_refinery;

import tools.refinery.logic.substitution.Substitution;
import tools.refinery.logic.term.BinaryTerm;
import tools.refinery.logic.term.Term;

public class DistanceComparisonTerm  extends BinaryTerm<Boolean, Coordinate, Coordinate> {

    public DistanceComparisonTerm(Term<Coordinate> left, Term<Coordinate> right) {
        super(Boolean.class, Coordinate.class , Coordinate.class, left, right);
    }

    @Override
    protected Boolean doEvaluate(Coordinate coordinate, Coordinate coordinate2) {
        return coordinate.isUnitDistance(coordinate2);
    }

    @Override
    public Term<Boolean> doSubstitute(Substitution substitution, Term<Coordinate> term, Term<Coordinate> term1) {
        return new DistanceComparisonTerm(term, term1);
    }
}

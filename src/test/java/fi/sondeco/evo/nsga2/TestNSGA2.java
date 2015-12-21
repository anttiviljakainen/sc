package fi.sondeco.evo.nsga2;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.sondeco.evo.ConstraintFunction;
import fi.sondeco.evo.ObjectiveFunction;
import fi.sondeco.evo.XSquaredObjectiveFunction;
import fi.sondeco.matrix.Matrix;

public class TestNSGA2 {

  @Test
  public void test() {
    NSGA2 nsga2 = new NSGA2(10);
    
    double nm = 20;
    double mutationProbability = 0.9;
    double crossoverProbability = 0.9;
    int maxIterations = 100;
    double[] maxBounds = new double[] { 1, 1 };
    double[] minBounds = new double[] { -1, -1 };
    int dimension = 2;
    List<ObjectiveFunction> objectives = new ArrayList<ObjectiveFunction>();
    objectives.add(new XSquaredObjectiveFunction());
    
    List<ConstraintFunction> constraints = new ArrayList<ConstraintFunction>();
    
    nsga2.minimize(objectives, constraints, dimension, minBounds, maxBounds, maxIterations, crossoverProbability, mutationProbability, nm);
  }

  @Test
  public void testConstr() {
    NSGA2 nsga2 = new NSGA2(10);
    
    double nm = 20;
    double mutationProbability = 0.9;
    double crossoverProbability = 0.9;
    int maxIterations = 100;
    double[] maxBounds = new double[] { 1, 5 };
    double[] minBounds = new double[] { 0.1, 0 };
    int dimension = 2;
    List<ObjectiveFunction> objectives = new ArrayList<ObjectiveFunction>();
    objectives.add(new ObjectiveFunction() {
      public double f(Matrix x) {
        return x.get(0, 0);
      }
    });
    objectives.add(new ObjectiveFunction() {
      public double f(Matrix x) {
        return (1 + x.get(0, 1)) / x.get(0, 0);
      }
    });
    
    List<ConstraintFunction> constraints = new ArrayList<ConstraintFunction>();
    constraints.add(new ConstraintFunction() {
      public boolean isFeasible(Matrix x) {
        return x.get(0, 1) + 9 * x.get(0, 0) >= 6;
      }

      public double violation(Matrix x) {
        return x.get(0, 1) + 9 * x.get(0, 0) - 6;
      }
    });

    constraints.add(new ConstraintFunction() {
      public boolean isFeasible(Matrix x) {
        return -x.get(0, 1) + 9 * x.get(0, 0) >= 1;
      }

      public double violation(Matrix x) {
        return -x.get(0, 1) + 9 * x.get(0, 0) - 1;
      }
    });
    
    nsga2.minimize(objectives, constraints, dimension, minBounds, maxBounds, maxIterations, crossoverProbability, mutationProbability, nm);
  }
  
}

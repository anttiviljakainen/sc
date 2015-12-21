package fi.sondeco.evo.nsga2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fi.sondeco.evo.ConstraintFunction;
import fi.sondeco.evo.Crossover;
import fi.sondeco.evo.EA;
import fi.sondeco.evo.Mutation;
import fi.sondeco.evo.ObjectiveFunction;
import fi.sondeco.matrix.Matrix;

public class NSGA2 {

  public NSGA2(int populationSize) {
    this.populationSize = populationSize;
    // Recycling list for domination counts
    dominateCount = new int[populationSize * 2];
    
    // Recycling lists for dominations of fronts
    for (int i = 0; i < populationSize * 2; i++)
      dominatedLists.add(new HashSet<Integer>());
    
    // Recycling list for fronts
    for (int i = 0; i < populationSize * 2; i++)
      fronts.add(new HashSet<Integer>());
  }
  
  public void minimize(
      List<ObjectiveFunction> objectives, List<ConstraintFunction> constraints, 
      int dimension, double[] minBounds, double[] maxBounds, int maxIterations, 
      double crossoverProbability, double mutationProbability, double nm) {
    
    Matrix population = generateInitialPopulation(dimension, populationSize, minBounds, maxBounds);
    
    for (int iteration = 0; iteration < maxIterations; iteration++) {
      // Generate offspring
      Matrix generation = nextGeneration(population, populationSize, minBounds, maxBounds, 
          crossoverProbability, mutationProbability, nm);
      
      // Find the best fronts and solutions 
      
      dominationSort(objectives, constraints, generation, populationSize, fronts, dominatedLists, dominateCount);
      
      // Reduce population + offspring down to new population of original size
      
      population = new Matrix(0, dimension);
      
      int front = 0;
      
      for (; front < fronts.size(); front++) {
        if (population.getRows() + fronts.get(front).size() > populationSize)
          break;
        
        for (Integer i : fronts.get(front)) {
          population.appendRows(generation.row(i));
        }
      }
      
      /**
       * Fill the population by selecting the solutions in the next 
       * front that preserve the least crowded areas. 
       */
      if (population.getRows() < populationSize) {
        Set<Integer> cdSelection = crowdingDistanceSelection(objectives, generation, fronts.get(front), dominateCount, populationSize - population.getRows());
        
        for (Integer i : cdSelection) {
          population.appendRows(generation.row(i));
        }
      }
      
      resetLists();
    }
    
    System.out.println(population.toString());
  }
  
  private Set<Integer> crowdingDistanceSelection(List<ObjectiveFunction> objectives, Matrix generation, 
      Set<Integer> frontIndividuals, int[] dominateCount, int select) {

    List<CrowdingDistance> crowding = new ArrayList<CrowdingDistance>();
    for (Integer i : frontIndividuals) {
      CrowdingDistance cd = new CrowdingDistance();
      cd.solution = i;
      crowding.add(cd);
    }
    
    for (ObjectiveFunction f : objectives) {
      double min = Double.POSITIVE_INFINITY;
      double max = Double.NEGATIVE_INFINITY;
      
      for (CrowdingDistance cd : crowding) {
        cd.fVal = f.f(generation.row(cd.solution));
        
        min = Math.min(min, cd.fVal);
        max = Math.max(max, cd.fVal);
      }

      Collections.sort(crowding, new Comparator<CrowdingDistance>() {
        public int compare(CrowdingDistance o1, CrowdingDistance o2) {
          if (o1.fVal < o2.fVal)
            return -1;
          if (o1.fVal > o2.fVal)
            return 1;
          return 0;
        }
      });

      // Boundary solutions to infinity
      CrowdingDistance first = crowding.get(0);
      first.crowdingDistance = Double.POSITIVE_INFINITY;
      CrowdingDistance last = crowding.get(crowding.size() - 1);
      last.crowdingDistance = Double.POSITIVE_INFINITY;
      
      for (int i = 1; i < crowding.size() - 1; i++) {
        CrowdingDistance prev = crowding.get(i - 1);
        CrowdingDistance cd = crowding.get(i);
        CrowdingDistance next = crowding.get(i + 1);
        
        cd.crowdingDistance += (next.fVal - prev.fVal) / (max - min);
      }
    }

    Collections.sort(crowding, new Comparator<CrowdingDistance>() {
      public int compare(CrowdingDistance o1, CrowdingDistance o2) {
        if (o1.crowdingDistance > o2.crowdingDistance)
          return -1;
        if (o1.crowdingDistance < o2.crowdingDistance)
          return 1;
        return 0;
      }
    });

    // Select the top n solutions
    
    Set<Integer> selection = new HashSet<Integer>();

    for (int i = 0; i < select; i++) {
      selection.add(crowding.get(i).solution);
    }

    return selection;
  }

  private void dominationSort(List<ObjectiveFunction> objectives, List<ConstraintFunction> constraints, Matrix generation, 
      int populationSize, List<Set<Integer>> fronts, List<Set<Integer>> dominatedLists, int[] dominateCount) {
    Set<Integer> all = new HashSet<Integer>();
    
    for (int i = 0; i < generation.getRows(); i++) {
      all.add(i);
      
      for (int j = 0; j < generation.getRows(); j++) {
        if (i == j)
          continue;

        if (dominates(objectives, constraints, generation, i, j))
          dominatedLists.get(i).add(j);
        else
          dominateCount[i]++;
      }
      
      // if individual i is not dominated by 
      // anything, it's on the first front
      if (dominateCount[i] == 0) {
        fronts.get(0).add(i);
        all.remove(i);
      }
    }
    
    int front = 0;
    for (; front < fronts.size(); front++) {
      if (fronts.get(front).isEmpty())
        break;

      for (Integer better : fronts.get(front)) {
        // for each dominated solution in the front
        for (Integer dominated : dominatedLists.get(better)) {
          dominateCount[dominated]--;
          
          // If the solution is no dominated by none 
          // other, it belongs to the next front
          if (dominateCount[dominated] == 0) {
            fronts.get(front + 1).add(dominated);
            all.remove(dominated);
          }
        }
      }
    }
    
    // Add the rest of the items to the next fronts
    
    for (Integer i : all)
      fronts.get(front).add(i);
  }

//  private void dominationSort(List<ObjectiveFunction> objectives, Matrix generation, 
//      int populationSize, List<Set<Integer>> fronts, List<Set<Integer>> dominatedLists, int[] dominateCount) {
//    for (int i = 0; i < generation.getRows(); i++) {
//      for (int j = 0; j < generation.getRows(); j++) {
//        if (i == j)
//          continue;
//
//        if (!dominates(objectives, generation, i, j))
//          dominateCount[i]++;
//      }
//    }
//
//    for (int i = 0; i < dominateCount.length; i++) {
//      fronts.get(dominateCount[i]).add(i);
//    }
//  }

  private boolean dominates(List<ObjectiveFunction> objectives, List<ConstraintFunction> constraints, Matrix generation, int i, int j) {
    boolean dominates = true;
    boolean equal = true;

    Matrix vector1 = generation.row(i);
    Matrix vector2 = generation.row(j);
    
    // If we have constraints, we do constraint domination check
    if ((constraints != null) && (!constraints.isEmpty())) {
      for (ConstraintFunction constraint : constraints) {
        boolean feasible1 = constraint.isFeasible(vector1);
        boolean feasible2 = constraint.isFeasible(vector2);
        
        // i feasible, j infeasible
        if (feasible1 && !feasible2)
          return true;

        // i infeasible, j feasible
        if (!feasible1 && feasible2)
          return false;
        
        // both infeasible so check for smaller violation
        if (feasible1 == feasible2 == false) {
          double vio1 = constraint.violation(vector1);
          double vio2 = constraint.violation(vector2);
          
          return vio1 < vio2;
        }
      }
    }
    
    for (ObjectiveFunction f : objectives) {
      double val1 = f.f(vector1);
      double val2 = f.f(vector2);
      
      if (val1 > val2) {
        dominates = false;
        break;
      }

      equal = equal && val1 == val2;
    }
    
    return dominates && !equal;
  }

  private Matrix nextGeneration(Matrix population, int populationSize, double[] minBounds, double[] maxBounds, 
      double crossoverProbability, double mutationProbability, double nm) {
    Matrix nextGen = (Matrix) population.clone();
    nextGen.addRows(nextGen.getRows(), populationSize);

    int[] indices = EA.permutedIndices(populationSize);

    /**
     * simulated binary crossover (SBX) operator and 
     * polynomial mutation [6] 
     * for real-coded GAs.
     */
    
    for (int i = 1; i < populationSize; i += 2) {
      int parent1Index = indices[i - 1];
      int parent2Index = indices[i];

      /**
       * Crossover
       */
      
      double[] parent1 = new double[nextGen.getColumns()];
      double[] parent2 = new double[nextGen.getColumns()];
      
      for (int c = 0; c < nextGen.getColumns(); c++) {
        parent1[c] = nextGen.get(parent1Index, c);
        parent2[c] = nextGen.get(parent2Index, c);
      }
      
      double[][] offspring = Crossover.naiveCrossover(parent1, parent2, getRandom());
      
      /**
       * Mutation
       */
      double[] offspring1 = Mutation.polynomialMutation(offspring[0], minBounds, maxBounds, nm, mutationProbability, rand);
      double[] offspring2 = Mutation.polynomialMutation(offspring[1], minBounds, maxBounds, nm, mutationProbability, rand);
      
      for (int c = 0; c < nextGen.getColumns(); c++) {
        nextGen.set(populationSize + i - 1, c, offspring1[c]);
        nextGen.set(populationSize + i, c, offspring2[c]);
      }
    }
    
    return nextGen;
  }
  
  private Matrix generateInitialPopulation(int dimension, int populationSize,
      double[] minBounds, double[] maxBounds) {
    Matrix population = Matrix.rnd(populationSize, dimension, getRandom());
    
    for (int r = 0; r < population.getRows(); r++) {
      for (int c = 0; c < population.getColumns(); c++) {
        double range = maxBounds[c] - minBounds[c];
        
        population.set(r, c, minBounds[c] + population.get(r, c) * range);
      }
    }
    
    return population;
  }

  private void resetLists() {
    for (int i = 0; i < dominateCount.length; i++)
      dominateCount[i] = 0;
    
    for (Set<Integer> s : dominatedLists)
      s.clear();
    
    for (Set<Integer> s : fronts)
      s.clear();
  }

  public Random getRandom() {
    return rand;
  }

  public void setRandomSeed(long seed) {
    rand = new Random(seed);
  }
  
  class CrowdingDistance {
    int solution;
    double fVal;
    double crowdingDistance;
  }
  
  private Random rand = new Random();
  private int populationSize;
  
  /** 
   * We allocate the memory for these lists in advance to try 
   * minimize the need for memory allocation during algorithm.
   */
  private int[] dominateCount;
  private List<Set<Integer>> dominatedLists = new ArrayList<Set<Integer>>();
  private List<Set<Integer>> fronts = new ArrayList<Set<Integer>>();
}

package fi.sondeco.machinelearning;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GridOptimizer {

  private int populationSize;
  private int lineCount;
  private int maxIterations;
  private double mutationProbability;
  private double crossoverProbability;
  private BitSetCostFunction costf;

  public GridOptimizer(int populationSize, int lineCount, int maxIterations, 
      double crossoverProbability, double mutationProbability, BitSetCostFunction costf) {
    this.populationSize = populationSize;
    this.lineCount = lineCount;
    this.maxIterations = maxIterations;
    this.crossoverProbability = crossoverProbability;
    this.mutationProbability = mutationProbability;
    this.costf = costf;
  }
  
  private List<BitSet> initialPopulation() {
    List<BitSet> population = new ArrayList<BitSet>(populationSize);
    for (int i = 0; i < populationSize; i++)
      population.add(randBitSet());
    
    return population;
  }
  
  private BitSet randBitSet() {
    BitSet bs = new BitSet(lineCount);
    
    Random r = new Random();

    for (int i = 0; i < 16; i++)
      bs.set(r.nextInt(lineCount));
    
//    for (int i = 0; i < lineCount; i++)
//      bs.set(i, r.nextBoolean());
    
    return bs;
  }
  

  public List<BitSet> minimize() {
    List<BitSet> population = initialPopulation();
    
    for (int i = 0; i < maxIterations; i++) {
      List<BitSet> newPopulation = new ArrayList<BitSet>(populationSize * 2);
      permute(population);

      for (int j = 0; j + 1 < population.size(); j = j + 2) {
        BitSet parent1 = population.get(j);
        BitSet parent2 = population.get(j + 1);
        
        crossover(parent1, parent2, newPopulation);
      }
  
      mutate(newPopulation);
  
      newPopulation.addAll(population);
      
      population = selection(newPopulation);
      
      System.out.println(String.format("Average cost: %f", averageCost(population)));
    }
    
    return population;
  }

  private double averageCost(List<BitSet> population) {
    double sum = 0;
    for (BitSet bs : population)
      sum += costf.cost(bs);
    return sum / population.size();
  }

  private void crossover(BitSet parent1, BitSet parent2, List<BitSet> newPopulation) {
    Random r = getRandom();
    
    BitSet child1;
    BitSet child2;
    if (r.nextDouble() < crossoverProbability) {
      int len = lineCount;
      int start = r.nextInt(len);
  //    int end = r.nextInt(len - start) + start;
  //
  //    BitSet mask1 = new BitSet(len);
  //    mask1.set(0, start);
      
      child1 = (BitSet) parent2.clone();
      child2 = (BitSet) parent1.clone();
      
      child1.clear(0, start);
      child2.clear(0, start);
      
      child1.or(parent1.get(0, start));
      child2.or(parent2.get(0, start));
      
      
  //    for (int i = 0; i < 16; i++) {
  //      child1.set(r.nextInt(len) + 1);
  //      child2.set(r.nextInt(len) + 1);
  //    }
    } else {
      child1 = (BitSet) parent1.clone();
      child2 = (BitSet) parent2.clone();
    }
    newPopulation.add(child1);
    newPopulation.add(child2);
  }

  private List<BitSet> selection(List<BitSet> population) {
    permute(population);

    for (int i = population.size() - 2; i >= 0; i -= 2) {
      BitSet bitset1 = population.get(i);
      BitSet bitset2 = population.get(i + 1);
      
      double costBS1 = costf.cost(bitset1);
      double costBS2 = costf.cost(bitset2);
      
      if (costBS1 < costBS2)
        population.remove(bitset2);
      else
        population.remove(bitset1);
    }
    
    return population;
  }

//  private double cost(BitSet bitset) {
//    int dimlen = bitset.length() / 2;
//    BitSet vertical = bitset.get(0, dimlen);
//    BitSet horizontal = bitset.get(dimlen, bitset.length());
//    
//    // Orthogonality
//    
//    
//    return 
//        Math.pow(8 - vertical.cardinality(), 2) + 
//        Math.pow(8 - horizontal.cardinality(), 2);
//  }

  private void mutate(List<BitSet> population) {
    Random r = getRandom();
    
    for (int i = 0; i < population.size(); i++) {
      BitSet bitSet = population.get(i);
      for (int j = 0; j < bitSet.length(); j++) {
        if (r.nextDouble() < mutationProbability) {
          bitSet.flip(j);
        }
      }
    }
  }

  private void permute(List<BitSet> population) {
    Collections.shuffle(population, getRandom());
//    
//    Random r = getRandom();
//    for (int i = 0; i < population.size(); i++) {
//      
//      
//      int newIndex = r.nextInt(population.size());
//      BitSet element = population.remove(i);
//      population.add(newIndex, element);
//    }
//    return population;
  }

  private Random getRandom() {
    return new Random();
  }
  
}

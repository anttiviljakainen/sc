package fi.sondeco.machinelearning;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GridOptimizer2 {

  private int lineCount;
  private int min;
  private int max;
  private BitSetCostFunction costf;

  public GridOptimizer2(int lineCount, int min, int max, 
      BitSetCostFunction costf) {
    this.lineCount = lineCount;
    this.min = min;
    this.max = max;
    this.costf = costf;
  }
  
  public int minimize() {
    double minCost = Double.MAX_VALUE;
    int minIndex = min;
    for (int val = min; val <= max; val++) {
      double cost = cost(val);
      if (cost < minCost) {
        minIndex = val;
        minCost = cost;
      }
      
      System.out.println(String.format("Cost (%d): %f", val, cost(val)));
    }

    return minIndex;
  }

  private double cost(int val) {
    BitSet bitSet = BitSet.valueOf(new long[] { val });
      
    return costf.cost(bitSet);
  }

}

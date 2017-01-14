package fi.sondeco.opt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NelderMead implements Optimizer {

  
  public double[] minimize(OptimizationJob job) {
    ObjectiveFunction f = job.getObjectiveFunction();
    double[] initialGuess = job.getInitialGuess();
    
    double alpha = 1;
    double phi = 2;
    double p = 0.5;
    double sigma = 0.5;
    int dimension = initialGuess.length;
    
    List<NMTestPoint> points = new ArrayList<NMTestPoint>();
    
    NMTestPoint x0 = new NMTestPoint(dimension);
    System.arraycopy(initialGuess, 0, x0.point, 0, dimension);
    x0.fVal = f.f(x0.point);
    points.add(x0);
    
    for (int i = 0; i < dimension; i++) {
      NMTestPoint tp = new NMTestPoint(dimension);
      System.arraycopy(initialGuess, 0, tp.point, 0, dimension);
      
      double step = x0.point[i] == 0 ? 0.00025 : 0.05;

      tp.point[i] += step;
      tp.fVal = f.f(tp.point);
      
      points.add(tp);
    }
    
    // TODO Termination criteria
    int iter = 1;
    while (iter < 10) {
      iter++;
      points.sort(new Comparator<NMTestPoint>() {
        public int compare(NMTestPoint o1, NMTestPoint o2) {
          if (o1.fVal < o2.fVal)
            return -1;
          if (o2.fVal > o1.fVal)
            return 1;
          return 0;
        }
      });
  
      double[] centroid = new double[initialGuess.length];
      
      for (int i = 0; i < points.size() - 1; i++) {
        NMTestPoint tp = points.get(i);
        
        for (int j = 0; j < tp.point.length; j++)
          centroid[j] += tp.point[j];
      }
      
      for (int j = 0; j < centroid.length; j++)
        centroid[j] /= centroid.length;
      
      NMTestPoint reflected = new NMTestPoint(dimension);
      NMTestPoint best = points.get(0);
      NMTestPoint worst = points.get(points.size() - 1);
      NMTestPoint secondWorst = points.get(points.size() - 2);
      
      for (int i = 0; i < dimension; i++)
        reflected.point[i] = centroid[i] + alpha * (centroid[i] - worst.point[i]);
      reflected.fVal = f.f(reflected.point);
      
      if ((best.fVal <= reflected.fVal) && (reflected.fVal < secondWorst.fVal)) {
        points.remove(points.size() - 1);
        points.add(reflected);
        continue;
      } else {
        if (reflected.fVal < best.fVal) {
          NMTestPoint expanded = new NMTestPoint(dimension);
          for (int i = 0; i < dimension; i++) {
            expanded.point[i] = reflected.point[i] + phi * (reflected.point[i] - best.point[i]);
          }
          
          expanded.fVal = f.f(expanded.point);
          
          points.remove(points.size() - 1);
          if (expanded.fVal < reflected.fVal)
            points.add(expanded);
          else
            points.add(reflected);
          
          continue;
        } else {
          NMTestPoint contracted = new NMTestPoint(dimension);
          for (int i = 0; i < dimension; i++)
            contracted.point[i] = best.point[i] + p * (worst.point[i] - best.point[i]);
          contracted.fVal = f.f(contracted.point);
          
          if (contracted.fVal < worst.fVal) {
            points.remove(points.size() - 1);
            points.add(contracted);
            continue;
          } else {
            for (int i = 1; i < points.size(); i++) {
              NMTestPoint tp = points.get(i);
              for (int j = 0; j < dimension; j++)
                tp.point[j] = best.point[j] + sigma * (tp.point[j] - best.point[j]);
            }
          }
        }
      }
    }
    
    return points.get(0).point;
  }
  
  
  class NMTestPoint {
    public NMTestPoint(int dimension) {
      this.point = new double[dimension];
    }
    
    double[] point;
    double fVal;
  }
}

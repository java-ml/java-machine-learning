package cn.hebeuit.perceptron;

import cn.hebeuit.base.DenseVector;

import java.util.HashMap;

public class Perceptron {
  protected DenseVector w;
  protected double b=0;
  protected double n=1;

    public Perceptron(DenseVector w, double b, double n) {
      this.w = w;
      this.b = b;
      this.n = n;
    }
  public void  set(DenseVector w, double b, double n) {
    this.w = w;
    this.b = b;
    this.n = n;
  }
    public void Train(HashMap<DenseVector,Byte> map,int iterNum){
            DenseVector minw=new DenseVector(w.size(),0.0);
            double      minb=0;
            double      mincost=Double.MAX_VALUE;
            while (iterNum-- >0){
               for(DenseVector vector:map.keySet()){
                 while (map.get(vector)*(w.Mul(vector)+b)<=0){
                   w.UpdateAdd(vector.Mul(n*map.get(vector)));
                   b+=n*map.get(vector);
                 }
               }
               double cost=Cost(map);
               if(cost<0.00001)break;
               else if(cost<mincost){
                 minw.copy(w);
                 minb=b;
               }
            }
           w.copy(minw);
           b=minb;
    }
    public double  Predicted(DenseVector vector){
      return  Math.signum(w.Mul(vector)+b);
    }

    protected double Cost(HashMap<DenseVector,Byte> map){
      double cost=0;
      for(DenseVector vec: map.keySet())
      cost+=Math.abs(w.Mul(vec)+b-map.get(vec));
      return cost;
    }

  @Override
  public String toString() {
    return "Perceptron{" +
            "w=" + w +
            ", b=" + b +
            '}';
  }
}

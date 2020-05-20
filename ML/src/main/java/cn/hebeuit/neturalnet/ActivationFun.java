package cn.hebeuit.neturalnet;

public interface ActivationFun {

   default  double Fun(double x) {
      return 1/(1+Math.pow(Math.E,-x));
   }
}

package cn.hebeuit.adaboost;

import cn.hebeuit.base.DenseVector;
import cn.hebeuit.perceptron.Perceptron;
import cn.hebeuit.perceptron.PerceptronDual;

public class BaseG extends PerceptronDual {
    public BaseG(DenseVector w, double b, double n) {
        super(w, b, n);
    }
    public void UpdataB(double value){
        b-=value;
    }
    public double getB(){
        return b;
    }
}

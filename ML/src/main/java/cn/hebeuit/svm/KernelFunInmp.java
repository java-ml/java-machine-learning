package cn.hebeuit.svm;

import cn.hebeuit.base.DenseVector;

public class KernelFunInmp {
    private static class Polynomial implements KernelFunction{
       int p;
       public Polynomial(int p){
           this.p=p;
       }
        @Override
        public double K(DenseVector x1, DenseVector x2) {
            return Math.pow(x1.Mul(x2)+1,p);
        }
    }
    private static class Gaussian implements  KernelFunction{
        int p;
        public Gaussian(int p){
            this.p=p;
        }
        @Override
        public double K(DenseVector x1, DenseVector x2) {
            return Math.exp(-x1.LpDistance(x2,2)/(2*p*p));
        }
    }
    public static  Polynomial getPolynomial(int p){
        return new Polynomial(p);
    }
    public static Gaussian getGaussian(int p){
        return new Gaussian(p);
    }
}

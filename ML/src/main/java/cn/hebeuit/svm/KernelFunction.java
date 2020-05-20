package cn.hebeuit.svm;

import cn.hebeuit.base.DenseVector;

public interface KernelFunction {
     double K(DenseVector x1,DenseVector x2);
}

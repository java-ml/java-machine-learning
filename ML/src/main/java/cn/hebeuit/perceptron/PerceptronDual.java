package cn.hebeuit.perceptron;

import cn.hebeuit.base.DenseVector;

import java.util.HashMap;
import java.util.Set;

public class PerceptronDual  extends Perceptron{


    public PerceptronDual(DenseVector w, double b, double n) {
        super(w, b, n);
    }

    @Override
    public void Train(HashMap<DenseVector, Byte> map,int iter) {
        Set<DenseVector> set =map.keySet();
        int size=set.size();
        DenseVector dense[]=new DenseVector[size];
        set.toArray(dense);
        double [][] val=new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j=0;j<size;j++){
                val[i][j]=dense[i].Mul(dense[j]);
            }
        }
        while (iter-- > 0) {
            double add ;
            for (int i = 0; i < size; i++) {
                while (true) {
                    add=0;
                    for (int j = 0; j < size; j++) {
                        add += w.getAt(j) * map.get(dense[j]) * val[j][i];
                    }
                    if ((add + b) * map.get(dense[i]) <= 0) Updata(map.get(dense[i]), i);
                    else break;
                }
            }
        }
        DenseVector tmp=new DenseVector(dense[0].size(),0.0);
        for (int i = 0,row=tmp.size(); i <row ; i++) {
            double add=0;
            for (int j=0;j<dense.length;j++){
                add+=dense[j].getAt(i)*w.getAt(j)*map.get(dense[j]);
            }
            tmp.setAt(i,add);
        }

        w=tmp;

    }

    private void Updata(double yi,int i){
        b+=n*yi;
        w.setAt(i,w.getAt(i)+n);
    }
}

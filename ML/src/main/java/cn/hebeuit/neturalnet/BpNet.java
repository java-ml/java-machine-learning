package cn.hebeuit.neturalnet;

import cn.hebeuit.base.DenseVector;

import java.util.HashMap;

public class BpNet {
    InputShell  in;
    HideShell   hide;
    OutputShell out;
    int i, h, o;
    public BpNet(int i,int h,int o,ActivationFun fun){
        in =new InputShell(i);
        hide=new HideShell(h,i,fun);
        out =new OutputShell(o,h,fun);
        this.i=i;this.h=h;this.o=o;
    }
    public DenseVector Predicted(DenseVector x){
        return out.getY(hide.getBh(x));
    }
    public  void Train(HashMap<DenseVector,DenseVector>map,double k,int iter) {

        DenseVector gj, bh, yj, y, eh;
        while (iter-- > 0) {
            for (DenseVector xi : map.keySet()) {

                in.x.copy(xi);

                bh = hide.getBh(in.x);
                yj = out.getY(bh);
                gj = new DenseVector(o, 0.0);
                y = map.get(xi);
                for (int j = 0; j < o; j++) {
                    gj.setAt(j, yj.getAt(j) * (1 - yj.getAt(j)) * (y.getAt(j) - yj.getAt(j)));
                }
                eh = new DenseVector(h, 0.0);
                for (int j = 0; j < h; j++) {
                    double add = 0;
                    for (int l = 0; l < o; l++) {
                        add += out.getWhj(j, l) * gj.getAt(l);
                    }
                    eh.setAt(j, bh.getAt(j) * (1 - bh.getAt(j)) * add);
                }

                for (int j = 0; j < o; j++) {
                    for (int hi = 0; hi < h; hi++) {
                        out.setWhj(hi, j, k * gj.getAt(j) * bh.getAt(hi));
                    }
                }

                for (int hi = 0; hi < h; hi++) {
                    for (int j = 0; j < i; j++)
                        hide.setV(j, hi, k * eh.getAt(hi) * xi.getAt(j));
                }
                out.setOj(gj.Mul(-k));

                hide.setOj(eh.Mul(-k));
            }

        }
    }
}

class InputShell{
    DenseVector x;
    InputShell(int n){
        x=new DenseVector(n,0.5);
    }
}
class HideShell{
    DenseVector bh,v[],oj;
    private ActivationFun fun;
    HideShell(int n,int m,ActivationFun fun){
        bh=new DenseVector(n,0.0);
        oj=new DenseVector(n,0.5);
        v=new DenseVector[n];
        this.fun=fun;
        for (int i = 0; i <n ; i++) {
            v[i]=new DenseVector(m,0.5);
        }
    }

    public DenseVector getBh(DenseVector x) {
        for (int i = 0,l=bh.size(); i < l; i++) {
            bh.setAt(i,fun.Fun(v[i].Mul(x)-oj.getAt(i)));
        }

        return bh;
    }
    public  void setV(int i,int h,double value){
        v[h].setAt(i,v[h].getAt(i)+value);
    }
    public void setOj(DenseVector vector){
        oj.UpdateAdd(vector);
    }
}
class OutputShell{
    private DenseVector y,w[],oj;
    private ActivationFun fun;
    OutputShell(int n,int m,ActivationFun fun){
        y=new DenseVector(n,0.0);
        oj=new DenseVector(n,0.5);
        w=new DenseVector[n];
        this.fun=fun;
        for (int i = 0; i <n ; i++) {
            w[i]=new DenseVector(m,0.5);
        }
    }
    public DenseVector getY(DenseVector bh) {


        for (int i = 0,l=y.size(); i < l; i++) {
            y.setAt(i,fun.Fun(w[i].Mul(bh)-oj.getAt(i)));
        }
        return y;
    }
    public double getWhj(int h,int j){
        return w[j].getAt(h);
    }
    public void  setWhj(int h,int j,double value){
        w[j].setAt(h,w[j].getAt(h)+value);
    }
    public void setOj(DenseVector value){
        oj.UpdateAdd(value);
    }
}

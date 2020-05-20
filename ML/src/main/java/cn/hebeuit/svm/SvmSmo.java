package cn.hebeuit.svm;

import cn.hebeuit.base.DenseVector;

import java.util.Arrays;
import java.util.HashMap;

public class SvmSmo {
    private KernelFunction kernel=KernelFunInmp.getGaussian(3);
    private DenseVector []xi;
    private Byte yi[];
    private double ai[],b=0;
    private int size;
    private double C=5.5;
    public void Train(HashMap<DenseVector,Byte> map){
        size=map.size();
        xi=new DenseVector[size];
        map.keySet().toArray(xi);
        yi=new Byte[size];
        map.values().toArray(yi);
        ai=new double[size];
        Arrays.fill(ai,0);
        double b1,b2,old2,old,e1,e2,L,H;
        int j=6000;
        do {
            for (int i = 0; i <size-1; i+=1) {
                double n=kernel.K(xi[i],xi[i+1])*-2+kernel.K(xi[i],xi[i])+kernel.K(xi[i+1],xi[i+1]);

                old2=ai[i+1];old=ai[i];
                if(yi[i]!=yi[i+1]){L=Math.max(0,old2-old);H=Math.min(C,C+old2-old);}
                else {L=Math.max(0,old2+old-C);H=Math.min(C,old2+old);}
                e1=(F(xi[i])-yi[i]);e2=(F(xi[i+1])-yi[i+1]);

                ai[i+1]+=yi[i+1]*(e1-e2)/n;
                if(ai[i+1]>H)ai[i+1]=H;
                else if(ai[i+1]<L)ai[i+1]=L;

                ai[i]+=yi[i]*yi[i+1]*(old2-ai[i+1]);

                b1=-e1-yi[i]*(ai[i]-old)*kernel.K(xi[i],xi[i])-yi[i+1]*kernel.K(xi[i+1],xi[i])*(ai[i+1]-old2)+b;
                b2=-e2-yi[i+1]*kernel.K(xi[i],xi[i+1])*(ai[i]-old)-yi[i+1]*kernel.K(xi[i+1],xi[i+1])*(ai[i+1]-old2)+b;
                if(ai[i]>0&&ai[i]<2.0)b=b1;
                else if(ai[i+1]>0&&ai[i+1]<2.0)b=b2;
                else b=(b1+b2)/2;
            }
        }while (j-- >0);
     for (int i=0;i<ai.length;i++) System.out.println(ai[i]);
    }

    private double F(DenseVector x){
        double y=0;
        for (int i = 0; i <size ; i++) {
            y+=ai[i]*yi[i]*kernel.K(xi[i],x);
        }
        return  (y+b);
    }
    public int Fun(DenseVector x){
        double y=0;
        for (int i = 0; i <size ; i++) {
            y+=ai[i]*yi[i]*kernel.K(xi[i],x);
        }
        if(y+b<-0.001) return -1;
        else if(y+b>0.001)return 1;
        else return 0;
    }

    public  void print(){
        for (int i = 0; i < ai.length; i++) {
            System.out.println(ai[i]);
        }
    }
}







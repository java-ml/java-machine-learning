package cn.hebeuit.adaboost;

import cn.hebeuit.base.DenseVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AdaBoost {
    private ArrayList<Double>am=new ArrayList<>();
    private ArrayList<BaseG> Gm=new ArrayList<>();
    private double wi[];
    private Double getEm(HashMap<DenseVector,Byte> map,BaseG G){

        int i=0;
        double e=0;
        for (DenseVector x:map.keySet()) {
            if(G.Predicted(x)!=map.get(x))
            e+=wi[i];
            i++;
        }
        return e;
    }
    private void setAm(double em){

        if(em==0)am.add(1.0);
         else am.add(0.5*Math.log((1-em)/em));
    }
    private void setWi(HashMap<DenseVector,Byte> map){

        if(Gm.isEmpty()){setWi(map.size());return;}
        double zm=0,a=am.get(am.size()-1);
        BaseG G=Gm.get(Gm.size()-1);
        int i=0;
        for (DenseVector x:map.keySet()){
            zm+=wi[i]*Math.exp(-a*map.get(x)*G.Predicted(x));
            wi[i]=wi[i]*Math.exp(-a*map.get(x)*G.Predicted(x));
            i++;
        }
        for ( i = 0; i <wi.length ; i++) {
            wi[i]/=zm;
        }
    }
    private void setWi(double n){

        for (int i=0;i<wi.length;i++){
            wi[i]=1/n;
        }
    }
    public double G(DenseVector x){
        double v=0;
        for (int i=0,m=Gm.size();i<m;i++)v+=am.get(i)*Gm.get(i).Predicted(x);
        return Math.signum(v);
    }
    public void Train(HashMap<DenseVector,Byte> map,int vs,double n){
        double  tmp=0,em=0;
        wi=new double[map.size()];
        int i=15;
        Random random=new Random();
        while (FalsePoint(map)>0.1&& i-- >0){
            BaseG g=new BaseG(new DenseVector(vs,random.nextDouble()+0.1),random.nextDouble(),n);
            setWi(map);
            g.Train(map,20);
            while ( Math.abs( (tmp=getEm(map,g))-em )>0.01){
                g.UpdataB(tmp-em);
                em=tmp;
            }
            setAm(em);
            Gm.add(g);
        }
    }
    private double FalsePoint(HashMap<DenseVector,Byte> map){
        if(Gm.isEmpty())return 1;
        double f=0;
        for (DenseVector x:map.keySet()){
            if(G(x)!=map.get(x))f+=1;
        }
        return f/map.size();
    }
}

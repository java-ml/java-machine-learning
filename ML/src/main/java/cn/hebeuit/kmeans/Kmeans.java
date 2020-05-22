package cn.hebeuit.kmeans;

import cn.hebeuit.base.DenseVector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class Kmeans {
    
     public static class Cluster{
         HashSet<DenseVector> xi=new HashSet<>();
         DenseVector centre;
         public Cluster(DenseVector x){
             centre=new DenseVector(x.size(),0.0);
             centre.copy(x);
         }
         private double getCentreLpDistance(DenseVector x){
             return x.LpDistance(centre,2);
         }
         private void updata(){
             if(!xi.isEmpty()) {
                 centre.UpdateMul(0.0);
                 for (DenseVector x : xi) {
                     centre.UpdateAdd(x);
                 }

                 centre.UpdateMul(1.0 / xi.size());
             }
         }

         public DenseVector getCentre() {
             return centre;
         }

         public HashSet<DenseVector> getXi() {
             return xi;
         }
     }
     private Cluster cluster[];
     public void Train(LinkedList<DenseVector> xi,int csize){
         boolean run=true;
         cluster=new Cluster[csize];
         Collections.sort(xi, new Comparator<DenseVector>() {
             @Override
             public int compare(DenseVector o1, DenseVector o2) {
                 return (int) (o1.getMod()-o2.getMod());
             }
         });
         for (int i=0,size=xi.size();i<csize;i++){
             cluster[i]=new Cluster(xi.get((i*(size/csize))%size));
         }
         while (run){
             run=false;
             for (DenseVector x:xi){
                 if(put(x))run=true;
             }
             resetCenter();
         }
     }

     private boolean put(DenseVector x){
         Cluster min = null,in=null;
         double minl=Double.MAX_VALUE,xl;
         for (Cluster c:cluster){
             if(c.xi.contains(x)){in=c;}
             xl=c.getCentreLpDistance(x);
             if(xl<minl){
                 minl=xl;
                 min=c;
             }
         }
         if(in==min){
             return false;
         }else {
             if(in!=null)in.xi.remove(x);
             min.xi.add(x);
             return true;
         }
     }
     private void resetCenter(){
         for (Cluster c:cluster){
             c.updata();
         }
     }

    public Cluster[] getCluster() {
        return cluster;
    }
}

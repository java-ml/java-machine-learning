package cn.hebeuit.kdtree;

import cn.hebeuit.base.DenseVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class NodeComparator implements Comparator<DenseVector>{
    private int row=0;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int compare(DenseVector o1, DenseVector o2) {
        return (int) (o1.getAt(row)-o2.getAt(row));
    }
}
public class KdTree {
    private NodeComparator comparator=new NodeComparator();
    private DenseVector min;
    private double R=Double.MAX_VALUE,ml=Double.MAX_VALUE;
    public DenseVector getMinVec() {
        return min;
    }

    public void MakeTree(ArrayList<DenseVector> vector, DenseVector [] ary, int k , int row){
        if(vector==null||vector.size()==0)return;
        double mo=0;int m=0, n=0;
        comparator.setRow(row);
        Collections.sort(vector,comparator);
        m=vector.size()/2;
        ArrayList<DenseVector> L=new ArrayList<>(),R=new ArrayList<>();
        for (int i=0,l=vector.size();i<l;i++){
            if(i<m){
                L.add(vector.get(i));
            }
            if(i>m){
                R.add(vector.get(i));
            }
        }


        if(k==1)ary[1]=vector.get(m);
        else if(k<ary.length) {
                ary[k]=vector.get(m);
        }
        MakeTree(L,ary,2*k,(row+1)%ary[1].size());
        MakeTree(R,ary,2*k+1,(row+1)%ary[1].size());
    }
    public void Find(DenseVector []vectors,int k,int row){
            if(k<1||k>=vectors.length)return;
            if(vectors[k].getAt(row)>vectors[0].getAt(row)){
                Find(vectors,2*k,(row+1)%vectors[0].size());
            }else {
                Find(vectors,2*k+1,(row+1)%vectors[0].size());
            }
            ml=vectors[0].LpDistance(vectors[k],2.0);
             if(ml<R) {
                     min=vectors[k];
                     R=ml;
             }
             if(k==1)return;

             if((k&1) == 1){
                 ml=vectors[0].LpDistance(vectors[k-1],2.0);
                 if(ml<R) {
                     min=vectors[k-1];
                     R=ml;
                     Find(vectors,k-1,0);
                 }

             }else {
                 ml=vectors[0].LpDistance(vectors[k+1],2.0);
                 if(ml<R) {
                     min=vectors[k+1];
                     R=ml;
                     Find(vectors,k+1,0);
                 }
             }
    }
}

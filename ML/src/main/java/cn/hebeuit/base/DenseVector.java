package cn.hebeuit.base;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class DenseVector {
    private ArrayList<Double> vec;
    public   DenseVector(int size,Double initnum){
        vec =new ArrayList<>(size);
        for (int i = 0; i <size ; i++) {
            vec.add(initnum);
        }
    }
    public DenseVector(Double ... num){
        vec =new ArrayList<>(num.length);
        for (int i = 0; i <num.length ; i++) {
            vec.add(num[i]);
        }
    }
    public DenseVector(int size){
        vec =new ArrayList<>(size);
    }
    public Double getAt(int i){
        return vec.get(i);
    }
    public int size(){return  vec.size();}
    public void setAt(int i,Double t){vec.set(i,t);}
    public DenseVector  getSection(int start,int end){
        DenseVector  den=new DenseVector(end-start+1);
        for (int i=start;i<=end;i++)den.add(this.vec.get(i));
        return den;
    }
    private void  add(Double num){
        vec.add(num);
    }
    public  DenseVector  Add(DenseVector dense){
        int len=vec.size();
        DenseVector den=new DenseVector(len);
        for (int i = 0; i <len ; i++) {
            den.add(vec.get(i)+dense.getAt(i));
        }
        return  den;
    }
    public  DenseVector  UpdateAdd(DenseVector dense){
        int len=vec.size();
        for (int i = 0; i <len ; i++) {
            this.setAt(i,vec.get(i)+dense.getAt(i));
        }
        return  this;
    }

    public  DenseVector  AddOrSubNum(double num){
        int len=vec.size();
        DenseVector  den=new DenseVector (len);
        for (int i = 0; i <len ; i++) {
            den.add(vec.get(i)+num);
        }
        return  den;
    }
    public  DenseVector  UpdateAddOrSubNum(double num){
        int len=vec.size();
        for (int i = 0; i <len ; i++) {
            this.setAt(i,vec.get(i)+num);
        }
        return  this;
    }
    public  double Mul(DenseVector num){
        int len=vec.size();
        double mul=0.0;

        for (int i = 0; i <len ; i++) {
           mul+=(vec.get(i)*num.getAt(i));
        }
        return  mul;
    }

    public  DenseVector Mul(double num){
        int len=vec.size();
        DenseVector den=new DenseVector(len);
        for (int i = 0; i <len ; i++) {
            den.add(vec.get(i)*num);
        }
        return  den;
    }
    public  DenseVector UpdateMul(double num){
        int len=vec.size();

        for (int i = 0; i <len ; i++) {
            this.setAt(i,vec.get(i)*num);
        }
        return  this;
    }
    public  double Avg(){
        int len=vec.size();
        double mul=0.0;
        for (int i = 0; i <len ; i++) {
            mul+=(vec.get(i));
        }
        return  mul/len;
    }
    public  long VecLen(){return  vec.size();}
    public  DenseVector  Sub(DenseVector dense){
        int len=vec.size();
        DenseVector den=new DenseVector(len);
        for (int i = 0; i <len ; i++) {
            den.add(vec.get(i)-dense.getAt(i));
        }
        return  den;
    }
    public  DenseVector  UpdateSub(DenseVector dense){
        int len=vec.size();
        for (int i = 0; i <len ; i++) {
            this.setAt(i,vec.get(i)-dense.getAt(i));
        }
        return  this;
    }
    public void copy(DenseVector dense){
        vec.clear();
        for (int i = 0,l=dense.size(); i < l; i++) {
           vec.add(dense.getAt(i));
        }
    }
    public  double LpDistance(DenseVector vector,double p){
        double add=0;
        for (int i = 0,l=size(); i <l ; i++) {
            add+=Math.pow(Math.abs(getAt(i)-vector.getAt(i)),p);
        }
        return Math.pow(add,1/p);
    }
    public  double LpDistance(DenseVector vector){
        double add=0;
        for (int i = 0,l=size(); i <l ; i++) {
            add+= Math.abs(getAt(i)-vector.getAt(i));
        }
        return add;
    }
    @Override
    public String toString() {
        return   vec.toString() ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenseVector that = (DenseVector) o;
        return Objects.equals(vec, that.vec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vec);
    }
}

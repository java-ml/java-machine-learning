import org.apache.spark.ml.linalg
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{Row, SparkSession}

import scala.reflect.ClassTag

case class Image[T <:AnyVal](data: Array[T],width:Int,height:Int,nChannels:Int,label:Int=0)
class Convolution extends Serializable{
  import scala.util.Random
  import org.apache.spark.ml.linalg.Vectors
  private var kernel:Array[Array[Array[Array[Byte]]]]=null
  private var kw:Int=3
  private var kh:Int=3
  private var k:Int=1
  def convolution[T<:AnyVal](imageArray: Array[T],w:Int,h:Int,d:Int,label:Int=0,ws:Int=1,hs:Int=1): Image[Int] ={
    val ch=(h-kh)/hs+1
    val cw=(w-kw)/ws+1
    val conv:Array[Int]=Array.ofDim[Int](cw*ch*k)
    var value:Int=0;
    if(imageArray.isInstanceOf[Array[Byte]]) {
     for(i<-0 until k) {
          for (hi<-0  until ch;wi<- 0  until cw){
            for(kd<-0 until d){
              for(khi<-0 until kh;kwi <-0 until kw) {
                value=getImgBGR (conv,cw,ch,k,wi,hi,i)+kernel(i)(kd)(kwi)(khi)*(getImgBGR(imageArray, w, h, d, kwi + ws * wi, khi + hs * hi, kd).asInstanceOf[Byte]&255)
                setImgBGR(conv,cw,ch,k,wi,hi,i,value)
              }
            }
          }
     }
    }else if(imageArray.isInstanceOf[Array[Int]]) {
      for(i<-0 until k) {
        for (hi<-0  until ch;wi<- 0  until cw){
          for(kd<-0 until d){
            for(khi<-0 until kh;kwi <-0 until kw) {
              value=getImgBGR (conv,cw,ch,k,wi,hi,i)+kernel(i)(kd)(kwi)(khi)*(getImgBGR(imageArray, w, h, d, kwi + ws * wi, khi + hs * hi, kd)).asInstanceOf[Int]
              setImgBGR(conv,cw,ch,k,wi,hi,i,value)
            }
          }
        }
    }
    }
    Image[Int](conv,cw,ch,k,label)
  }

  def initKernel(w:Int=3,h:Int=3,nChannels:Int=3,k:Int=1): Unit ={
    this.k=k
    this.kw=w
    this.kh=h
    var temp:Int=0
    var len=0
    kernel=new Array[Array[Array[Array[Byte]]]](k)
    for(i<- 0 until k) {
      kernel(i) = Array.ofDim[Byte](nChannels, w, h)
      for (di <- 0 until nChannels) {
        kernel(i)(di)(w/2)(h/2)=5
        len=math.max(w,h)/2
        for(li<- 1 to len){
            temp=h/2+li
            if(temp<h)kernel(i)(di)(w/2)(temp) = -1
            temp=h/2-li
            if(temp>=0)kernel(i)(di)(w/2)(temp) = -1
            temp=w/2+li
            if(temp<w)kernel(i)(di)(temp)(h/2) = -1
            temp=w/2-li
            if(temp>=0)kernel(i)(di)(temp)(h/2) = -1
          }
        }
    }
  }
  private def getImgBGR[T<:AnyVal](imageArray: Array[T],w:Int,h:Int,d:Int=1,wi:Int,hi:Int,di:Int=1):T={
    imageArray( di+wi*d+hi*w*d)
  }
  private def setImgBGR[T<:AnyVal](imageArray: Array[T],w:Int,h:Int,d:Int=1,wi:Int,hi:Int,di:Int=1,value:T):Unit ={
    imageArray(di+wi*d+hi*w*d)=value.asInstanceOf[T]
  }
  def BytetoVectors(image:Array[Int]):linalg.Vector={
    val data:Array[Double]=image.map(x=>x/255.0)
    Vectors.dense(data)
  }
  def resize (data:Array[Byte],w:Int,h:Int,d:Int,neww:Int,newh:Int,label:Int=0): Image[Byte] ={
    val rdata=new Array[Byte](newh*neww*d)
    var kh=0
    var kw=0
    if(w<neww) kw=(neww-w)>>1
    if(h<newh) kh=(newh-h)>>1
    for (di<-0 until d;wi<-0 until w;hi<- 0 until h  if (wi<neww && hi  < newh ))
    setImgBGR(rdata,neww,newh,d,wi+kw,hi+kh,di,getImgBGR(data,w,h,d,wi,hi,di))
    Image(rdata,neww,newh,d,label)
  }

  def Pooling2D (data:Array[Int],w:Int,h:Int,d:Int,label:Int=0,sw:Int=2,sh:Int=2,model:String="avg"): Image[Int] ={
    val kw=w/sw
    val kh=h/sh
    val pdata=Array.ofDim[Int](kw*kh*d)
    var temp = 0
      model match {
        case "avg" =>for(di<-0 until d;wi<-0 until kw;hi<-0 until kh ){
          temp=0
          for (swi<- 0 until sw;shi <- 0 until sh){
            temp+=getImgBGR(data,w,h,d,wi*sw+swi,hi*sh+shi,di)
          }
          setImgBGR(pdata,kw,kh,d,wi,hi,di,(temp/(sw*sh)))
        }
        case "max"=> for(di<-0 until d;wi<-0 until kw;hi<-0 until kw){
          temp=Int.MinValue
          for (swi<- 0 until sw;shi <- 0 until sh){
            temp=Math.max(temp,getImgBGR(data,w,h,d,wi*sw+swi,hi*sh+shi,di))
          }
          setImgBGR(pdata,kw,kh,d,wi,hi,di,temp)
        }
      }
    Image(pdata,kw,kh,d,label)
  }
 }

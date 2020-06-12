import org.apache.spark.ml.linalg
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{Row, SparkSession}
//返回卷积后的图片特征，高度，宽度，通道数
case class Image(data: Array[Byte],height:Int,width:Int,nChannels:Int)
class Convolution extends Serializable{
  import scala.util.Random
  import org.apache.spark.ml.linalg.Vectors
  private var kernel:Array[Array[Array[Array[Byte]]]]=null
  private var kw:Int=3
  private var kh:Int=3
  private var k:Int=1
  //imageArray：由spark 图片数据源生成的GBR数据   w:宽     h:高 d:通道数 ws（hs）:水平(垂直)方向步长
  def convolution(imageArray: Array[Byte],w:Int,h:Int,d:Int=3,ws:Int=1,hs:Int=1): Image ={
    val ch=(h-kh)/hs+1
    val cw=(w-kw)/ws+1
    val conv:Array[Byte]=Array.ofDim[Byte](cw*ch*k)
    var value:Int=0;
     for(i<-0 until k) {
          for (hi<-0  until ch;wi<- 0  until cw){
            for(kd<-0 until d){
              for(khi<-0 until kh;kwi <-0 until kw) {
                value=(getImgBGR(conv,cw,ch,k,wi,hi,i)&255)+(kernel(i)(kd)(kwi)(khi)&255)*(getImgBGR(imageArray,w,h,d,kwi+ws*wi,khi+hs*hi,kd)&255)
                setImgBGR(conv,cw,ch,k,wi,hi,i,value.toByte)
              }
            }
          }
     }
    Image(conv,ch,cw,k)
  }
  //随机初始化卷积核 宽，高，通道数，卷积核数量
  def initKernel(w:Int=3,h:Int=3,nChannels:Int=3,k:Int=1): Unit ={
    this.k=k
    this.kw=w
    this.kh=h
    kernel=new Array[Array[Array[Array[Byte]]]](k)
    for(i<- 0 until k) {
      kernel(i) = Array.ofDim[Byte](nChannels, w, h)
      for (di <- 0 until nChannels; hi <- 0 until h; wi <- 0 until w) {
        kernel(i)(di)(wi)(hi) = (Random.nextInt() % 3).toByte
      }
    }

  }
  //通过线性图片数组数据获取指定像素数据 图像数组，宽度，高度，通道数，指定宽坐标，指定高坐标，指定通道
  def getImgBGR(imageArray: Array[Byte],w:Int,h:Int,d:Int=1,wi:Int,hi:Int,di:Int=0):Byte ={
    imageArray(di+wi*d+hi*w*d)
  }
  def setImgBGR(imageArray: Array[Byte],w:Int,h:Int,d:Int=1,wi:Int,hi:Int,di:Int=0,value:Byte):Unit ={
    imageArray(di+wi*d+hi*w*d)=value
  }
  //将图像数组转换为向量
  def BytetoVectors(image:Array[Byte]):linalg.Vector={
    val data:Array[Double]=image.map(x=>(x&255).toDouble)
    Vectors.dense(data)
  }
}

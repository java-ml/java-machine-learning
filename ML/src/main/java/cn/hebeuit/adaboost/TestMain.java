package cn.hebeuit.adaboost;

import cn.hebeuit.base.DenseVector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class TestMain {
    static Byte y1=-1;
    static Byte y2=1;
    /*static DenseVector y1=new DenseVector(0.0);
      static DenseVector y2=new DenseVector(1.0);
    */
    public static void main(String[] args) {

        HashMap<DenseVector,Byte>map=new HashMap<>();
        LinkedList<DenseVector>xi=new LinkedList<>();
        int i=200;
        Random random=new Random(50);
        while (i-- > 0) {
            if(i<90) {
                DenseVector x1 = new DenseVector(Math.abs(random.nextInt()%50+80)*1.0, Math.abs(random.nextInt()%50+50)*1.0);
                map.put(x1, y1);

            }else {
                DenseVector x1 = new DenseVector(Math.abs(random.nextInt()%50+200)*1.0, Math.abs(random.nextInt()%50+i+90)*1.0);
                map.put(x1, y2);
            }
        }

        AdaBoost boost=new AdaBoost();
        boost.Train(map,map.size(),0.25);
        Img(map,boost);
    }

    public static void Img(HashMap<DenseVector,Byte>map,AdaBoost boost){

       try {

           BufferedImage image = new BufferedImage(600,600,BufferedImage.TYPE_3BYTE_BGR);
           image = new BufferedImage(400,400,BufferedImage.TYPE_3BYTE_BGR);
           Graphics g=image.createGraphics();
           for (DenseVector x: map.keySet()){
               if(map.get(x)!=-1)
                   g.setColor(Color.magenta);
               else g.setColor(Color.blue);
                   g.fillOval(x.getAt(0).intValue(),x.getAt(1).intValue(),4,4);
           }
           ImageIO.write(image, "png", new File("00.png"));
           DenseVector vector=new DenseVector(0.0,0.0);
          for (int i = 0; i < 400; i++) {
               for (int j = 0; j <400; j++) {
                   vector.setAt(0,i*1.0);
                   vector.setAt(1,j*1.0);
                    double k=boost.G(vector);
                    if(k==-1)g.setColor(Color.blue);
                    else  g.setColor(Color.magenta);
                   g.fillOval(i,j,2,2);
               }
           }
           ImageIO.write(image, "png", new File("01.png"));
       } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
   }
}
//5866427
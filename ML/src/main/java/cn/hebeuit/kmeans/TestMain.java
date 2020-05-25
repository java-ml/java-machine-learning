package cn.hebeuit.kmeans;

import cn.hebeuit.base.DenseVector;
import cn.hebeuit.svm.SvmSmo;

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
                //map.put(x1, y1);
                xi.add(x1);
            }else {
                DenseVector x1 = new DenseVector(Math.abs(random.nextInt()%50+200)*1.0, Math.abs(random.nextInt()%50+i+90)*1.0);
               // map.put(x1, y2);
                xi.add(x1);
            }
        }
        Kmeans kmeans=new Kmeans();
        kmeans.Train(xi,4);
        Kmeans.Cluster []c=kmeans.getCluster();
        Img(c);
    }
    public static void Img(Kmeans.Cluster c[]){

        try {
            BufferedImage image = new BufferedImage(600,600,BufferedImage.TYPE_3BYTE_BGR);

            image = new BufferedImage(400,400,BufferedImage.TYPE_3BYTE_BGR);
            Color color[]={Color.blue,Color.CYAN,Color.green,Color.MAGENTA,Color.ORANGE,Color.RED,Color.WHITE};
            Graphics g=image.createGraphics();
            int i=0;
            for (Kmeans.Cluster cluster:c) {
                g.setColor(color[i++ % color.length]);
                for (DenseVector x : cluster.getXi()) {
                    g.fillOval(x.getAt(0).intValue(), x.getAt(1).intValue(), 4, 4);
                }
                DenseVector centre=cluster.getCentre();
                g.setColor(new Color(105+i*5, 105+i*6, i*15));
                g.fillRect(centre.getAt(0).intValue(),centre.getAt(1).intValue(),5,5);
            }
            ImageIO.write(image, "png", new File("/00.png"));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
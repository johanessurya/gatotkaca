package graphics;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

/**
 * Class <code>ImageMan</code> adalah class untuk menghandel manipulasi 
 * gambar(Image Manipulation).
 * @author Yohanes Surya
 */
public class ImageMan {
    private GraphicsConfiguration gc;

    /**
     * Membuat object <code>ImageMan</code>
     * @param gc berguna dalam membuat gambar yang kompetibel dengan 
     * monitor(8-bit, 16-bit, 32-bit)
     */
    public ImageMan(GraphicsConfiguration gc){
    	this.gc=gc;
    }
     
    /**
     * Membuat gambar transparan
     * @param img gambar yang akan di transparankan
     * @param transparancy index transparansi 0-1
     * @return gambar baru
     */
    public static Image drawTransparent(Image img, float transparancy){
		Image newImg;
    	//Change Image to blank image
		newImg=new BufferedImage(img.getWidth(null),img.getHeight(null),
				BufferedImage.TRANSLUCENT);
		Graphics2D g=(Graphics2D)newImg.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparancy));
		g.drawImage(img,0,0,null);
		g.dispose();

    	return newImg;
    }

    /**
     * Untuk mencerminkan gambar
     * @param image gambar yang akan di cerminkan
     * @return gambar baru
     */
    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }

    /**
     * Untuk membalikkan(flip) gambar
     * @param image gambar yang akan di balik
     * @return gambar baru
     */
    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }

    private Image getScaledImage(Image image, float x, float y) {
        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.TRANSLUCENT);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
    
    /**
     * Mendapatkan hex warna tiap pixel dan disimpan dalam array(Matrix)
     * @param image gambar yang akan diambil pixelnya
     * @return pixel dalam bentuk hex yang disimpan di array 2D(Matrix)
     */
	private static int[][] getPixelMatrix(Image image){
		int width,height;
		width=image.getWidth(null);
		height=image.getHeight(null);
		
		int[] pixel=new int[width*height];
		int[][] matrix=new int[width][height];
		PixelGrabber pg=new PixelGrabber(image,0,0,image.getWidth(null),image.getHeight(null),pixel,0,image.getWidth(null));
		try {
			pg.grabPixels(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int row=0;
		for(int i=0; i<pixel.length; i++){
			matrix[i % image.getWidth(null)][row]=pixel[i];
			if((i+1) % image.getWidth(null)==0)
			{
				row++;
			}
		}
		
		return matrix;
	}
	
	/**
	 * Mendapatkan kotak paling kecil untuk suatu gambar. Berguna untuk mendeteksi
	 * tumbukan antar object. Dalam game pada umumnya pendeteksian tumbukan lebih
	 * komplek dan membutuhkan spesifikasi komputer yang cepat tetapi dalam game
	 * ini saya hanya mendeteksi tumbukan dengan membuat kotak batuan dalam 
	 * pendeteksian tumbukan. Method ini berguna untuk membuat kotak bantuan yang
	 * nantinya akan berguna dalam pendeteksian tumbukan
	 * 
	 * @param image gambar yang akan di cari kotak yang paling kecil
	 * @return kotak yang siap untuk pengecheckan tumbukan nantinya.
	 */
	public static Rectangle getFitRectangle(Image image){
		int[][] pixel2d=getPixelMatrix(image);
	
		Point pos=new Point(0,0);
		Point dimension=new Point(0,0);
		
		//Get y
		for(int y=0; y<pixel2d[0].length; y++)
		{
			int isFound=0;
			for(int x=0; x<pixel2d.length; x++)
				if(pixel2d[x][y]!=0)
				{
					isFound=y;
					break;
				}
			pos.y=isFound;
			if(isFound!=0)break;
		}
		
		//Get x 
		for(int x=0; x<pixel2d.length; x++)
		{
			int isFound=0;
			for(int y=0; y<pixel2d[0].length; y++)
				if(pixel2d[x][y]!=0)
				{
					isFound=x;
					break;
				}
			pos.x=isFound;
			if(isFound!=0)break;
		}

		//Get y dimension
		for(int y=pixel2d[0].length-1; y>0; y--)
		{
			int isFound=0;
			for(int x=0; x<pixel2d.length; x++)
				if(pixel2d[x][y]!=0)
				{
					isFound=y;
					break;
				}
			dimension.y=isFound;
			if(isFound!=0)break;
		}
		
		//Get x dimension
		for(int x=pixel2d.length-1; x>0; x--)
		{
			int isFound=0;
			for(int y=0; y<pixel2d[0].length; y++)
				if(pixel2d[x][y]!=0)
				{
					isFound=x;
					break;
				}
			dimension.x=isFound;
			if(isFound!=0)break;
		}

		Rectangle rect=new Rectangle(pos.x,pos.y,dimension.x-pos.x,dimension.y-pos.y);
		
		return rect;
	}
}

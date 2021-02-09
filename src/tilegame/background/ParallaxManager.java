package tilegame.background;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

public class ParallaxManager {
    public ParallaxManager(){}
    
    public void addParallax(Image img, float speed){
        parallaxs.add(new Parallax(img,speed,null));
    }
    
    public void addParallax(Parallax p){
    	parallaxs.add(p);
    }
    
    public void update(int elapsedTime){
        for(int i=0; i<parallaxs.size(); i++){
            parallaxs.get(i).update(elapsedTime);
        }
    }
    
    public void draw(Graphics2D g){
        for(int i=0; i<parallaxs.size(); i++){
            parallaxs.get(i).draw(g);
        }        
    }
    
    private ArrayList<Parallax> parallaxs=new ArrayList<Parallax>();
}

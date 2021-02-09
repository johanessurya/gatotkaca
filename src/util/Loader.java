package util;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import tilemap.ResourceManager;
import tilemap.TileMap;

/**
 * Class <code>Loader</code> adalah class abstrak yang akan mengload resource.
 * Untuk menggunakan class ini harus menggunakan subclass nya yang dibagi menjadi 2.
 * <code>Default</code> dan <code>External</code>
 * @author Yohanes Surya
 */
public abstract class Loader {
	/**
	 * <code>ResourceManager</code> berguna untuk subclassnya
	 */
	protected ResourceManager resourceManager;

	/**
	 * Membuat object baru
	 * @param simpleSection <code>SimpleSection<code> yang ditetapkan
	 * @param resourceManager <code>ResourceManager<code> yang ditetapkan
	 */
	public Loader(SimpleSection simpleSection, 
			ResourceManager resourceManager){
		this.simpleSection=simpleSection;
		
		this.resourceManager=resourceManager;
		this.sectionResource=null;
		loadSectionResource();
	}
	
	private void loadSectionResource(){
		//Get simple section
		SimpleSection simple=getSimpleSection();
		
		//Create SectionResource Class
		//Construct Title and Player(JT/GK)
		SectionResource resource=
			new SectionResource(simple.getTitle(),simple.isJabangTetuka());
		
		//Add Opening
		ArrayList<String> temp=simple.getOpening();
		for(int i=0; i<temp.size(); i++){
			Image comic=loadImage(temp.get(i));
			if(comic==null)return;
			resource.addOpening(comic);				
		}

		//Add Map
		temp=simple.getMaps();
		for(int i=0; i<temp.size(); i++){
			TileMap map=loadMap(temp.get(i));
			
			if(map==null)return;
			
			resource.addMap(map);
		}
		
		//Add Ending
		temp=simple.getEnding();
		for(int i=0; i<temp.size(); i++){
			Image comic=loadImage(temp.get(i));
			if(comic==null)return;
			resource.addEnding(comic);				
		}

		sectionResource=resource;
	}
	
	/**
	 * Untuk meng-load gambar
	 * @param filename lokasi gambar
	 * @return gambar
	 */
	protected abstract Image loadImage(String filename);
	
	/**
	 * Untuk meng-load map
	 * @param filename lokasi map
	 * @return map
	 */
	protected abstract TileMap loadMap(String filename);

	/**
	 * Mendapatkan <code>SimpleSection</code>
	 * @return <code>SimpleSection</code> yang diminta
	 */
	public SimpleSection getSimpleSection(){
		return simpleSection;
	}
	
	/**
	 * Mendapatkan <code>SectionResource</code>
	 * @return <code>SectionResource</code> yang diminta
	 */
	public SectionResource getSectionResource(){
		return sectionResource;
	}
	
	private SimpleSection simpleSection;
	private SectionResource sectionResource;	
	
	//Default loader - load from *.jar resource
	/**
	 * Class <code>Default</code> berfungsi untuk meng-load semua default resource
	 */
	public static class Default extends Loader{
		/**
		 * Membuat object baru
		 * @param simpleSection <code>SimpleSection<code> yang ditetapkan
		 * @param resourceManager <code>ResourceManager<code> yang ditetapkan
		 */
		public Default(SimpleSection simpleSection, 
				ResourceManager resourceManager){
			super(simpleSection, resourceManager);
		}
		
		protected Image loadImage(String filename){
			return ResourceLoader.loadImage("images/comic/"+filename+".jpg");
		}
		
		protected TileMap loadMap(String filename){
			try {
				return resourceManager.loadMap(
						ResourceLoader.loadFile("maps/"+filename+".map"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	//External loader - load from external resource
	/**
	 * Class <code>External</code> berfungsi untuk meng-load semua external
	 * resource yang letaknya ada di folder yang sama dengan program. 
	 */
	public static class External extends Loader{
		/**
		 * Membuat object baru
		 * @param simpleSection <code>SimpleSection<code> yang ditetapkan
		 * @param resourceManager <code>ResourceManager<code> yang ditetapkan
		 */
		public External(SimpleSection simpleSection, 
				ResourceManager resourceManager){
			super(simpleSection, resourceManager);
		}

		protected Image loadImage(String filename){
			String fullFilename="comic/"+filename+".jpg";
			File file=new File(fullFilename);
			if(!file.exists())
				return null;
			return new ImageIcon(fullFilename).getImage();
		}
		
		protected TileMap loadMap(String filename){
			
			try {
				BufferedReader reader=new BufferedReader(
						new FileReader("maps/"+filename+".map"));
				return resourceManager.loadMap(reader);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
}

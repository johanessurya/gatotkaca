package util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class <code>StoryLoader</code> berguna untuk mengload story.xml dan mengubahnya
 * menjadi <code>SimpleSection</code> yang siap diolah nantinya
 * @author Yohanes Surya
 */
public class StoryLoader {
	/**
	 * Membuat object baru
	 * @param storyIs <code>InputStream</code> yang ditetapkan
	 * @throws ParserConfigurationException jika class <code>StoryLoader</code>
	 * tidak dapat membuat <code>javax.xml.parsers.DocumentBuilder</code>
	 * @throws SAXException jika parsing xml terjadi error
	 * @throws IOException jika input/output terjadi error
	 */
	public StoryLoader(InputStream storyIs)
	throws ParserConfigurationException, SAXException, IOException{
		this.simpleSections=new ArrayList<SimpleSection>();
		
		DocumentBuilderFactory dbf=
			DocumentBuilderFactory.newInstance();		
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		dom=db.parse(storyIs);
		
		Element root=dom.getDocumentElement();
		NodeList sections=root.getElementsByTagName("section");
		
		for(int i=0; i<sections.getLength(); i++){
			Element section=(Element)sections.item(i);
			simpleSections.add(getSimpleSection(section));
		}
	}
	
	/**
	 * Check apakah story.xml nya valid
	 * @return <code>true</code> jika valid ingin ditampilkan 
	 * dan <code>false</code> jika sebaliknya
	 */
	public boolean isValid(){
		for(int i=0; i<simpleSections.size(); i++){
			if(simpleSections.get(i)==null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Mendapatkan <code>SimpleSection</code>
	 * @return <code>SimpleSection</code> yang diminta
	 */
	public ArrayList<SimpleSection> getSimpleSections(){
		return simpleSections;
	}
	
	private SimpleSection getSimpleSection(Element section){
		SimpleSection simpleSection=new SimpleSection();
		NodeList title,player,opening,maps,ending;
		title=section.getElementsByTagName("title");
		player=section.getElementsByTagName("player");
		opening=section.getElementsByTagName("opening");
		maps=section.getElementsByTagName("map");
		ending=section.getElementsByTagName("ending");
		
		if(title.getLength()<=0 || player.getLength()<=0 ||
				opening.getLength()<=0 || maps.getLength()<=0 ||
				ending.getLength()<=0)
		{
			return null;
		}
		
		//Set title
		simpleSection.setTitle(getTextValue(title.item(0)));
		
		//Set player
		simpleSection.setPlayer(getPlayer(player));
		
		//Set opening
		for(int i=0; i<opening.getLength(); i++){
			simpleSection.addOpening(getTextValue(opening.item(i)));
		}
		
		//Set map
		for(int i=0; i<maps.getLength(); i++){
			simpleSection.addMap(getTextValue(maps.item(i)));
		}
		
		//Set ending
		for(int i=0; i<ending.getLength(); i++){
			simpleSection.addEnding(getTextValue(ending.item(i)));
		}

		return simpleSection;
	}
	
	private String getTextValue(Node node){
		return node.getChildNodes().item(0).getNodeValue();
	}
	
	private boolean getPlayer(NodeList player){
		String tempPlayer=getTextValue(player.item(0));
		
		if(tempPlayer.equals("0")){
			return true;
		}
		
		return false;
	}
	
	private Document dom;
	private ArrayList<SimpleSection> simpleSections;
}

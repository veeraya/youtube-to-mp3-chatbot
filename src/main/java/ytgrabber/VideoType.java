package ytgrabber;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sushil Kumar <kaysush@outlook.com>
 */
@XmlRootElement(name = "type")
public class VideoType {

    @XmlElement(name = "quality")
    private String quality;
    @XmlElement(name = "format")
    private String format;

    public VideoType(){
        //A nothing to do constructor for JAXB
    }
    public VideoType(String quality, String format) {
        this.quality = quality;
        this.format = format;
    }
    
    public String getQuality(){
        return this.quality;
    }
    
    public String getFormat(){
        return this.format;
    }
    
    public String toString(){
        return this.format + "/" + this.quality;
    }
}

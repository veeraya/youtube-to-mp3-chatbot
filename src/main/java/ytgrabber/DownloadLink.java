package ytgrabber;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Sushil Kumar <kaysush@outlook.com>
 */
public class DownloadLink {
    
    @XmlElement(name = "url")
    private String url;
    @XmlElement(name = "type")
    private VideoType type;

    public DownloadLink(){
        //A nothing to do constructor for JAXB
    }
    public DownloadLink(String url, String quality, String format) {
        this.url = url;
        this.type = new VideoType(quality, format);
    }

    public String getUrl() {
        return this.url;
    }

    public VideoType getType() {
        return this.type;
    }

    public String toString() {
        return "URL : " + this.url + "\n VideoType : " + this.type;
    }
}

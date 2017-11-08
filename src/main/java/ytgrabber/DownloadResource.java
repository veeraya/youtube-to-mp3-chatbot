package ytgrabber;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sushil Kumar <kaysush@outlook.com>
 */
@XmlRootElement(name = "links")
public class DownloadResource {
    private String thumbnail_url;
    private String title;
    @XmlElement(name = "link")
    private ArrayList<DownloadLink> links;
    @XmlElement(name = "error")
    private ProcessingError error;

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param title new value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Get the value of thumbnailUrl
     *
     * @return the value of thumbnailUrl
     */
    public String getThumbnailUrl() {
        return thumbnail_url;
    }

    /**
     * Set the value of thumbnailUrl
     *
     * @param thumbnailUrl new value of thumbnailUrl
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnail_url = thumbnailUrl;
    }


    public ProcessingError getError() {
        return error;
    }

    public void setError(ProcessingError error) {
        this.error = error;
    }
    
    
    public DownloadResource(){
        this.links = new ArrayList<DownloadLink>();
    }
    
    public void addLink(String url, String quality, String format){
        this.links.add(new DownloadLink(url, quality, format));
    }

    public List<DownloadLink> getLinks() {
        return this.links;
    }
    
    public String toString(){
        return links.toString();
    }
    
}

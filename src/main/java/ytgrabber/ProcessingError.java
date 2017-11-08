/*
 * Represent the error (if any during the request)
 * Has two attributes error_code and error_message
 * error_message is applicaple only if error_code is greater than zero (0)
 */
package ytgrabber;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kaysush
 */
@XmlRootElement(name = "error")
public class ProcessingError {
    
    @XmlElement(name = "error_code")
    private int errorCode;
    
    @XmlElement(name = "error_message")
    private String errorMessage;
    
    public ProcessingError(int _errorCode , String _errorMessage){
        errorCode = _errorCode;
        errorMessage = _errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return "[" + this.errorCode + "] " + this.errorMessage;
    }
    
}

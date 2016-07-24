package intranet;

import java.util.List;
import java.util.Observable;

/**
 * Created by juanan on 21/04/14.
 */
public class InputParamsIntranetConnection extends Observable {

    private Exception exception = null;
    private int codeResponse = -1;
    private List<String> cookieList = null;
    private String result = null;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.setChanged();
        this.exception = exception;
    }

    public int getCodeResponse() {
        return codeResponse;
    }

    public void setCodeResponse(int codeResponse) {
        this.setChanged();
        this.codeResponse = codeResponse;
    }

    public List<String> getCookieList() {
        return cookieList;
    }

    public void setCookieList(List<String> cookieList) {
        this.setChanged();
        this.cookieList = cookieList;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.setChanged();
        this.result = result;
    }
}
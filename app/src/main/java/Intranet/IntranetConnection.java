package intranet;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.HttpsURLConnection;

import calendarupv.CalendarsJSON;


public class IntranetConnection implements Observer {

    private String user;
    private String pass;
    private String username;
    private List<String> cookieList;

    private OutPutParamsIntranetConnection outPutParamsIntranetConnection;

    public IntranetConnection(String user, String pass, Observer observer) {
        this.user = user;
        this.pass = pass;
        outPutParamsIntranetConnection = new OutPutParamsIntranetConnection();
        outPutParamsIntranetConnection.addObserver(observer);
        CookieHandler.setDefault(new CookieManager());
    }

    public void connect() {


        if (this.outPutParamsIntranetConnection.getException() != null)
            return;

        IntranetLogin login = new IntranetLogin(this);
        login.execute(this.user, this.pass);
    }

    public void getCalendars() {


        if (this.outPutParamsIntranetConnection.getException() != null)
            return;

        IntranetCalendars calendars = new IntranetCalendars(this);
        calendars.execute(this.cookieList);
    }

    public void getICS(String url) {


        if (this.outPutParamsIntranetConnection.getException() != null)
            return;


        IntranetICS Ics = new IntranetICS(this, url);
        Ics.execute(this.cookieList);
    }

    private Exception processCode(int i) {

        Exception returnException;

        switch (i) {
            case HttpsURLConnection.HTTP_FORBIDDEN:
                returnException = new Exception("Acceso prohibido");
                break;

            case HttpsURLConnection.HTTP_UNAUTHORIZED:
                returnException = new Exception("Usuario o contraseña erroneos");
                break;

            case HttpsURLConnection.HTTP_NOT_FOUND:
                returnException = new Exception("Recurso no encontrado");
                break;

            case HttpsURLConnection.HTTP_CLIENT_TIMEOUT:
                returnException = new Exception("Tiempo de espera agotado");
                break;

            case HttpsURLConnection.HTTP_BAD_REQUEST:
                returnException = new Exception("Petición incorrecta");
                break;

            case HttpsURLConnection.HTTP_MOVED_PERM:
            case HttpsURLConnection.HTTP_MOVED_TEMP:
                returnException = new Exception("Han respondido una redirección");
                break;

            case HttpsURLConnection.HTTP_OK:
            case HttpsURLConnection.HTTP_ACCEPTED:
                returnException = null;
                break;

            default:
                returnException = new Exception("Error desconocido");
        }

        return returnException;
    }

    @Override
    public void update(Observable observable, Object data) {

        InputParamsIntranetConnection inputParamsIntranetConnection = (InputParamsIntranetConnection) observable;

        if (inputParamsIntranetConnection.getException() != null || this.processCode(inputParamsIntranetConnection.getCodeResponse()) != null) {
            //error....

            this.outPutParamsIntranetConnection.setException(this.processCode(inputParamsIntranetConnection.getCodeResponse()));

            if (inputParamsIntranetConnection.getCodeResponse() == 401) {
                outPutParamsIntranetConnection.setUserFail(true);
            }

            this.outPutParamsIntranetConnection.setChanged();
            this.outPutParamsIntranetConnection.notifyObservers();
            return;
        }

        this.cookieList = inputParamsIntranetConnection.getCookieList();

        if (data.equals("login")) {
            //login...

            this.outPutParamsIntranetConnection.setChanged();
            this.outPutParamsIntranetConnection.notifyObservers("login");
        } else if (data.equals("calendars")) {
            //calendars...


            this.outPutParamsIntranetConnection.setCalendars(new CalendarsJSON(inputParamsIntranetConnection.getResult()));

            this.outPutParamsIntranetConnection.notifyObservers("calendars");

        } else if (data.equals("ics")) {
            //ics...


            this.outPutParamsIntranetConnection.setIcsString(inputParamsIntranetConnection.getResult());
            this.outPutParamsIntranetConnection.notifyObservers("ics");

        } else {

        }


    }
}

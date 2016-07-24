package intranet;


import java.util.Observable;

import calendarupv.CalendarsJSON;


public class OutPutParamsIntranetConnection extends Observable {

    private boolean UserFail = false;
    private Exception exception = null;
    private CalendarsJSON calendars;
    private String icsString;

    public void setChanged() {
        super.setChanged();
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.setChanged();
        this.exception = exception;
    }

    public CalendarsJSON getCalendars() {
        return calendars;
    }


    public void setCalendars(CalendarsJSON calendars) {
        this.setChanged();
        this.calendars = calendars;
    }

    public String getIcsString() {
        return icsString;
    }

    public void setIcsString(String icsString) {
        this.setChanged();
        this.icsString = icsString;
    }

    public boolean isUserFail() {
        return UserFail;
    }

    public void setUserFail(boolean userFail) {
        this.setChanged();
        UserFail = userFail;
    }
}

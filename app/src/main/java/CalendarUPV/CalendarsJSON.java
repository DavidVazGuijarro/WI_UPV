package calendarupv;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CalendarsJSON {

    private String username;
    private List<Calendario> calendarios;

    public CalendarsJSON(String data) {
        calendarios = new ArrayList<Calendario>();
        this.parseJson(data);
    }

    private void parseJson(String data) {
        try {

            JSONObject jObject = new JSONObject(data);

            //username
            this.username = jObject.getString("username");

            //agendas...
            JSONArray agendasArray = jObject.getJSONArray("agendas");
            for (int i = 0; i < agendasArray.length(); i++) {
                try {

                    DiaryJSON diaryJSON = new DiaryJSON(agendasArray.getJSONObject(i));
                    if (diaryJSON.getNombre().contains("Horario de Clases")) {
                        calendarios.add(new Calendario(diaryJSON.getUid(), diaryJSON.getNombre(), diaryJSON.getUrl()));
                    }
                } catch (JSONException e) {
                    Log.w(((Object) this).getClass().getName(), "Exception", e);
                }
            }
        } catch (JSONException e) {
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }
    }

    public void setCalendarios(List<Calendario> calendarios) {
        this.calendarios = calendarios;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Calendario> getCalendarios() {
        return calendarios;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (Calendario item : this.calendarios) {
            stringBuilder.append(item.toString());
        }

        return "Calendars{" +
                "username='" + username + '\'' +
                ", agendas=" + stringBuilder.toString() +
                '}';
    }
}



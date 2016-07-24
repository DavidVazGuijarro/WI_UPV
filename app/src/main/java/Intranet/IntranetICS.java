package intranet;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Observer;


public class IntranetICS extends AsyncTask<List<String>, Void, InputParamsIntranetConnection> {

    private String url;
    private InputParamsIntranetConnection inputParamsIntranetConnection;
    private Observer observer;

    public IntranetICS(Observer observer, String url) {
        this.url = url;
        this.observer = observer;
    }

    @Override
    protected void onPreExecute() {
        this.inputParamsIntranetConnection = new InputParamsIntranetConnection();
        this.inputParamsIntranetConnection.addObserver(this.observer);
    }

    @Override
    protected InputParamsIntranetConnection doInBackground(List<String>... params) {

        try {

            URL url = new URL(this.url);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            request.setUseCaches(false);
            request.setRequestMethod("GET");

            request.setInstanceFollowRedirects(true);

            request.setRequestProperty("User-Agent", "Mozilla/5.0");
            request.setRequestProperty("Host", "www.upv.es");

            for (String cookie : params[0]) {
                request.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }

            request.setRequestProperty("Referer", "http://www.upv.es");

            request.setDoOutput(true);
            request.setDoInput(true);

            inputParamsIntranetConnection.setCodeResponse(request.getResponseCode());

            if (request.getHeaderFields().get("Set-Cookie") != null) {
                params[0].addAll(request.getHeaderFields().get("Set-Cookie"));
            }

            inputParamsIntranetConnection.setCookieList(params[0]);

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringWriter sw = new StringWriter();
            char[] buffer = new char[1024 * 4];
            int n;
            while (-1 != (n = in.read(buffer))) {
                sw.write(buffer, 0, n);
            }
            this.inputParamsIntranetConnection.setResult(sw.toString());
        } catch (Exception e) {
            inputParamsIntranetConnection.setException(e);
        }

        return inputParamsIntranetConnection;
    }

    @Override
    protected void onPostExecute(InputParamsIntranetConnection inputParamsIntranetConnection) {
        inputParamsIntranetConnection.notifyObservers("ics");
    }

}
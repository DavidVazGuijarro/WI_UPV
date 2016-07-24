package util.RSS;

import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Noticia implements Comparable<Noticia> {
    static SimpleDateFormat FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private String title;
    private String link;
    private String date;

    public Noticia(String title, String link, String date) {
        this.title = title;
        this.link = link;
        this.date = date;
    }

    public Noticia() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    // getters and setters omitted for brevity
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Noticia copy() {
        Noticia copy = new Noticia(title, link, date);
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ");
        sb.append(title);
        sb.append('\n');
        sb.append("Date: ");
        sb.append(this.getDate());
        sb.append('\n');
        sb.append("Link: ");
        sb.append(link);
        sb.append('\n');
        return sb.toString();
    }


    public int compareTo(Noticia another) {
        if (another == null) return 1;
        // sort descending, most recent first
        return another.date.compareTo(date);
    }

    public void insertarNoticiaDB(SQLiteDatabase db) {
        String sql = "INSERT OR IGNORE INTO \"main\".\"Noticia\" (\"titulo\",\"url\",\"fecha\") VALUES (\"" + this.title + "\",\"" + this.link + "\",\"" + this.date + "\");";
        db.execSQL(sql);
    }
}

package util.RSS;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class XmlPullFeedParser extends BaseFeedParser {

    public XmlPullFeedParser(String feedUrl) {
        super(feedUrl);
    }

    public List<Noticia> parse() {
        List<Noticia> noticias = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            // auto-detect the encoding from the stream
            parser.setInput(this.getInputStream(), null);
            int eventType = parser.getEventType();
            Noticia currentNoticia = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        noticias = new ArrayList<Noticia>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            currentNoticia = new Noticia();
                        } else if (currentNoticia != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                currentNoticia.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                //currentNoticia.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                currentNoticia.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                currentNoticia.setTitle(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM) && currentNoticia != null) {
                            noticias.add(currentNoticia);
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e("AndroidNews::PullFeedParser", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return noticias;
    }
}

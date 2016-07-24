package util.RSS;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import java.util.ArrayList;
import java.util.List;

public class AndroidSaxFeedParser extends BaseFeedParser {

    static final String RSS = "rss";

    public AndroidSaxFeedParser(String feedUrl) {
        super(feedUrl);
    }

    public List<Noticia> parse() {
        final Noticia currentNoticia = new Noticia();
        RootElement root = new RootElement(RSS);
        final List<Noticia> noticias = new ArrayList<Noticia>();
        Element channel = root.getChild(CHANNEL);
        Element item = channel.getChild(ITEM);
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                noticias.add(currentNoticia.copy());
            }
        });
        item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentNoticia.setTitle(body);
            }
        });
        item.getChild(LINK).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentNoticia.setLink(body);
            }
        });
        item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                //currentNoticia.setDescription(body);
            }
        });
        item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentNoticia.setDate(body);
            }
        });
        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.ISO_8859_1, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return noticias;
    }
}

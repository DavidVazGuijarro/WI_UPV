package util.RSS;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static util.RSS.BaseFeedParser.DESCRIPTION;
import static util.RSS.BaseFeedParser.ITEM;
import static util.RSS.BaseFeedParser.LINK;
import static util.RSS.BaseFeedParser.PUB_DATE;
import static util.RSS.BaseFeedParser.TITLE;

public class RssHandler extends DefaultHandler {
    private List<Noticia> noticias;
    private Noticia currentNoticia;
    private StringBuilder builder;

    public List<Noticia> getNoticias() {
        return this.noticias;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        super.endElement(uri, localName, name);
        if (this.currentNoticia != null) {
            if (localName.equalsIgnoreCase(TITLE)) {
                currentNoticia.setTitle(builder.toString());
            } else if (localName.equalsIgnoreCase(LINK)) {
                currentNoticia.setLink(builder.toString());
            } else if (localName.equalsIgnoreCase(DESCRIPTION)) {
                //currentNoticia.setDescription(builder.toString());
            } else if (localName.equalsIgnoreCase(PUB_DATE)) {
                currentNoticia.setDate(builder.toString());
            } else if (localName.equalsIgnoreCase(ITEM)) {
                noticias.add(currentNoticia);
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        noticias = new ArrayList<Noticia>();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if (localName.equalsIgnoreCase(ITEM)) {
            this.currentNoticia = new Noticia();
        }
    }
}
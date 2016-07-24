package util.RSS;

public abstract class FeedParserFactory {
    static String feedUrl = "http://www.upv.es/pls/oalu/sic_rss.rss_ver20?p_rss_feed=0&p_idioma=i&p_miwser=PI";

    public static FeedParser getParser() {
        return getParser(ParserType.ANDROID_SAX);
    }

    public static FeedParser getParser(ParserType type) {
        switch (type) {
            case SAX:
                return new SaxFeedParser(feedUrl);
            case DOM:
                return new DomFeedParser(feedUrl);
            case ANDROID_SAX:
                return new AndroidSaxFeedParser(feedUrl);
            case XML_PULL:
                return new XmlPullFeedParser(feedUrl);
            default:
                return null;
        }
    }
}

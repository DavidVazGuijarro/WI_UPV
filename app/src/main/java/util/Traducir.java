package util;

public class Traducir {

    String code;
    String lang;
    String text;

    public Traducir() {
    }

    public Traducir(String code, String lang, String text) {
        this.code = code;
        this.lang = lang;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

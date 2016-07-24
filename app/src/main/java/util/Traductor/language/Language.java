/*
 * Copyright 2013 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util.Traductor.language;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import upv.welcomeincoming.com.R;

/**
 * Language - an enum of language codes supported by the Yandex API
 */
public enum Language {
    ALBANIAN("sq"),
    ARMENIAN("hy"),
    AZERBAIJANI("az"),
    BELARUSIAN("be"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GEORGIAN("ka"),
    GREEK("el"),
    HUNGARIAN("hu"),
    ITALIAN("it"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SERBIAN("sr"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("es"),
    SWEDISH("sv"),
    TURKISH("tr"),
    UKRAINIAN("uk");

    public static String[] ArrayNameLanguage = {"CATALAN", "ENGLISH", "FRENCH", "GERMAN", "ITALIAN", "PORTUGUESE", "SPANISH"};
    public static String[] ArrayLanguageCode = {"ca", "en", "fr", "de", "it", "pt", "es"};
    public static int[] ArrayLanguageDrawable = {R.drawable.ca, R.drawable.en, R.drawable.fr, R.drawable.de, R.drawable.it, R.drawable.pt, R.drawable.es};
    private static Locale[] ArrayLocaleLanguage = {new Locale("ca", "ES"), new Locale("en", "GB"), new Locale("fr", "FR"), new Locale("de", "DE"), new Locale("it", "IT"), new Locale("pt", "PT"), new Locale("es", "ES")};


    /**
     * String representation of this language.
     */
    private final String language;

    /**
     * Enum constructor.
     *
     * @param pLanguage The language identifier.
     */
    private Language(final String pLanguage) {
        language = pLanguage;
    }

    public static Language fromString(final String pLanguage) {
        for (Language l : values()) {
            if (l.toString().equals(pLanguage)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Returns the String representation of this language.
     *
     * @return The String representation of this language.
     */
    @Override
    public String toString() {
        return language;
    }

    //Devuelve una lista de strings con los nombres de los lenguajes que puede traducir el traductor
    public static List<String> getArrayStringNameLanguages() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < ArrayNameLanguage.length; i++) {
            list.add(ArrayNameLanguage[i]);
        }
        return list;
    }


    public static String getCodeByName(String name) {
        String res = "es";
        for (int i = 0; i < ArrayNameLanguage.length; i++) {
            if (ArrayNameLanguage[i].equals(name)) return ArrayLanguageCode[i];
        }
        return res;
    }

    public static String getNameByCode(String code) {
        String res = "ENGLISH";
        for (int i = 0; i < ArrayNameLanguage.length; i++) {
            if (ArrayLanguageCode[i].equals(code)) return ArrayNameLanguage[i];
        }
        return res;
    }

    public static Locale getLocaleByName(String name) {
        Locale res = new Locale("en", "GB");
        for (int i = 0; i < ArrayNameLanguage.length; i++) {
            if (ArrayNameLanguage[i].equals(name)) return ArrayLocaleLanguage[i];
        }
        return res;
    }


    public static Drawable getFlagResourcebyName(String selection, Context mContext) {
        Drawable res = mContext.getResources().getDrawable(R.drawable.en);
        for (int i = 0; i < ArrayNameLanguage.length; i++) {
            if (ArrayNameLanguage[i].equals(selection))
                return mContext.getResources().getDrawable(ArrayLanguageDrawable[i]);
        }
        return res;
    }

    public static Locale getLocaleByCode(String code) {
        Locale res = new Locale("en", "GB");
        for (int i = 0; i < ArrayLanguageCode.length; i++) {
            if (ArrayLanguageCode[i].equals(code)) return ArrayLocaleLanguage[i];
        }
        return res;
    }

    public static Drawable getFlagResourcebyCode(String languageCode, Context mContext) {
        Drawable res = null;
        for (int i = 0; i < ArrayLanguageCode.length; i++) {
            if (ArrayLanguageCode[i].equals(languageCode))
                return mContext.getResources().getDrawable(ArrayLanguageDrawable[i]);
        }
        return res;
    }
}

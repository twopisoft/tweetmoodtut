package com.twopi.tutorial.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class provides a method to convert from ISO-639-1 languages designation (returned by Twitter) to
 * ISO-639-2 one which is needed by IDOL Sentiment Analysis API.
 * @author arshad01
 *
 */
public class LanguageMapper {
    
    private static Map<String, String> LANG_MAP = new HashMap<String,String>();
    
    static {
        String[] languages = Locale.getISOLanguages();
        for (String lang : languages) {
            Locale loc = new Locale(lang);
            LANG_MAP.put(loc.getISO3Language(), lang);
        }
        
    }
    
    public static String map(String inputLanguage) {
        return LANG_MAP.getOrDefault(inputLanguage, "eng");
    }

}

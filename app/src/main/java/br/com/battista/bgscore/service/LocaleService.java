package br.com.battista.bgscore.service;

import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;

import static br.com.battista.bgscore.service.LocaleService.SupportedLocale.EN;
import static br.com.battista.bgscore.service.LocaleService.SupportedLocale.PT;

public class LocaleService {

    private Map<String, String> supportedLocales;

    public LocaleService() {
        supportedLocales = Maps.newHashMap();
        supportedLocales.put(PT.name().toLowerCase(), PT.name().toLowerCase());
        supportedLocales.put(EN.name().toLowerCase(), EN.name().toLowerCase());
    }

    public String processSupportedLocales(Locale locale) {
        if (locale == null || !supportedLocales.containsKey(locale.getLanguage().toLowerCase())) {
            return EN.name();
        }
        return supportedLocales.get(locale.getLanguage().toLowerCase());
    }

    public enum SupportedLocale {
        PT, EN
    }

}

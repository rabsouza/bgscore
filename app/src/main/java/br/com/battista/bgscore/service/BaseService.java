package br.com.battista.bgscore.service;

import android.net.http.HttpResponseCache;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.constants.HttpStatus;
import br.com.battista.bgscore.constants.RestConstant;
import br.com.battista.bgscore.exception.AuthenticationException;
import br.com.battista.bgscore.exception.BGScoreException;
import br.com.battista.bgscore.exception.EntityAlreadyExistsException;
import br.com.battista.bgscore.exception.EntityNotFoundException;
import br.com.battista.bgscore.exception.ValidatorException;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_CACHE_SIZE;
import static br.com.battista.bgscore.constants.RestConstant.HEADER_CACHE_CONTROL_MAX_AGE_KEY;
import static br.com.battista.bgscore.constants.RestConstant.HEADER_CACHE_CONTROL_MAX_AGE_VALUE;
import static br.com.battista.bgscore.constants.RestConstant.HEADER_LOCALE_KEY;
import static br.com.battista.bgscore.constants.RestConstant.HEADER_USER_AGENT_KEY;
import static br.com.battista.bgscore.constants.RestConstant.HEADER_USER_AGENT_VALUE;

public class BaseService {

    public static final String METHOD_GET = "GET";
    private static final String TAG = BaseService.class.getSimpleName();

    protected Retrofit builder;

    public BaseService() {
        HttpLoggingInterceptor logging = createHttpLoggingInterceptor();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(logging);
        if (MainApplication.instance() != null) {
            Cache cache = createCache();
            httpClient.cache(cache);
        }
        httpClient.networkInterceptors().add(createHttpInterceptor());

        builder = new Retrofit.Builder()
                .baseUrl(RestConstant.REST_API_ENDPOINT.concat(RestConstant.REST_API_V1))
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build()).build();
    }

    protected Retrofit getBuilder() {
        return builder;
    }

    @NonNull
    private Interceptor createHttpInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                final MainApplication instance = MainApplication.instance();
                final LocaleService localeService = new LocaleService();
                if (request.method().equals(METHOD_GET)) {
                    request = request
                            .newBuilder()
                            .addHeader(HEADER_CACHE_CONTROL_MAX_AGE_KEY, HEADER_CACHE_CONTROL_MAX_AGE_VALUE)
                            .addHeader(HEADER_USER_AGENT_KEY, HEADER_USER_AGENT_VALUE)
                            .addHeader(HEADER_LOCALE_KEY, localeService.processSupportedLocales(instance.getCurrentLocale()))
                            .build();
                } else {
                    request = request
                            .newBuilder()
                            .addHeader(HEADER_LOCALE_KEY, localeService.processSupportedLocales(instance.getCurrentLocale()))
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    @NonNull
    private Cache createCache() {
        MainApplication instance = MainApplication.instance();
        try {
            Log.i(TAG, "createCache: default cache file!");
            File cacheFile = instance.getCacheDir();
            cacheFile.setReadable(true);

            HttpResponseCache.install(cacheFile, DEFAULT_CACHE_SIZE);
            return new Cache(cacheFile, DEFAULT_CACHE_SIZE);

        } catch (IOException e) {
            Log.e(TAG, "createCache: " + e.getLocalizedMessage(), e);
            throw new BGScoreException("Error create to cache!", e);
        }
    }

    @NonNull
    private HttpLoggingInterceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return logging;
    }

    protected void validateErrorResponse(Response<?> response, String errorMessage) {
        if (response != null && response.code() == HttpStatus.BAD_REQUEST.value()) {
            Log.e(TAG, errorMessage);
            throw new ValidatorException(errorMessage);
        } else if (response != null && response.code() == HttpStatus.UNAUTHORIZED.value()) {
            Log.e(TAG, errorMessage);
            throw new AuthenticationException(errorMessage);
        } else if (response != null && response.code() == HttpStatus.NOT_FOUND.value()) {
            Log.e(TAG, errorMessage);
            throw new EntityNotFoundException(errorMessage);
        } else if (response != null && response.code() == HttpStatus.CONFLICT.value()) {
            Log.e(TAG, errorMessage);
            throw new EntityAlreadyExistsException(errorMessage);
        } else {
            Log.e(TAG, errorMessage);
        }
    }
}

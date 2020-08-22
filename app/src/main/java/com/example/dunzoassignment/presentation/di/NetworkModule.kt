package com.example.dunzoassignment.presentation.di

import com.example.dunzoassignment.BuildConfig
import com.example.dunzoassignment.network.ApiClient
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun getApiClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.flickr.com/services/rest/")
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun getOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            headerInterceptor: Interceptor
    ): OkHttpClient {

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, null)
        val sslSocketFactory = sslContext.socketFactory

        val httpBuilder = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory)
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))



        if (BuildConfig.DEBUG)
            httpBuilder.addInterceptor(loggingInterceptor)
        return httpBuilder.build()
    }

    @Provides
    @Singleton
    fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                    chain.request().newBuilder()
                            .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}
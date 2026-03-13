package com.example.myapplication.itemapi

import com.example.myapplication.service.HistorialPesoService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class HistorialPesoAPI {
    companion object {
        private var mItemAPI: HistorialPesoService? = null

        @Synchronized
        fun API(): HistorialPesoService {
            if (mItemAPI == null) {

                val gsondateformat = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                mItemAPI = Retrofit.Builder()
                    .baseUrl("http://158.101.112.242:8080/")
                    .client(getUnsafeOkHttpClient()) // 🚨 ¡ESTO ES LO QUE TE FALTABA! 🚨
                    .addConverterFactory(NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                    .build()
                    .create(HistorialPesoService::class.java)
            }
            return mItemAPI!!
        }

        private fun getUnsafeOkHttpClient(): OkHttpClient {
            try {
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                )

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                val sslSocketFactory = sslContext.socketFactory

                return OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier { _, _ -> true }
                    .build()

            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        /**
         * Converter factory that handles empty response bodies (0 bytes) by returning null
         * instead of throwing an EOFException in the Gson converter.
         */
        class NullOnEmptyConverterFactory : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, *> {
                val delegate: Converter<ResponseBody, *> =
                    retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
                return Converter { body ->
                    if (body.contentLength() == 0L) null else delegate.convert(body)
                }
            }
        }
    }
}
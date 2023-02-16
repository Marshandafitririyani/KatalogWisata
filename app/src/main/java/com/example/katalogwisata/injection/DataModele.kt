package com.example.katalogwisata.injection

import android.content.Context
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.okhttp.SSLTrust
import com.example.katalogwisata.BuildConfig
import com.example.katalogwisata.api.ApiService
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.data.user.Session
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

@InstallIn(SingletonComponent::class)
@Module
class DataModele {
    @Provides
    fun provideSession(@ApplicationContext context: Context,gson: Gson) = Session(context,gson)

    @Provides
    fun provideGson() =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Provides
    fun provideOkHttpClient(session: Session): OkHttpClient {

        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val token = session.getString(Const.TOKEN.API_TOKEN)
                val egld = session.getString(Const.TOKEN.FCM_TOKEN).trim()
                Timber.d("tokenAndRegid: $token &regId")
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .header("Contet-Type", "application/json")
                    .header("Cookie", "laravel_session=qqAYeW97FzclkdubCgNLJCZnQGCH7cYiQTAN89TY")
                    .header("Postman-Token", "<calculated when request is sent>")
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }

        if (BuildConfig.DEBUG) {
            val interceptors = HttpLoggingInterceptor()
            interceptors.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptors)
        }

        return okHttpClient.build()

    }
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://magang.crocodic.net/ki/kelompok_2/project-wisata/public/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    }

}
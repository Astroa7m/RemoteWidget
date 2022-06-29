package com.example.remotewidget.data

import com.example.remotewidget.BuildConfig
import com.example.remotewidget.data.model.RemoteImage
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RemoteImageService {

    /**
     * returns a random image object from unsplash api
     * @param clientId should be replaced with your client id
     * which you can obtain it from unsplash api
     */
    @GET
    suspend fun getRandomImageObject(
        @Url unsplashBaseUrl: String = "https://api.unsplash.com/photos/random",
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("query") query: String
    ): Response<RemoteImage>


    /**
     * returns a random image bytes from unsplash api
     * @param clientId should be replaced with your client id
     * which you can obtain it from unsplash api
     */
    @GET
    suspend fun getRandomImageBytes(
        @Url imageUrl: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID
    ): Response<ResponseBody>

    companion object{
        val serviceInstance: RemoteImageService by lazy {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().addInterceptor(logging).build()

            Retrofit.Builder()
                .client(client)
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RemoteImageService::class.java)
        }
    }

}
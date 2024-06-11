package hr.algebra.echoessence.api

import android.content.Context
import hr.algebra.echoessence.model.Album
import hr.algebra.echoessence.model.MyData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeezerFetcher(private val context: Context) {

    private val deezerApi: DeezerApi = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DeezerApi::class.java)

    fun search(query: String): Call<MyData> {
        return deezerApi.search(query)
    }
}

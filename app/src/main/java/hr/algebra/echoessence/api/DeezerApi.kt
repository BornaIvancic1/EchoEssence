package hr.algebra.echoessence.api

import hr.algebra.echoessence.model.Album
import hr.algebra.echoessence.model.MyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val API_URL = "https://deezerdevs-deezer.p.rapidapi.com/"

interface DeezerApi {
        @Headers("x-rapidapi-key: b9ebec4d8bmshcb087ea7cf9b77ap16b28ajsnd4b11dcaf3bb",
                "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com")
        @GET("search")
        fun search(@Query("q") query: String): Call<MyData>
}

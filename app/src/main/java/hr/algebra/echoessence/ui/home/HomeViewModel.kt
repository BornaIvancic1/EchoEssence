package hr.algebra.echoessence.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.algebra.echoessence.api.DeezerFetcher
import hr.algebra.echoessence.model.Data
import hr.algebra.echoessence.model.MyData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _music = MutableLiveData<MyData>()
    val music: LiveData<MyData> get() = _music

    var currentAlbumCoverUrl: String? = null
    var currentSongTitle: String? = null
    var currentArtistName: String? = null
    var dominantColor: Int? = null
    var vibrantColor: Int? = null

    fun searchMusic(context: Context, query: String) {
        val fetcher = DeezerFetcher(context)
        fetcher.search(query).enqueue(object : Callback<MyData> {
            override fun onResponse(call: Call<MyData>, response: Response<MyData>) {
                if (response.isSuccessful) {
                    response.body()?.let { myData ->
                        _music.value = myData
                        Log.d("HomeViewModel", "Music retrieved successfully: ${myData.data.size}")
                    }
                } else {
                    Log.e("HomeViewModel", "Response was not successful. Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MyData>, t: Throwable) {
                Log.e("HomeViewModel", "Failed to retrieve music", t)
            }
        })
    }
}

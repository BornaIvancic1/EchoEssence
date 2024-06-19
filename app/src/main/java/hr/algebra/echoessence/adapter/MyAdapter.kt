package hr.algebra.echoessence.adapter

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.echoessence.R
import hr.algebra.echoessence.model.Data
import hr.algebra.echoessence.model.Library
import hr.algebra.echoessence.dao.LibraryRepository
import hr.algebra.echoessence.singleton.MusicPlayer

class MyAdapter(
    val context: FragmentActivity,
    val dataList: List<Data>,
    val listener: OnAlbumClickListener,
    val playPauseMusic: () -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val libraryRepository = LibraryRepository(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData = dataList[position]
        holder.title.text = currentData.title
        holder.artist.text = currentData.artist.name
        Picasso.get().load(currentData.album.cover_xl).into(holder.image)

        holder.itemView.setOnClickListener {
            playMusic(currentData.preview, currentData.title, currentData.artist.name)
            listener.onAlbumClick(currentData.album.cover_xl)
        }

        holder.save.setOnClickListener {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val libraryEntry = Library(
                    id = currentData.id,
                    userId = currentUserId,
                    albumTitle = currentData.album.title,
                    songTitle = currentData.title,
                    duration = currentData.duration,
                    albumCoverUrl = currentData.album.cover_xl,
                    artistName = currentData.artist.name,
                    artistId = currentData.artist.id
                )
                libraryRepository.addLibraryEntry(libraryEntry)
                Toast.makeText(context, "Added to library successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playMusic(previewUrl: String, songName: String, artistName: String) {
        MusicPlayer.mediaPlayer?.stop()
        MusicPlayer.mediaPlayer?.release()
        MusicPlayer.mediaPlayer = MediaPlayer.create(context, previewUrl.toUri()).apply {
            setOnErrorListener { mp, what, extra ->
                Log.e("MusicPlayer", "Error occurred while playing music: $what, $extra")
                true
            }
            start()
        }

        val songNameTextView = context.findViewById<TextView>(R.id.songName)
        val artistNameTextView = context.findViewById<TextView>(R.id.artistName)

        songNameTextView.text = songName
        artistNameTextView.text = artistName
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.musicImage)
        val title: TextView = itemView.findViewById(R.id.musicTitle)
        val artist: TextView = itemView.findViewById(R.id.musicArtist)
        val save: ImageButton = itemView.findViewById(R.id.btnSave)
    }

    private fun getCurrentUserId(): Int? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        Log.d("MyAdapter", "Retrieved user ID: $userId")
        return userId.takeIf { it != -1 }
    }
}

package hr.algebra.echoessence.adapter

import android.app.Activity
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.echoessence.R
import hr.algebra.echoessence.model.Album
import hr.algebra.echoessence.model.Data
import hr.algebra.echoessence.model.MyData
import hr.algebra.echoessence.singleton.MusicPlayer

class MyAdapter(val context: FragmentActivity, val dataList:List<Data>, val listener: OnAlbumClickListener):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.item_album,parent,false)
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

        holder.image.setOnClickListener {
            playMusic(currentData.preview)
            listener.onAlbumClick(currentData.album.cover_xl)
        }

        holder.save.setOnClickListener{
        }
    }

    private fun playMusic(previewUrl: String) {
        MusicPlayer.mediaPlayer?.stop()
        MusicPlayer.mediaPlayer?.release()
        MusicPlayer.mediaPlayer = MediaPlayer.create(context, previewUrl.toUri())
        MusicPlayer.mediaPlayer?.setOnErrorListener { mp, what, extra ->
            true
        }
        MusicPlayer.mediaPlayer?.start()
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val image: ImageView
        val title: TextView
        val artist: TextView
        val save: ImageButton

        init {
            image=itemView.findViewById(R.id.musicImage)
            title=itemView.findViewById(R.id.musicTitle)
            artist=itemView.findViewById(R.id.musicArtist)
            save=itemView.findViewById(R.id.btnSave)
        }
    }
}

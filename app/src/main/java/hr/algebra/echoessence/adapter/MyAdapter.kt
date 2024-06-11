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

class MyAdapter(val context: FragmentActivity, val dataList:List<Data>):
RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.item_album,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData=dataList[position]

        holder.title.text=currentData.title
        Picasso.get().load(currentData.album.cover_xl).into(holder.image)

        holder.play.setOnClickListener{
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context,currentData.preview.toUri())
            mediaPlayer?.start()
        }
        holder.pause.setOnClickListener{
            mediaPlayer?.pause()
        }
        holder.save.setOnClickListener{
            // Implement your save functionality here
        }
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val image: ImageView
        val title: TextView
        val play: ImageButton
        val pause: ImageButton
        val save: ImageButton

        init {
            image=itemView.findViewById(R.id.musicImage)
            title=itemView.findViewById(R.id.musicTitle)
            play=itemView.findViewById(R.id.btnPlay)
            pause=itemView.findViewById(R.id.btnPause)
            save=itemView.findViewById(R.id.btnSave)
        }
    }
}
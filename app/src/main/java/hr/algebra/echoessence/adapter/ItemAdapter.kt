package hr.algebra.echoessence.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.echoessence.R
import hr.algebra.echoessence.model.Item

class ItemAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // Define a ViewHolder for the RecyclerView items
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val albumCover: ImageView = view.findViewById(R.id.album_cover)
        val albumName: TextView = view.findViewById(R.id.album_name)
        val artistName: TextView = view.findViewById(R.id.artist_name)
    }

    // Inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return ItemViewHolder(view)
    }

    // Bind data to the item
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.albumName.text = item.title
        holder.artistName.text = item.artist
        // Load the album cover into the ImageView (you can use a library like Glide or Picasso)
        // Glide.with(holder.view.context).load(item.coverImagePath).into(holder.albumCover)
    }

    // Return the total count of items
    override fun getItemCount() = itemList.size
}

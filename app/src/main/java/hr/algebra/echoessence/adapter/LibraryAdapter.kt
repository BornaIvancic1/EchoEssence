package hr.algebra.echoessence.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.echoessence.R
import hr.algebra.echoessence.model.Library

class LibraryAdapter(
    private val context: Context,
    private val libraryList: List<Library>,
    private val onDeleteClickListener: (Library) -> Unit,
    private val onUpdateNoteClickListener: (Library) -> Unit
) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false)
        return LibraryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return libraryList.size
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val currentLibrary = libraryList[position]

        holder.trackTitle.text = currentLibrary.songTitle
        holder.trackArtist.text = currentLibrary.artistName
        holder.albumTitle.text = currentLibrary.albumTitle
        Picasso.get().load(currentLibrary.albumCoverUrl).into(holder.trackImage)

        holder.btnDeleteTrack.setOnClickListener {
            onDeleteClickListener(currentLibrary)
        }

        holder.itemView.setOnClickListener {
            showNoteDialog(currentLibrary)
        }
    }

    private fun showNoteDialog(library: Library) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_note_input, null)
        val etNoteInput = dialogView.findViewById<EditText>(R.id.etNoteInput)
        val btnSaveNote = dialogView.findViewById<Button>(R.id.btnSaveNote)

        etNoteInput.setText(library.note)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corners)

        btnSaveNote.setOnClickListener {
            library.note = etNoteInput.text.toString()
            onUpdateNoteClickListener(library)
            alertDialog.dismiss()
            notifyDataSetChanged()
        }

        alertDialog.show()
    }

    class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
        val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
        val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
        val albumTitle: TextView = itemView.findViewById(R.id.albumTitle)
        val btnDeleteTrack: ImageButton = itemView.findViewById(R.id.btnDeleteTrack)
    }
}

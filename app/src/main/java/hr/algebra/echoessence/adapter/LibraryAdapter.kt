package hr.algebra.echoessence.adapter

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import hr.algebra.echoessence.R
import hr.algebra.echoessence.model.Library
import hr.algebra.echoessence.ui.extra.ColorExtractor
import hr.algebra.echoessence.ui.extra.TimeUtils
import hr.algebra.echoessence.worker.SaveNoteWorker

class LibraryAdapter(
    private val context: FragmentActivity,
    private val libraryList: List<Library>,
    private val onDeleteClickListener: (Library) -> Unit,
    private val onUpdateNoteClickListener: (Library) -> Unit
) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    private var currentPopupView: View? = null
    private val colorExtractor = ColorExtractor(context)

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
            togglePopupVisibility(currentLibrary)
        }
    }

    private fun togglePopupVisibility(library: Library) {
        val rootView = context.findViewById<ViewGroup>(android.R.id.content)

        if (currentPopupView != null) {
            val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
            currentPopupView!!.startAnimation(zoomOut)
            zoomOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    rootView.removeView(currentPopupView)
                    currentPopupView = null
                    showPopup(library)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        } else {
            showPopup(library)
        }
    }

    private fun showPopup(library: Library) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_library_detail, null)

        val libraryFullAlbumArt: ShapeableImageView = popupView.findViewById(R.id.libraryFullAlbumArt)
        val libraryFullSongName: TextView = popupView.findViewById(R.id.libraryFullSongName)
        val libraryFullArtistName: TextView = popupView.findViewById(R.id.libraryFullArtistName)
        val libraryFullAlbumTitle: TextView = popupView.findViewById(R.id.libraryFullAlbumTitle)
        val libraryFullDuration: TextView = popupView.findViewById(R.id.libraryFullDuration)
        val libraryFullNote: EditText = popupView.findViewById(R.id.libraryFullNote)
        val saveNoteButton: Button = popupView.findViewById(R.id.saveNoteButton)
        val libraryCollapseButton: ImageButton = popupView.findViewById(R.id.libraryCollapseButton)

        Picasso.get().load(library.albumCoverUrl).into(libraryFullAlbumArt, object : Callback {
            override fun onSuccess() {
                val drawable = libraryFullAlbumArt.drawable as BitmapDrawable
                val bitmap = drawable.bitmap
                colorExtractor.extractColors(bitmap, object : ColorExtractor.ColorExtractionListener {
                    override fun onColorsExtracted(dominantColor: Int, vibrantColor: Int) {
                        applyGradientBackground(popupView, dominantColor, vibrantColor)
                    }
                })
            }

            override fun onError(e: Exception?) {
            }
        })

        libraryFullSongName.text = library.songTitle
        libraryFullArtistName.text = library.artistName
        libraryFullAlbumTitle.text = library.albumTitle
        libraryFullDuration.text = "Duration: ${TimeUtils.formatDuration(library.duration)}"
        libraryFullNote.setText(library.note)

        // Start marquee effect
        libraryFullSongName.isSelected = true
        libraryFullArtistName.isSelected = true
        libraryFullAlbumTitle.isSelected = true
        libraryFullDuration.isSelected = true

        saveNoteButton.setOnClickListener {
            val updatedNote = libraryFullNote.text.toString()
            library.note = updatedNote
            onUpdateNoteClickListener(library)

            // Trigger the Worker to show a notification
            val saveNoteWorkRequest = OneTimeWorkRequest.Builder(SaveNoteWorker::class.java).build()
            WorkManager.getInstance(context).enqueue(saveNoteWorkRequest)

            // Close the popup
            val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
            popupView.startAnimation(zoomOut)
            zoomOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    val rootView = context.findViewById<ViewGroup>(android.R.id.content)
                    rootView.removeView(popupView)
                    currentPopupView = null
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.TOP
        )
        layoutParams.topMargin = 50
        layoutParams.leftMargin = 16
        layoutParams.rightMargin = 16
        popupView.layoutParams = layoutParams

        val rootView = context.findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(popupView)

        val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
        popupView.startAnimation(zoomIn)
        popupView.bringToFront()

        libraryCollapseButton.setOnClickListener {
            val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
            popupView.startAnimation(zoomOut)
            zoomOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    val rootView = context.findViewById<ViewGroup>(android.R.id.content)
                    rootView.removeView(popupView)
                    currentPopupView = null
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }

        currentPopupView = popupView
        popupView.visibility = View.VISIBLE
    }

    private fun applyGradientBackground(view: View, dominantColor: Int, vibrantColor: Int) {
        val density = context.resources.displayMetrics.density
        val cornerRadiusPx = 30 * density

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(vibrantColor, dominantColor)
        ).apply {
            cornerRadius = cornerRadiusPx
        }

        view.background = gradientDrawable
    }

    class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
        val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
        val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
        val albumTitle: TextView = itemView.findViewById(R.id.albumTitle)
        val btnDeleteTrack: ImageButton = itemView.findViewById(R.id.btnDeleteTrack)
    }
}

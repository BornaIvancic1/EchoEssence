package hr.algebra.echoessence.ui.home

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import hr.algebra.echoessence.R
import hr.algebra.echoessence.adapter.MyAdapter
import hr.algebra.echoessence.adapter.OnAlbumClickListener
import hr.algebra.echoessence.databinding.FragmentHomeBinding
import hr.algebra.echoessence.singleton.MusicPlayer
import hr.algebra.echoessence.ui.extra.ColorExtractor

class HomeFragment : Fragment(), OnAlbumClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var miniPlayer: View
    private lateinit var fullPlayer: View
    private lateinit var colorExtractor: ColorExtractor
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        colorExtractor = ColorExtractor(requireContext())

        miniPlayer = root.findViewById(R.id.musicPlayer)
        fullPlayer = root.findViewById(R.id.fullPlayer)

        setupListeners(root)
        observeMusic(homeViewModel, recyclerView)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateMusicPlayerUI()
    }

    private fun setupListeners(root: View) {
        root.findViewById<ImageButton>(R.id.playPauseButton)?.setOnClickListener {
            playPauseMusic()
        }
        root.findViewById<ImageButton>(R.id.fullPlayPauseButton)?.setOnClickListener {
            playPauseMusic()
        }
        miniPlayer.setOnClickListener {
            togglePlayerVisibility()
        }
        fullPlayer.findViewById<ImageButton>(R.id.collapseButton)?.setOnClickListener {
            togglePlayerVisibility()
        }
        root.findViewById<Button>(R.id.searchButton)?.setOnClickListener {
            val query = root.findViewById<EditText>(R.id.searchInput).text.toString()
            if (query.trim().isNotEmpty()) {
                homeViewModel.searchMusic(requireContext(), query)
                root.findViewById<EditText>(R.id.searchInput).setText("")
            } else {
                Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeMusic(homeViewModel: HomeViewModel, recyclerView: RecyclerView) {
        homeViewModel.music.observe(viewLifecycleOwner, Observer { music ->
            if (music.data.isEmpty()) {
                Toast.makeText(context, "No music available", Toast.LENGTH_SHORT).show()
            } else {
                recyclerView.adapter = MyAdapter(requireActivity(), music.data, this)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onAlbumClick(albumCoverUrl: String) {
        val fullCoverImage: ImageView? = view?.findViewById(R.id.fullAlbumArt)
        val coverImage: ImageView? = view?.findViewById(R.id.albumArt)

        // Load the image into the full player image view
        Picasso.get().load(albumCoverUrl).into(fullCoverImage, object : Callback {
            override fun onSuccess() {
                fullCoverImage?.drawable?.let { drawable ->
                    val bitmap = (drawable as BitmapDrawable).bitmap
                    colorExtractor.extractColors(bitmap, object : ColorExtractor.ColorExtractionListener {
                        override fun onColorsExtracted(dominantColor: Int, vibrantColor: Int) {
                            homeViewModel.dominantColor = dominantColor
                            homeViewModel.vibrantColor = vibrantColor
                            applyGradientBackground(dominantColor, vibrantColor)
                        }
                    })
                }
            }
            override fun onError(e: Exception?) {
                Toast.makeText(context, "Error loading image into full player", Toast.LENGTH_SHORT).show()
            }
        })

        Picasso.get().load(albumCoverUrl).into(coverImage, object : Callback {
            override fun onSuccess() {
            }
            override fun onError(e: Exception?) {
                Toast.makeText(context, "Error loading image into mini player", Toast.LENGTH_SHORT).show()
            }
        })

        homeViewModel.currentAlbumCoverUrl = albumCoverUrl
        updateMusicPlayerUI()
    }

    private fun applyGradientBackground(dominantColor: Int, vibrantColor: Int) {
        val density = resources.displayMetrics.density
        val cornerRadiusPx = 50 * density

        Log.d("HomeFragment", "Applying gradient background with vibrant color: $vibrantColor and lighter black")

        val lighterBlack = Color.BLACK
        val gradientDrawableFull = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(vibrantColor, lighterBlack)
        ).apply {
            cornerRadius = cornerRadiusPx
        }
        fullPlayer.background = gradientDrawableFull

        val colorDrawableMini = GradientDrawable().apply {
            setColor(vibrantColor)
            cornerRadius = cornerRadiusPx
        }
        miniPlayer.background = colorDrawableMini
    }

    override fun onPlayPauseClick() {
        playPauseMusic()
    }

    private fun playPauseMusic() {
        MusicPlayer.mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                setPlayPauseIcon(android.R.drawable.ic_media_play)
            } else {
                it.start()
                setPlayPauseIcon(android.R.drawable.ic_media_pause)
            }
        } ?: Toast.makeText(context, "No music selected", Toast.LENGTH_SHORT).show()
    }

    private fun setPlayPauseIcon(drawableRes: Int) {
        val playPauseButton = view?.findViewById<ImageButton>(R.id.playPauseButton)
        val fullPlayPauseButton = view?.findViewById<ImageButton>(R.id.fullPlayPauseButton)
        playPauseButton?.setImageResource(drawableRes)
        fullPlayPauseButton?.setImageResource(drawableRes)
    }

    private fun togglePlayerVisibility() {
        if (miniPlayer.visibility == View.VISIBLE) {
            miniPlayer.visibility = View.GONE
            fullPlayer.visibility = View.VISIBLE
            fullPlayer.bringToFront()
            fullPlayer.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in))
        } else {
            val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
            fullPlayer.startAnimation(zoomOut)
            zoomOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    fullPlayer.visibility = View.GONE
                    miniPlayer.visibility = View.VISIBLE
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    private fun updateMusicPlayerUI() {
        homeViewModel.currentAlbumCoverUrl?.let { albumCoverUrl ->
            val fullCoverImage: ImageView? = view?.findViewById(R.id.fullAlbumArt)
            val coverImage: ImageView? = view?.findViewById(R.id.albumArt)

            fullCoverImage?.let {
                Picasso.get().load(albumCoverUrl).into(it)
            }
            coverImage?.let {
                Picasso.get().load(albumCoverUrl).into(it)
            }
        }

        homeViewModel.currentSongTitle?.let { songTitle ->
            val songNameTextView: TextView? = view?.findViewById(R.id.songName)
            val fullSongNameTextView: TextView? = view?.findViewById(R.id.fullSongName)
            songNameTextView?.text = songTitle
            fullSongNameTextView?.text = songTitle
        }

        homeViewModel.currentArtistName?.let { artistName ->
            val artistNameTextView: TextView? = view?.findViewById(R.id.artistName)
            val fullArtistNameTextView: TextView? = view?.findViewById(R.id.fullArtistName)
            artistNameTextView?.text = artistName
            fullArtistNameTextView?.text = artistName
        }

        homeViewModel.dominantColor?.let { dominantColor ->
            homeViewModel.vibrantColor?.let { vibrantColor ->
                applyGradientBackground(dominantColor, vibrantColor)
            }
        }

        MusicPlayer.mediaPlayer?.let {
            if (it.isPlaying) {
                miniPlayer.visibility = View.VISIBLE
                fullPlayer.visibility = View.GONE
                setPlayPauseIcon(android.R.drawable.ic_media_pause)
            }
        }
    }
}

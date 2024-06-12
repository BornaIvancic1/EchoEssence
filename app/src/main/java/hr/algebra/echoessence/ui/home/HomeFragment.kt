package hr.algebra.echoessence.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.echoessence.R
import hr.algebra.echoessence.adapter.MyAdapter
import hr.algebra.echoessence.adapter.OnAlbumClickListener
import hr.algebra.echoessence.databinding.FragmentHomeBinding
import hr.algebra.echoessence.singleton.MusicPlayer

class HomeFragment : Fragment(), OnAlbumClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 1)

        val playPauseButton = root.findViewById<ImageButton>(R.id.playPauseButton)
        playPauseButton.setOnClickListener {
            playPauseMusic()
        }

        val coverImage = root.findViewById<ImageView>(R.id.albumArt)

        homeViewModel.music.observe(viewLifecycleOwner, Observer { music ->
            Log.d("HomeFragment", "Number of music: ${music.data.size}")
            if (music.data.isEmpty()) {
                Toast.makeText(context, "No music available", Toast.LENGTH_SHORT).show()
            } else {
                recyclerView.adapter = activity?.let { MyAdapter(it, music.data, this, ::playPauseMusic) }
            }
        })

        val searchInput = root.findViewById<EditText>(R.id.searchInput)
        val searchButton = root.findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.trim().isNotEmpty()) {
                homeViewModel.searchMusic(requireContext(), query)
                searchInput.setText("")
            } else {
                Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAlbumClick(albumCoverUrl: String) {
        val coverImage = view?.findViewById<ImageView>(R.id.albumArt)
        coverImage?.let {
            Picasso.get().load(albumCoverUrl).into(it)
        }
    }

    override fun onPlayPauseClick() {
        playPauseMusic()
    }

    fun playPauseMusic() {
        val playPauseButton = view?.findViewById<ImageButton>(R.id.playPauseButton)
        if (MusicPlayer.mediaPlayer?.isPlaying == true) {
            MusicPlayer.mediaPlayer?.pause()
            playPauseButton?.setImageResource(android.R.drawable.ic_media_play)
        } else if (MusicPlayer.mediaPlayer != null) {
            MusicPlayer.mediaPlayer?.start()
            playPauseButton?.setImageResource(android.R.drawable.ic_media_pause)
        } else {
            Toast.makeText(context, "No music selected", Toast.LENGTH_SHORT).show()
        }
    }
}

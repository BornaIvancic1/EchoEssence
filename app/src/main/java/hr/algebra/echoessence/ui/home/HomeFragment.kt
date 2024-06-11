package hr.algebra.echoessence.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.echoessence.R
import hr.algebra.echoessence.adapter.MyAdapter
import hr.algebra.echoessence.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

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

        homeViewModel.music.observe(viewLifecycleOwner, Observer { music ->
            Log.d("HomeFragment", "Number of music: ${music.data.size}")
            if (music.data.isEmpty()) {
                Toast.makeText(context, "No music available", Toast.LENGTH_SHORT).show()
            } else {
                recyclerView.adapter = activity?.let { MyAdapter(it,music.data) }
            }
        })

        val searchInput = root.findViewById<EditText>(R.id.searchInput)
        val searchButton = root.findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            homeViewModel.searchMusic(requireContext(), query)
            searchInput.setText("") // Clear the search input field
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

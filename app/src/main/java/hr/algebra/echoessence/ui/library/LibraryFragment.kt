package hr.algebra.echoessence.ui.library

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.echoessence.R
import hr.algebra.echoessence.adapter.LibraryAdapter
import hr.algebra.echoessence.dao.LibraryRepository
import hr.algebra.echoessence.model.Library

class LibraryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var libraryAdapter: LibraryAdapter
    private lateinit var libraryRepository: LibraryRepository
    private var libraryList: MutableList<Library> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewLibrary)
        libraryRepository = LibraryRepository(requireContext())

        libraryAdapter = LibraryAdapter(requireActivity(), libraryList) { library ->
            // Handle delete action
            libraryRepository.deleteLibraryEntry(library.id)
            libraryList.remove(library)
            libraryAdapter.notifyDataSetChanged()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = libraryAdapter

        loadLibraryEntries()
    }

    private fun loadLibraryEntries() {
        val currentUserId = getCurrentUserId()
        if (currentUserId != null) {
            libraryList.clear()
            val entries = libraryRepository.getLibraryEntriesByUserId(currentUserId)
            if (entries != null) {
                libraryList.addAll(entries)
                libraryAdapter.notifyDataSetChanged()
            } else {
                // Handle error loading entries
                // Show a message to the user or log the error
            }
        } else {
            // Handle error when user ID is not found
            // Show a message to the user or log the error
        }
    }

    private fun getCurrentUserId(): Int? {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("userId", -1).takeIf { it != -1 }
    }
}

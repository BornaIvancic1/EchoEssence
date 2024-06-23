package hr.algebra.echoessence.ui.library

import hr.algebra.echoessence.adapter.LibraryAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.echoessence.R
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
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewLibrary)
        libraryRepository = LibraryRepository(requireContext())

        libraryAdapter = LibraryAdapter(requireActivity(), libraryList,
            onDeleteClickListener = { library ->
                libraryRepository.deleteLibraryEntry(library.id)
                libraryList.remove(library)
                libraryAdapter.notifyDataSetChanged()
            },
            onUpdateNoteClickListener = { library ->
                // Handle update note action
                libraryRepository.updateLibraryEntry(library)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = libraryAdapter

        loadLibraryEntries()
    }

    fun updateLibraryData(libraryEntries: List<Library>) {
        libraryList.clear()
        libraryList.addAll(libraryEntries)
        libraryAdapter.notifyDataSetChanged()
    }

    private fun loadLibraryEntries() {
        val currentUserId = getCurrentUserId()
        if (currentUserId != null) {
            libraryList.clear()
            val entries = libraryRepository.getLibraryEntriesByUserId(currentUserId)
            if (entries != null) {
                libraryList.addAll(entries)
                libraryAdapter.notifyDataSetChanged()
            }
        }
    }

    fun onUserLoggedIn() {
        loadLibraryEntries()
    }

    private fun getCurrentUserId(): Int? {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("userId", -1).takeIf { it != -1 }
    }

    override fun onResume() {
        super.onResume()
        loadLibraryEntries()
    }
}

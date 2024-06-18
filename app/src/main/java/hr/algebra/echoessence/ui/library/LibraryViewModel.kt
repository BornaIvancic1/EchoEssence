package hr.algebra.echoessence.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.algebra.echoessence.model.Library

class LibraryViewModel : ViewModel() {
    private val _libraryList = MutableLiveData<List<Library>>()
    val libraryList: LiveData<List<Library>> = _libraryList
    private val _text = MutableLiveData<String>().apply {
        value = "This is library Fragment"
    }
    val text: LiveData<String> = _text
}
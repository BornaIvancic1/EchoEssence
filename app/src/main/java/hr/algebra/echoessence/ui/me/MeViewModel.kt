package hr.algebra.echoessence.ui.me

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val email = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val photoUrl = MutableLiveData<String>()
}
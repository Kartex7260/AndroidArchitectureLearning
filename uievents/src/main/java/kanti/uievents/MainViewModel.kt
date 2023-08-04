package kanti.uievents

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val TAG = "VM"

    private val _textLiveData: MutableLiveData<TextUiState> = MutableLiveData(TextUiState("", null))
    val textLiveData: LiveData<TextUiState> = _textLiveData

    fun updateText() {
        _textLiveData.value = _textLiveData.value?.copy(text = "Text from ViewModel", err = null)
        Log.d(TAG, "ui state: text")
    }

    fun updateTextErr() {
        _textLiveData.value = _textLiveData.value?.copy(text = "", err = "Error from ViewModel")
        Log.d(TAG, "ui state: show error")
    }

    fun removeErrMsg() {
        _textLiveData.value = _textLiveData.value?.copy(err = null)
        Log.d(TAG, "ui state: hide error")
    }

}
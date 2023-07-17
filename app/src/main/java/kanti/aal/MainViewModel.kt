package kanti.aal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kanti.aal.lib.BaseDate
import kanti.aal.lib.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val bd = BaseDate()

    private var job: Job? = null

    private val _itemsLiveData: MutableLiveData<ItemsUiState> = MutableLiveData()
    val itemsLiveData: LiveData<ItemsUiState> = _itemsLiveData

    fun addElement(text: String) {
        bd.add(Item(text))
        updateItemsLiveData()
    }

    fun removeElement(text: String) {
        bd.remove(Item(text))
        updateItemsLiveData()
    }

    private fun updateItemsLiveData() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Default) {
            val items = ArrayList<ItemUiState>()
            bd.forEach {
                items.add(ItemUiState(it.text) {
                    removeElement(it.text)
                })
            }
            _itemsLiveData.postValue(ItemsUiState(items))
        }
    }
}
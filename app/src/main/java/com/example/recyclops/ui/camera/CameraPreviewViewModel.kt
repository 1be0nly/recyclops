package com.example.recyclops.ui.camera

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclops.data.TrashScanned

@SuppressLint("NullSafeMutableLiveData")
class CameraPreviewViewModel : ViewModel() {

    private var _scannedTrash = MutableLiveData<List<TrashScanned>>()
    val scannedTrash: LiveData<List<TrashScanned>>
        get() = _scannedTrash



    fun addListTrashScanned(listTrashScanned: List<TrashScanned>) {
        _scannedTrash.value = listTrashScanned
    }

    fun increaseQuantity(id: Int) {
        val list = _scannedTrash.value?.toMutableList()
        val index = list?.indexOfFirst { it.id == id }
        if (index != null && index >= 0) {
            list[index].quantity += 1
            _scannedTrash.value = list
        }
    }

    fun decreaseQuantity(id: Int) {
        val list = _scannedTrash.value?.toMutableList()
        val index = list?.indexOfFirst { it.id == id }
        if (index != null && index >= 0) {
            if (list[index].quantity == 1)  {
                _scannedTrash.value = list
            } else {
                list[index].quantity -= 1
                _scannedTrash.value = list
            }
        }
    }

    fun deleteItem(id: Int) {
        val list = _scannedTrash.value?.toMutableList()
        val index = list?.indexOfFirst { it.id == id }
        if (index != null ) {
            list.removeAt(index)
            _scannedTrash.value = list
        }
    }

}
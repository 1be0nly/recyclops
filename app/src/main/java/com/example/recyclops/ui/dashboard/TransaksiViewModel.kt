package com.example.recyclops.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclops.R
import com.example.recyclops.data.TrashScanned

class TransaksiViewModel : ViewModel() {

    private val _dataList = MutableLiveData<List<TrashScanned>>(generateFakeData())
    val dataList: LiveData<List<TrashScanned>>
        get() = _dataList

//    init {
//        _dataList.value = listOf(
//            TrashScanned(1,"Trash 1", 1,11, R.drawable.img_botol_kaca),
//            TrashScanned(2,"Trash 2", 1,9, R.drawable.img_kardus),
//            TrashScanned(3,"Trash 3", 1,6,R.drawable.img_styrofoam),
//        )
//    }

    fun updateData(newList: List<TrashScanned>) {
        _dataList.value = newList
    }

    fun generateFakeData(): List<TrashScanned> {
        return listOf(
            TrashScanned(1,"Trash 1", 5,11, R.drawable.img_botol_kaca),
            TrashScanned(2,"Trash 2", 10,9, R.drawable.img_kardus),
            TrashScanned(3,"Trash 3", 7,6,R.drawable.img_styrofoam),
            TrashScanned(4,"Trash 4", 3,15,R.drawable.img_botol_kaca),
            TrashScanned(5,"Trash 5", 2,10,R.drawable.img_styrofoam),
            TrashScanned(6,"Trash 6", 1,13,R.drawable.img_kardus),
            TrashScanned(7,"Trash 7", 4,5,R.drawable.img_styrofoam),
        )
    }

    fun increaseQuantity(id: Int) {
        val updatedDataList = _dataList.value?.toMutableList()
        val position = updatedDataList?.indexOfFirst { it.id == id }
        position?.let {
            updatedDataList[it].quantity++
            _dataList.value = updatedDataList
        }
    }

    fun decreaseQuantity(id: Int) {
        val updatedDataList = _dataList.value?.toMutableList()
        val position = updatedDataList?.indexOfFirst { it.id == id }
        position?.let {
            if (updatedDataList[it].quantity > 0) {
                updatedDataList[it].quantity--
            }
            _dataList.value = updatedDataList
        }
    }

    fun deleteItem(id: Int) {
        val updatedDataList = _dataList.value?.toMutableList()
        val position = updatedDataList?.indexOfFirst { it.id == id }
        position?.let {
            updatedDataList.removeAt(it)
            _dataList.value = updatedDataList
        }
    }


}
package dev210202.goingtohakwon

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/*
    temp를 사용해서 list를 새롭게 변경하는 이유

    기존 방식은 항상 동일한 리스트 인스턴스를 사용해서 추가, 제거 등을 수행하여 list의 아이템의 참조가 같은거로 판단해서 recyclerview의 item이 업데이트 되지 않는다.
    따라서 temp를 새로 생성하여 리스트를 받은다음 temp에서 변경한 뒤 다시 리스트의 값에 반영하게 한다.
*/
class MutableListLiveData<T> : MutableLiveData<List<T>>() {

    init {
        value = mutableListOf()
    }
    fun add(item: T) {
        val temp = value!!.toMutableList()
        temp.add(item)
        value = temp
    }

    fun addAll(items: List<T>) {
        val temp = value!!.toMutableList()
        temp.addAll(items)
        value = temp
    }

    fun remove(item: T) {
        val temp = value!!.toMutableList()
        temp.remove(item)
        value = temp
    }

    fun clear() {
        val temp = value!!.toMutableList()
        temp.clear()
        value = temp
    }

    fun set(index: Int, element: T){
        val temp = value!!.toMutableList()
        temp[index] = element
        value = temp
    }
}
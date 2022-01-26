package com.example.myshoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.myshoppinglist.domain.ShopItem

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return  oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //проверяем это то же элемент или новый
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id //если id совпадают, то true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // проверяем на содержание: если поля изменились, значит объект нужно перерисовать,
        // если не изменились, то не нужно перерисовывать
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem // произойдет сравнение по иквалс, но т.к. у data class этот метод
        // переопределен, то произойдет сравнение по всем полям в первичном конструкторе
    }
}
package com.example.myshoppinglist.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem
import com.example.myshoppinglist.presentation.ShopListAdapter.*

class ShopListAdapter: androidx.recyclerview.widget.ListAdapter<ShopItem,
        ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null // функция принимает ShopItem и
    // ничего не возращает
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_DISABLE -> R.layout.item_shop_disabled
            VIEW_TYPE_ENABLE -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout,parent,false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        viewHolder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
        true // можем вызвать функцию только в том случае, если переменная не равна null
        }
        viewHolder.view.setOnClickListener() {
            onShopItemClickListener?.invoke(shopItem)
            }

        viewHolder.tvName.text = shopItem.name
        viewHolder.tvCount.text = shopItem.count.toString()
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) VIEW_TYPE_ENABLE
        else VIEW_TYPE_DISABLE
    }

    companion object{
        const val VIEW_TYPE_ENABLE = 100
        const val VIEW_TYPE_DISABLE = 101
        const val MAX_POOL_SIZE = 15
    }
}
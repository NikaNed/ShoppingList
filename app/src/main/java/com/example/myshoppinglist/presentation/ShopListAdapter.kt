package com.example.myshoppinglist.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var count = 0
    var shopList = listOf<ShopItem>()
        set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter","onCreateViewHolder, count: ${++count}")
        val layout = when (viewType) {
            VIEW_TYPE_ENABLE -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLE -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout,parent,false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        viewHolder.view.setOnClickListener() {
            true
        }
        viewHolder.tvName.text = shopItem.name
        viewHolder.tvCount.text = shopItem.count.toString()
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) VIEW_TYPE_ENABLE
        else VIEW_TYPE_DISABLE
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    class ShopItemViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }

    companion object{
        const val VIEW_TYPE_ENABLE = 100
        const val VIEW_TYPE_DISABLE = 101
        const val MAX_POOL_SIZE = 20
    }
}
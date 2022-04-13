package com.example.myshoppinglist.presentation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.myshoppinglist.R
import com.example.myshoppinglist.databinding.ItemShopDisabledBinding
import com.example.myshoppinglist.databinding.ItemShopEnabledBinding
import com.example.myshoppinglist.domain.ShopItem


class ShopListAdapter : ListAdapter<ShopItem,
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
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = viewHolder.binding
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true // можем вызвать функцию только в том случае, если переменная не равна null
        }
        binding.root.setOnClickListener() {
            onShopItemClickListener?.invoke(shopItem)
        }
        when (binding) {
            is ItemShopDisabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
            is ItemShopEnabledBinding -> {
                binding.tvName.text = shopItem.name
                binding.tvCount.text = shopItem.count.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) VIEW_TYPE_ENABLE
        else VIEW_TYPE_DISABLE
    }

    companion object {
        const val VIEW_TYPE_ENABLE = 100
        const val VIEW_TYPE_DISABLE = 101
        const val MAX_POOL_SIZE = 15
    }
}
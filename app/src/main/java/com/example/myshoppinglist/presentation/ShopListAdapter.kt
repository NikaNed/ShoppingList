package com.example.myshoppinglist.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R
import com.example.myshoppinglist.domain.ShopItem

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var count = 0
    var shopList = listOf<ShopItem>()
        set(value) { // устанавливаем новое значение в список shopList
        val callback = ShopListDiffCallback(shopList, value)//создаем экземпляр класса, в параметры
        // передаем два списка: старый и новый
        val diffResult = DiffUtil.calculateDiff(callback) //из класса DiffUtil вызываем статический
        //метод  calculateDiff. Он произведет вычисления и вернёт в diffResult. В этом объекте будут
        //храниться все изменения, которые необходимы сделать адаптеру
        diffResult.dispatchUpdatesTo(this) //вызываем метод dispatchUpdatesTo, чтобы адаптер
        // сделал эти изменения
        field = value //обновляем список
    }

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
        Log.d("ShopListAdapter","onBindViewHolder, count: ${++count}")
        val shopItem = shopList[position]
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

    override fun onViewRecycled(viewHolder: ShopItemViewHolder) {
       super.onViewRecycled(viewHolder)
        viewHolder.tvName.text = ""
        viewHolder.tvCount.text = ""
        viewHolder.tvName.setTextColor(ContextCompat.getColor(
           viewHolder.view.context,
          android.R.color.white)) }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) VIEW_TYPE_ENABLE
        else VIEW_TYPE_DISABLE
    }

    class ShopItemViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }

    interface OnShopItemLongClickListener {
        fun onShopItemLongClick(shopItem: ShopItem)
    }


    companion object{
        const val VIEW_TYPE_ENABLE = 100
        const val VIEW_TYPE_DISABLE = 101
        const val MAX_POOL_SIZE = 15
    }
}
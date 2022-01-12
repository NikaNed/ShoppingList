package com.example.myshoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this){
            shopListAdapter.shopList = it // в адаптер вставим новый лист

        }
    }

    private fun setUpRecyclerView() { //настраиваем RecyclerView
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list) //получаем RecyclerView
        with (rvShopList){
            shopListAdapter = ShopListAdapter() // создаем адаптер
            adapter = shopListAdapter // устанавлием адаптер у RecyclerView

            recycledViewPool.setMaxRecycledViews( // устанавлием адаптер
                ShopListAdapter.VIEW_TYPE_ENABLE,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLE,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }


    }
}
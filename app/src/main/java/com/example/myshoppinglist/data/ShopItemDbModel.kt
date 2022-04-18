package com.example.myshoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "shop_items")
data class ShopItemDbModel(
    @PrimaryKey (autoGenerate = true) //id генерируется автоматически
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
)
package com.example.myshoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopListDao {

    @Query ("SELECT*FROM shop_items") //запрос выбираем все из таблицы shop_items
    fun getShopList(): LiveData<List<ShopItemDbModel>> //метод запрашивает данные из базы

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItemDbModel: ShopItemDbModel) //метод не только добавляет элемент, но и
    // редактирует его

    @Query("DELETE FROM shop_items WHERE id =:shopItemId" )
    suspend fun deleteShopItem(shopItemId: Int)

    @Query("SELECT*FROM shop_items WHERE id=:shopItemId LIMIT 1" )
    suspend fun getShopItem(shopItemId: Int): ShopItemDbModel
}
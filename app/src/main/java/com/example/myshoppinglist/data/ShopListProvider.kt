package com.example.myshoppinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.myshoppinglist.domain.ShopItem
import com.example.myshoppinglist.presentation.ShopListApp
import javax.inject.Inject

class ShopListProvider: ContentProvider() {

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val component by lazy {
        (context as ShopListApp).component
    }


    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.example.myshoppinglist", "shop_item", GET_SHOP_ITEM_QUERY)
        addURI("com.example.myshoppinglist", "shop_item/#", GET_SHOP_BY_ID_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            GET_SHOP_ITEM_QUERY -> {
                shopListDao.getShopListCursor()
            }
            else -> { null
            }
        }
    }


    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when(uriMatcher.match(uri)){
            GET_SHOP_ITEM_QUERY -> {
                if (values == null) return null
                val id = values.getAsInteger(INSERT_ID)
                val name = values.getAsString(INSERT_NAME)
                val count = values.getAsInteger(INSERT_COUNT)
                val enabled = values.getAsBoolean(INSERT_ENABLED)
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                shopListDao.addShopItemProvider(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when(uriMatcher.match(uri)){
            GET_SHOP_ITEM_QUERY -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                shopListDao.deleteShopItemProvider(id)
            }
        }
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        when(uriMatcher.match(uri)){
            GET_SHOP_ITEM_QUERY -> {
                val idUpdate = selectionArgs?.get(0)?.toInt() ?: -1
                shopListDao.deleteShopItemProvider(idUpdate)

                if (values == null) return 0
                val id = values.getAsInteger(INSERT_ID)
                val name = values.getAsString(INSERT_NAME)
                val count = values.getAsInteger(INSERT_COUNT)
                val enabled = values.getAsBoolean(INSERT_ENABLED)
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                shopListDao.addShopItemProvider(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return 0
    }

    companion object{
        const val GET_SHOP_ITEM_QUERY = 10
        const val GET_SHOP_BY_ID_QUERY = 11
        const val INSERT_ID = "id"
        const val INSERT_NAME = "name"
        const val INSERT_COUNT = "count"
        const val INSERT_ENABLED = "enabled"
    }
}
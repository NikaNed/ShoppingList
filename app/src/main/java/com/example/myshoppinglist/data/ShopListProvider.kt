package com.example.myshoppinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.myshoppinglist.presentation.ShopListApp
import javax.inject.Inject

class ShopListProvider: ContentProvider() {

    @Inject
    lateinit var shopListDao: ShopListDao

    val component by lazy {
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
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    companion object{
        const val GET_SHOP_ITEM_QUERY = 10
        const val GET_SHOP_BY_ID_QUERY = 11
    }
}
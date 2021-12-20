package com.example.myshoppinglist.data

import com.example.myshoppinglist.domain.ShopItem
import com.example.myshoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init {
        for (i in 1 until 10) {
            val item = ShopItem("Name $i", i, enabled = true)
            addShopItem(item)
        }

    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINDED_ID){ //если при добавлении нового элемента id не найден, то создаем id//
            shopItem.id = autoIncrementId++
        }
       shopList.add(shopItem) //в противном случае просто добавим объект в коллекцию
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id) // чтобы отредактировать элемент, сначала получаем id старого объекта
        shopList.remove(oldItem) // затем удалаяем его из коллекции
        addShopItem(shopItem) // и добавляем новый объект на его место
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { // этот метод найдет элемент по его id и вернет его
            it.id ==  shopItemId // если возвращаемый тип данных никак не может быть null, то бросаем исключение
        } ?: throw RuntimeException("Element with id $shopItemId not found") // если элемент не найден с таким id, то приложение упадет
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList() // возвращаем копию shopList
    }
}
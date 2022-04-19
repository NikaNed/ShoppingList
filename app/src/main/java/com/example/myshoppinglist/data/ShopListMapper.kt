package com.example.myshoppinglist.data

import com.example.myshoppinglist.domain.ShopItem

class ShopListMapper {

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel( //метод преобразовывает сущность
        //domain-слоя в модель БД
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enabled = shopItem.enabled
        )
    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem( //метод преобразовывает
        // модель БД в сущность domain-слоя
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enabled = shopItemDbModel.enabled
    )

    fun mapDbModelListToListEntity (list: List<ShopItemDbModel>) = list.map {
        mapDbModelToEntity(it) //для каждого элемента коллекции вызовем метод
    } //метод преобразовывает List объектов ShopItemDbModel в List объектов ShopItem
}
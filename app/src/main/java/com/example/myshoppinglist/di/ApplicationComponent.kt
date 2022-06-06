package com.example.myshoppinglist.di

import android.app.Application
import android.content.ContentProvider
import com.example.myshoppinglist.data.ShopListProvider
import com.example.myshoppinglist.presentation.MainActivity
import com.example.myshoppinglist.presentation.ShopItemFragment
import com.example.myshoppinglist.presentation.ShopListApp
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [ViewModelModule::class, DataModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemFragment)
    fun inject(application: ShopListApp)
    fun inject(provider: ShopListProvider)

    @Component.Factory
    interface ApplicationComponentFactory{
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
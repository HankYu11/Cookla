package com.flowerish.cookla.di

import android.content.Context
import androidx.room.Room
import com.flowerish.cookla.database.FridgeDao
import com.flowerish.cookla.database.FridgeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FridgeDatabase{
        return Room.databaseBuilder(
            appContext,
            FridgeDatabase::class.java,
            "com.flowerish.com.flowerish.cookla")
            .build()
    }

    @Provides
    fun getDao(database: FridgeDatabase): FridgeDao{
        return database.getDao()
    }
}
package dev.yuanzix.jettodo.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.yuanzix.jettodo.data.ToDoDatabase
import dev.yuanzix.jettodo.util.Constants.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, ToDoDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideDao(
        database: ToDoDatabase,
    ) = database.toDoDao()
}
/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.poketmontest.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.poketmontest.model.Pokemon


//@Database(entities = [Pokemon::class, PokemonInfo::class], version = 1, exportSchema = true)
//@TypeConverters(value = [TypeResponseConverter::class])
@Database(entities = [Pokemon::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

  abstract fun pokemonDao(): PokemonDao
 // abstract fun pokemonInfoDao(): PokemonInfoDao

  companion object {
    private var instance: AppDatabase? = null

    @Synchronized
    fun getInstance(context: Context): AppDatabase? {
      if (instance == null) {
        synchronized(AppDatabase::class){
          instance = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "PokedexTest.db"
          ).build()
        }
      }
      return instance
    }
  }
}

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

package com.example.poketmontest.repository

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.example.poketmontest.db.AppDatabase
import com.example.poketmontest.db.PokemonDao
import com.example.poketmontest.model.PokemonResponse
import com.example.poketmontest.network.PokedexAPI
import com.example.poketmontest.network.PokedexClient
import com.example.poketmontest.network.PokedexService
import com.example.poketmontest.ui.main.MainActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Inject

class MainRepository(
    private val application: Application
) : Repository {

    fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {

        var db = AppDatabase.getInstance(application)
        var pokemons = db!!.pokemonDao().getPokemonList(page)

        // var pokemons = pokemonDao.getPokemonList(page)
        if (pokemons.isEmpty()) {
            /**
             * fetches a list of [Pokemon] from the network and getting [ApiResponse] asynchronously.
             * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#apiresponse-extensions-for-coroutines)
             */

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            val pokedexClient = retrofit.create(PokedexAPI::class.java)

            CoroutineScope(Dispatchers.Main).launch {
                val response = pokedexClient.fetchPokemonList(page)
                try {
                    pokemons = response.results
                    pokemons.forEach { pokemon ->
                        pokemon.page = page
                    }
                    db!!.pokemonDao().insertPokemonList(pokemons)
                    emit(db!!.pokemonDao().getAllPokemonList(page))

                } catch (e: Exception) {
                    Timber.w(e)
                }

            }

            /*           val response = pokedexClient.fetchPokemonList(page = page)
                       GlobalScope.launch {

                           try {
                               if (response.isSuccessful) {
                                   pokemons = response.body()!!.results
                                   pokemons.forEach { pokemon ->
                                       pokemon.page = page
                                   }
                                   pokemonDao.insertPokemonList(pokemons)
                                   emit(pokemonDao.getAllPokemonList(page))
                               } else {
                                   Timber.w(response.errorBody().toString())
                               }
                           } catch (e: Exception) {
                               Timber.w(e)
                           }
                       }*/
        } else {
            emit(db!!.pokemonDao().getAllPokemonList(page))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }
}

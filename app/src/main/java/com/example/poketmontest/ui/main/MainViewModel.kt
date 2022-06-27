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

package com.example.poketmontest.ui.main

import android.app.Application
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poketmontest.model.Pokemon
import com.example.poketmontest.network.PokedexClient
import com.example.poketmontest.repository.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel constructor(
  mainRepository: MainRepository
) : ViewModel() {
  var isLoading: Boolean = false
    private set
  var toastMessage: String? = null
    private set
  var pokemonList: List<Pokemon>? = null
    private set


  private var _pokemonListString = MutableStateFlow(pokemonList)
  var pokemonListString = _pokemonListString.asStateFlow()

  private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)


  init {
   viewModelScope.launch { pokemonFetchingIndex.flatMapLatest { page ->
      mainRepository.fetchPokemonList(
        page = page,
        onStart = { isLoading = true },
        onComplete = { isLoading = false },
        onError = { toastMessage = it }
      )
    }.collect { v->
      pokemonList = v
     _pokemonListString.value = pokemonList
      Log.d("JIWOUNG", "DBTest: " + pokemonListString)
    }}



    Timber.d("init MainViewModel")
  }

  fun fetchNextPokemonList() {
    if (!isLoading) {
      pokemonFetchingIndex.value++
    }
  }
}

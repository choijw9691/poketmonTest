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

package com.example.poketmontest.bindingAdapter

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.poketmontest.model.Pokemon
import com.example.poketmontest.ui.adapter.PokemonAdapter
import com.example.poketmontest.ui.main.MainViewModel


object RecyclerViewBinding {

  @JvmStatic
  @BindingAdapter("adapter")
  fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter.apply {
      stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
  }


  @BindingAdapter("submitList")
  @JvmStatic
  fun RecyclerView.bindAdapter(itemList: List<Pokemon>?){

    // 어댑터 최초 연결
    if(this.adapter == null) {
      val adapter = PokemonAdapter()
      this.adapter = adapter
    }

    val myAdapter = this.adapter as PokemonAdapter

    // 자동 갱신
    myAdapter.submitList(itemList?.toMutableList())
  }

/*
  @JvmStatic
  @BindingAdapter("submitList")
  fun bindSubmitList(view: RecyclerView, itemList: List<Pokemon>?) {
    // 어댑터 최초 연결
    if(view.adapter == null) {
      val adapter = PokemonAdapter()
      view.adapter = adapter
    }

    val pokemonAdapter = view.adapter as PokemonAdapter

    // 자동 갱신
    pokemonAdapter.submitList(itemList)
  }
*/
/*  @JvmStatic
  @BindingAdapter("paginationPokemonList")
  fun paginationPokemonList(view: RecyclerView, viewModel: MainViewModel) {
    RecyclerViewPaginator(
      recyclerView = view,
      isLoading = { viewModel.isLoading },
      loadMore = { viewModel.fetchNextPokemonList() },
      onLast = { false }
    ).run {
      threshold = 8
    }
  }*/
}

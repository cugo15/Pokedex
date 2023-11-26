package com.cugocumhurgunay.pokedex.presentation.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RadioButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cugocumhurgunay.pokedex.R
import com.cugocumhurgunay.pokedex.domain.model.list.PokeListItem
import com.cugocumhurgunay.pokedex.domain.repository.PokeRepo
import com.cugocumhurgunay.pokedex.domain.usecase.GetPokemonListUseCase
import com.cugocumhurgunay.pokedex.utils.Constants.PAGE_SIZE
import com.cugocumhurgunay.pokedex.utils.Status
import com.cugocumhurgunay.pokedex.utils.extractPokemonId
import com.cugocumhurgunay.pokedex.utils.getPokemonImageUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getPokemonListUseCase:GetPokemonListUseCase) : ViewModel() {

    val pokemonList = MutableLiveData<List<PokeListItem>>()
    val endReached = MutableLiveData<Boolean>(false)
    val searchedPokemonList = MutableLiveData<List<PokeListItem>>()
    val filtredPokemonList = MutableLiveData<List<PokeListItem>>()


    private var curPage = 0

    val mutableIsLoading = MutableLiveData<Boolean>(false)
    val mutableError = MutableLiveData<Boolean?>(false)

    fun loadData() {
        viewModelScope.launch {
            if (searchedPokemonList.value.isNullOrEmpty()&&!endReached.value!!) {
                mutableIsLoading.value = true
                delay(1000)
                val result = getPokemonListUseCase(PAGE_SIZE, curPage * PAGE_SIZE)
                when (result.status) {
                    Status.SUCCESS -> {
                        endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                        val pokedexEntries = result.data.results.map { entry ->
                            val id = extractPokemonId(entry.url)
                            val url = getPokemonImageUrl(id!!)
                            PokeListItem(entry.name.capitalize(Locale.ROOT), url, id)
                        }
                        curPage++
                        mutableIsLoading.value = false
                        mutableError.value = false
                        pokemonList.value = (pokemonList.value ?: emptyList()) + pokedexEntries
                    }
                    Status.ERROR -> {
                        mutableError.value = true
                        mutableIsLoading.value = false
                    }
                    Status.LOADING -> {
                        mutableIsLoading.value = true
                        mutableError.value = false
                    }
                }
            }
        }
    }

    fun searchPokemonList(query: String) {
        viewModelScope.launch {
            val filteredList = pokemonList.value?.filter {
                it.pokemonName.lowercase().contains(query.lowercase())
            } ?: emptyList()

            searchedPokemonList.value = if (filteredList == pokemonList.value) {
                emptyList()
            } else {
                ArrayList(filteredList)
            }
        }
    }

    fun orderPokemonListByName() {
        viewModelScope.launch {
            filtredPokemonList.value = pokemonList.value?.sortedBy { it.pokemonName }
        }
    }

}


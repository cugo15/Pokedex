package com.cugocumhurgunay.pokedex.presentation.viewmodel


import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cugocumhurgunay.pokedex.domain.model.list.PokeListItem
import com.cugocumhurgunay.pokedex.domain.usecase.GetPokemonListUseCase
import com.cugocumhurgunay.pokedex.utils.Constants.PAGE_SIZE
import com.cugocumhurgunay.pokedex.utils.Status
import com.cugocumhurgunay.pokedex.utils.extractPokemonId
import com.cugocumhurgunay.pokedex.utils.getPokemonImageUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getPokemonListUseCase:GetPokemonListUseCase) : ViewModel() {

    val pokemonList = MutableLiveData<List<PokeListItem>>()
    private var endReached = MutableLiveData<Boolean>(false)
    val searchedPokemonList = MutableLiveData<List<PokeListItem>>()
    val filtredPokemonList = MutableLiveData<List<PokeListItem>>()
    private var matched = false

    private var curPage = 0

    val mutableIsLoading = MutableLiveData<Boolean>(false)
    val mutableError = MutableLiveData<Boolean?>(false)

    fun loadData() {
        viewModelScope.launch {Dispatchers.IO
            // Check if there is not searching at the moment and the end is not reached
            if (!matched && !endReached.value!!) {
                mutableIsLoading.value = true
                delay(800)
                // Fetch Pokemon list from the use case
                val result = getPokemonListUseCase(PAGE_SIZE, curPage * PAGE_SIZE)
                // Handle the result based on its status
                when (result.status) {
                    Status.SUCCESS -> {
                        // update endReached value
                        endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                        // Map the results
                        val pokeItems = result.data.results.map { entry ->
                            val id = extractPokemonId(entry.url)
                            val url = getPokemonImageUrl(id!!)
                            PokeListItem(entry.name.capitalize(Locale.ROOT), url, id)
                        }
                        // Increment the current page to load rest of the data on other getData() call
                        curPage++
                        mutableIsLoading.value = false
                        mutableError.value = false
                        // Update the Pokemon list with the new entries
                        pokemonList.value = (pokemonList.value ?: emptyList()) + pokeItems
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
        viewModelScope.launch {Dispatchers.IO
            val isQueryNumeric = query.isDigitsOnly()
            val filteredList = pokemonList.value
                ?.let {
                    // If the query is empty, return the entire pokemon list
                    if (query.isNullOrEmpty()) it
                    else if (isQueryNumeric) {
                        // If the query is numeric, filter by pokemon ID
                        it.filter { pokemon -> pokemon.pokemonID.toString().contains(query) }
                    } else {
                        // If the query is not numeric, filter by pokemon name
                        it.filter { pokemon -> pokemon.pokemonName.lowercase().contains(query.lowercase()) }
                    }
                } ?: emptyList() // If pokemonList.value is null, return an empty list

            // Check if the filtered list is different from the original pokemon list
            if (filteredList != pokemonList.value) {
                // If there is a match with the query, update the searched Pokemon list
                searchedPokemonList.value = ArrayList(filteredList)
                matched = true
            } else {
                // If there is no match, reset the search to the entire pokemon list
                matched = false
                searchedPokemonList.value = pokemonList.value
            }
        }
    }

    fun orderPokemonListByName() {
        viewModelScope.launch {Dispatchers.IO
            filtredPokemonList.value = pokemonList.value?.sortedBy { it.pokemonName }
        }
    }
    fun orderPokemonListByID() {Dispatchers.IO
        viewModelScope.launch {
            filtredPokemonList.value = pokemonList.value?.sortedBy { it.pokemonID }
        }
    }

}


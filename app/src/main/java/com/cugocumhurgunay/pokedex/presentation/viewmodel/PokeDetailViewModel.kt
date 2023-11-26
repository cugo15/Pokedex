package com.cugocumhurgunay.pokedex.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetail
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.domain.model.list.PokeListItem
import com.cugocumhurgunay.pokedex.domain.model.mapper.PokeDetailMapper
import com.cugocumhurgunay.pokedex.domain.repository.PokeRepo
import com.cugocumhurgunay.pokedex.domain.usecase.GetPokeSpeciesUseCase
import com.cugocumhurgunay.pokedex.domain.usecase.GetPokemonDetailsUseCase
import com.cugocumhurgunay.pokedex.utils.Resource
import com.cugocumhurgunay.pokedex.utils.Status
import com.cugocumhurgunay.pokedex.utils.getPokemonImageUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeDetailViewModel @Inject constructor (
    private val getPokeSpeciesUseCase:GetPokeSpeciesUseCase,
    private val getPokemonDetailsUseCase:GetPokemonDetailsUseCase) : ViewModel() {

    val pokemonDetail = MutableLiveData<PokeDetailItem?>()
    val pokemonFlavorText = MutableLiveData<String?>()

    private val pokeDetailMapper = PokeDetailMapper()

    val mutableIsLoading = MutableLiveData<Boolean>()

    val mutableError = MutableLiveData<Boolean>(false)

    fun loadData(pokemonID: Int) {
        viewModelScope.launch {Dispatchers.IO
            mutableIsLoading.value = true
            // Fetch Pokemon details from the use case
            val result = getPokemonDetailsUseCase(pokemonID)
            // Handle the result based on its status
            when (result.status) {
                Status.SUCCESS -> {
                    mutableIsLoading.value = false
                    // Map the result to a presentation model
                    val pokeDetailItem = pokeDetailMapper.mapToPresentation(result.data)
                    // Update the Pokemon detail live data
                    pokemonDetail.value = pokeDetailItem
                    // Print the Pokemon detail value (for debugging)
                    println(pokemonDetail.value)
                    // Check for unexpected errors
                    if (result.data == null || pokeDetailItem == null) {
                        mutableError.value = true
                    }
                }
                Status.ERROR -> {
                    mutableError.value = true
                    mutableIsLoading.value = false
                }
                Status.LOADING -> {
                    mutableIsLoading.value = true
                }
            }
        }
    }

    fun loadSpecies(pokemonID: Int) {
        viewModelScope.launch {Dispatchers.IO
            mutableIsLoading.value = true

            // Fetch Pokemon species details from the use case
            val result = getPokeSpeciesUseCase(pokemonID)
            // Handle the result based on its status
            when (result.status) {
                Status.SUCCESS -> {
                    mutableIsLoading.value = false
                    // Get English flavor text entries and clean the text
                    val englishFlavorTextList = result?.data?.flavorTextEntries
                        ?.filter { it.language?.name == "en" && it.flavorText?.isNotBlank() == true }
                        ?.mapNotNull { it.flavorText }
                        ?: emptyList()
                    // Random flavor text to show different info every single time and clean it
                    val cleanedText = englishFlavorTextList.randomOrNull()?.replace(Regex("[^\\x20-\\x7E]"), "")
                    // Print the cleaned flavor text (for debugging)
                    print(cleanedText)
                    // Update the Pokemon flavor text live data
                    pokemonFlavorText.value = cleanedText
                }
                Status.ERROR -> {
                    mutableError.value = true
                    mutableIsLoading.value = false
                }
                Status.LOADING -> {
                    mutableIsLoading.value = true
                }
            }
        }
    }

}
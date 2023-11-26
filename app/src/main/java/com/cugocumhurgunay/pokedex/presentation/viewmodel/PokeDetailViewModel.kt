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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeDetailViewModel @Inject constructor (
    private val getPokeSpeciesUseCase:GetPokeSpeciesUseCase,
    private val getPokemonDetailsUseCase:GetPokemonDetailsUseCase) : ViewModel() {

    val pokemonDetail = MutableLiveData<PokeDetailItem?>()
    val pokemonFlavorText = MutableLiveData<String?>()

    private val pokeDetailMapper = PokeDetailMapper()

    private val mutablePokeDetail = MutableLiveData<PokeDetail?>()
    val pokeDetail: LiveData<PokeDetail?> get() = mutablePokeDetail

    private val mutableIsLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = mutableIsLoading

    private val mutableError = MutableLiveData<String?>()
    val error: LiveData<String?> get() = mutableError

    fun loadData(pokemonID: Int) {
        viewModelScope.launch {
            mutableIsLoading.value = true
            val result = getPokemonDetailsUseCase(pokemonID)
            when (result.status) {
                Status.SUCCESS -> {
                    mutableIsLoading.value = false
                    val pokeDetailItem = pokeDetailMapper.mapToPresentation(result.data)
                    pokemonDetail.value = pokeDetailItem
                    println(pokemonDetail.value)
                    if (result.data == null || pokeDetailItem == null) {
                        mutableError.value = "Unexpected error"
                    }
                }
                Status.ERROR -> {
                    mutableError.value = result.message
                    mutableIsLoading.value = false
                }
                Status.LOADING -> {
                    mutableIsLoading.value = true
                }
            }
        }
    }
    fun loadSpecies(pokemonID: Int) {
        viewModelScope.launch {
            mutableIsLoading.value = true
            val result = getPokeSpeciesUseCase(pokemonID)
            when (result.status) {
                Status.SUCCESS -> {
                    mutableIsLoading.value = false

                    val englishFlavorTextList = result?.data?.flavorTextEntries
                        ?.filter { it.language?.name == "en" && it.flavorText?.isNotBlank() == true }
                        ?.mapNotNull { it.flavorText }
                        ?: emptyList()

                    val cleanedText = englishFlavorTextList.randomOrNull()?.replace(Regex("[^\\x20-\\x7E]"), "")

                    print(cleanedText)
                    pokemonFlavorText.value = cleanedText
                }
                Status.ERROR -> {
                    mutableError.value = result.message
                    mutableIsLoading.value = false
                }
                Status.LOADING -> {
                    mutableIsLoading.value = true
                }
            }
        }
    }
}
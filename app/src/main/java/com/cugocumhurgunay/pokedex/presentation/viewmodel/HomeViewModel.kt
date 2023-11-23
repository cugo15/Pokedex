package com.cugocumhurgunay.pokedex.presentation.viewmodel


import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.pokedex.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (val pokemonRepository : PokemonRepository) : ViewModel(){
}
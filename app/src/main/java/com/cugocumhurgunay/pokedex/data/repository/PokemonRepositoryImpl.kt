package com.cugocumhurgunay.pokedex.data.repository

import com.cugocumhurgunay.pokedex.domain.repository.PokemonRepository
import com.cugocumhurgunay.pokedex.domain.usecase.PokemonUseCase
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor (val useCase: PokemonUseCase) : PokemonRepository {
}
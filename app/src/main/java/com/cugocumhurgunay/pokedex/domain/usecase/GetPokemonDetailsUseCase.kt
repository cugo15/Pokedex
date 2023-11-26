package com.cugocumhurgunay.pokedex.domain.usecase

import com.cugocumhurgunay.pokedex.domain.repository.PokeRepo
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(private val pokeRepo: PokeRepo) {
    suspend operator fun invoke(pokemonID: Int) = pokeRepo.getPokemonDetails(pokemonID)
}
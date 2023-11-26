package com.cugocumhurgunay.pokedex.domain.usecase

import com.cugocumhurgunay.pokedex.domain.repository.PokeRepo
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(private val pokeRepo: PokeRepo) {
    suspend operator fun invoke(limit: Int, offset: Int) = pokeRepo.getPokemonList(limit, offset)
}
package com.cugocumhurgunay.pokedex.domain.repository

import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetail
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokeSpecies
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokemonSpecie
import com.cugocumhurgunay.pokedex.domain.model.list.PokemonList
import com.cugocumhurgunay.pokedex.utils.Resource

interface PokeRepo {
    suspend fun getPokemonList(limit:Int,offset:Int) : Resource<PokemonList>

    suspend fun getPokemonDetails(pokemonID:Int) : Resource<PokeDetailItem>

    suspend fun getPokeSpecies(pokemonID:Int) : Resource<PokemonSpecie>
}
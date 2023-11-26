package com.cugocumhurgunay.pokedex.data.repository

import com.cugocumhurgunay.pokedex.data.retrofit.service.PokemonAPI
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetail
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokeSpecies
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokemonSpecie
import com.cugocumhurgunay.pokedex.domain.model.list.PokemonList
import com.cugocumhurgunay.pokedex.domain.model.mapper.mapToPresentation
import com.cugocumhurgunay.pokedex.domain.repository.PokeRepo
import com.cugocumhurgunay.pokedex.utils.Resource
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor (private val pokemonAPI: PokemonAPI) : PokeRepo {
    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList>{
        return try {
            val response = pokemonAPI.getPokemonList(limit, offset)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured",null)
            } else {
                Resource.error("An unknown error occured",null)
            }
        } catch (e: Exception) {
            Resource.error("An unknown error occured",null)
        }
    }

    override suspend fun getPokemonDetails(pokemonID: Int): Resource<PokeDetailItem> {
        return try {
            val response = pokemonAPI.getPokemonDetail(pokemonID)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it.mapToPresentation())
                } ?: Resource.error("An unknown error occured",null)
            } else {
                Resource.error("An unknown error occured",null)
            }
        } catch (e: Exception) {
            Resource.error("An unknown error occured",null)
        }
    }

    override suspend fun getPokeSpecies(pokemonID: Int): Resource<PokemonSpecie> {
        return try {
            val response = pokemonAPI.getPokemonSpecies(pokemonID)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it.mapToPresentation())
                } ?: Resource.error("An unknown error occured",null)
            } else {
                Resource.error("An unknown error occured",null)
            }
        } catch (e: Exception) {
            Resource.error("An unknown error occured",null)
        }
    }
}





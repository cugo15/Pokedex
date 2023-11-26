package com.cugocumhurgunay.pokedex.data.repository

import com.cugocumhurgunay.pokedex.data.retrofit.service.PokemonAPI
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetail
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokeSpecies
import com.cugocumhurgunay.pokedex.domain.model.list.PokemonList
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
                } ?: Resource.error("Body boş geldi!",null)
            } else {
                Resource.error("Response successful değil!",null)
            }
        } catch (e: Exception) {
            Resource.error("Bütün olay patladı!",null)
        }
    }

    override suspend fun getPokemonDetails(pokemonID: Int): Resource<PokeDetail> {
        return try {
            val response = pokemonAPI.getPokemonDetail(pokemonID)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Body boş geldi!",null)
            } else {
                Resource.error("Response successful değil!",null)
            }
        } catch (e: Exception) {
            Resource.error("Bütün olay patladı!",null)
        }
    }

    override suspend fun getPokeSpecies(pokemonID: Int): Resource<PokeSpecies> {
        return try {
            val response = pokemonAPI.getPokemonSpecies(pokemonID)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Body boş geldi!",null)
            } else {
                Resource.error("Response successful değil!",null)
            }
        } catch (e: Exception) {
            Resource.error("Bütün olay patladı!",null)
        }
    }
}





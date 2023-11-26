package com.cugocumhurgunay.pokedex.domain.model.mapper

import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetail
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.utils.getPokemColors
import com.cugocumhurgunay.pokedex.utils.getPokemonImageUrl


    fun PokeDetail.mapToPresentation(): PokeDetailItem {
        val typeNamesList = types.map { type -> type.type.name.capitalize() }
        val pokeMovesString = abilities.joinToString(separator = "\n") { ability -> ability.ability.name.capitalize() }
        val pokeStatsMap = stats.associate { stat ->
            stat.stat.name to String.format("%03d", stat.baseStat)
        }
        return PokeDetailItem(
                pokemonID = id,
                pokemonImageUrl = getPokemonImageUrl(id),
                pokemonName = name.capitalize(),
                typeNamesList = typeNamesList,
                pokemonWeight = weight.toString(),
                pokemonHeight = height.toString(),
                pokemonMoves = pokeMovesString,
                pokemonStatsMap = pokeStatsMap,
                color = getPokemColors(typeNamesList)
            )
        }





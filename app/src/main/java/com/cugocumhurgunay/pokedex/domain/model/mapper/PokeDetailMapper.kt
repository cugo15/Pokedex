package com.cugocumhurgunay.pokedex.domain.model.mapper

import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetail
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.utils.getPokemColors
import com.cugocumhurgunay.pokedex.utils.getPokemonImageUrl

class PokeDetailMapper {
    fun mapToPresentation(pokeDetail: PokeDetail?): PokeDetailItem? {
        return pokeDetail?.let {
            val typeNamesList = it.types.map { type -> type.type.name.capitalize() }
            val pokeMovesString = it.abilities.joinToString(separator = "\n") { ability -> ability.ability.name.capitalize() }
            val pokeStatsMap = it.stats.associate { stat ->
                stat.stat.name to String.format("%03d", stat.baseStat)
            }






            PokeDetailItem(
                pokemonID = it.id,
                pokemonImageUrl = getPokemonImageUrl(it.id),
                pokemonName = it.name.capitalize(),
                typeNamesList = typeNamesList,
                pokemonWeight = it.weight.toString(),
                pokemonHeight = it.height.toString(),
                pokemonMoves = pokeMovesString,
                pokemonStatsMap = pokeStatsMap,
                color = getPokemColors(typeNamesList)
            )
        }
    }


}

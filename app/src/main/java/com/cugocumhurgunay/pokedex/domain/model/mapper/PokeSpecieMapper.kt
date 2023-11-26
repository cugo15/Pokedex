package com.cugocumhurgunay.pokedex.domain.model.mapper

import com.cugocumhurgunay.pokedex.domain.model.details.species.PokeSpecies
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokemonSpecie

fun PokeSpecies.mapToPresentation(): PokemonSpecie {
    return PokemonSpecie(
        flavorTxt = flavorTextEntries
    )
}
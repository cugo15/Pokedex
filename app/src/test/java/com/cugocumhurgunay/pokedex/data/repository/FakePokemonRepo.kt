package com.cugocumhurgunay.pokedex.data.repository

import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.domain.model.details.species.FlavorTextEntry
import com.cugocumhurgunay.pokedex.domain.model.details.species.Language
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokemonSpecie
import com.cugocumhurgunay.pokedex.domain.model.details.species.Version
import com.cugocumhurgunay.pokedex.domain.model.list.PokemonList
import com.cugocumhurgunay.pokedex.domain.model.list.Result
import com.cugocumhurgunay.pokedex.domain.repository.PokeRepo
import com.cugocumhurgunay.pokedex.utils.Resource

class FakePokemonRepo :PokeRepo{

    val fakePokemonList = PokemonList(
        count = 10,
        next = "https://api.example.com/pokemon?offset=10",
        previous = "",
        results = listOf(
            com.cugocumhurgunay.pokedex.domain.model.list.Result(
                name = "bulbasaur",
                url = "https://api.example.com/pokemon/1"
            ),
            Result(name = "ivysaur", url = "https://api.example.com/pokemon/2"),
        )
    )

    val fakePokeDetail = PokeDetailItem(
        pokemonID = 1,
        pokemonImageUrl = "https://example.com/bulbasaur.png",
        pokemonName = "Bulbasaur",
        typeNamesList = listOf("Grass", "Poison"),
        pokemonWeight = "6.9 kg",
        pokemonHeight = "0.7 m",
        pokemonMoves = "Tackle, Growl, Leech Seed, Vine Whip",
        pokemonStatsMap = mapOf(
            "HP" to "45",
            "Attack" to "49",
            "Defense" to "49",
            "Special Attack" to "65",
            "Special Defense" to "65",
            "Speed" to "45"
        ),
        color = listOf("Green")

    )

    val fakePokeSpecies = PokemonSpecie(
        flavorTxt = listOf(
            FlavorTextEntry("This is a sample flavor text.",
                Language("en","utl"),
                Version("red","")
            ),
            FlavorTextEntry("This is a sample flavor text.", Language("en","utl"), Version("red","")),
            // Add other flavor text entries as needed
        )
    )

    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        return Resource.success(fakePokemonList)
    }

    override suspend fun getPokemonDetails(pokemonID: Int): Resource<PokeDetailItem> {
        return Resource.success(fakePokeDetail)
    }

    override suspend fun getPokeSpecies(pokemonID: Int): Resource<PokemonSpecie> {
        return Resource.success(fakePokeSpecies)
    }


}
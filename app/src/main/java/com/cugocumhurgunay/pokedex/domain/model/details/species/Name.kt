package com.cugocumhurgunay.pokedex.domain.model.details.species


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("language")
    val language: Language,
    @SerializedName("name")
    val name: String
)
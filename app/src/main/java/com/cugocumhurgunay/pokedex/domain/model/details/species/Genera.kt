package com.cugocumhurgunay.pokedex.domain.model.details.species


import com.google.gson.annotations.SerializedName

data class Genera(
    @SerializedName("genus")
    val genus: String,
    @SerializedName("language")
    val language: Language
)
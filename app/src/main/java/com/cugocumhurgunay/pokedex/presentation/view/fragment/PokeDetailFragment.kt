package com.cugocumhurgunay.pokedex.presentation.view.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

import com.cugocumhurgunay.pokedex.databinding.FragmentPokeDetailBinding
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.presentation.viewmodel.PokeDetailViewModel
import com.cugocumhurgunay.pokedex.utils.downloadUrl
import com.cugocumhurgunay.pokedex.utils.getPokemonImageUrl
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokeDetailFragment : Fragment() {
    private var _binding: FragmentPokeDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PokeDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getInt("poke_id")
        movieId?.let {
            viewModel.loadData(it)
            viewModel.loadSpecies(it)
        }
        viewModel.pokemonDetail.observe(viewLifecycleOwner) { pokemon ->
            initColorToViews(pokemon)
            initPokemonDetails(pokemon)
            if (pokemon != null) {
                if(pokemon.pokemonID == 1){
                    binding.imageViewPreviousPoke.visibility = View.GONE
                }else{
                    binding.imageViewPreviousPoke.visibility = View.VISIBLE
                }
            }
            binding.imageViewPreviousPoke.setOnClickListener {
                pokemon?.let {
                    viewModel.loadData(it.pokemonID - 1)
                    viewModel.loadSpecies(it.pokemonID)
                }
            }

            binding.imageViewNextPoke.setOnClickListener {
                pokemon?.let {
                    viewModel.loadData(it.pokemonID + 1)
                    viewModel.loadSpecies(it.pokemonID)
                }
            }
        }
        viewModel.pokemonFlavorText.observe(viewLifecycleOwner){
            binding.textViewAboutPoke.text = it
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: PokeDetailViewModel by viewModels()
        viewModel = tempViewModel
    }

    private fun initColorToViews(pokemonDetail: PokeDetailItem?) {
        pokemonDetail?.let { detail ->
            val baseColor = Color.parseColor(detail.color[0])
            val alphaColor = ColorUtils.setAlphaComponent(baseColor, (0.2 * 255).toInt())
            val colorStateList = ColorStateList.valueOf(baseColor)

            with(binding) {
                pokeDetailLayout.setBackgroundColor(baseColor)
                chipType1.chipBackgroundColor = colorStateList
                if (detail.color.size>1){
                    chipType2.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(detail.color[1]))
                }
                textViewAbout.setTextColor(baseColor)
                textViewBaseStats.setTextColor(baseColor)
                val textViews = listOf(
                    textViewHP, textViewATK, textViewDEF,
                    textViewSATK, textViewSDEF, textViewSPD
                )
                textViews.forEach { it.setTextColor(baseColor) }

                val progressBars = listOf(pbHP, pbATK, pbDEF, pbSATK, pbSDEF, pbSPD)
                progressBars.forEach {
                    it.setIndicatorColor(baseColor)
                    it.trackColor = alphaColor
                }
            }
            activity?.window?.statusBarColor = baseColor
        }
    }

    private fun initPokemonDetails(pokemonDetail: PokeDetailItem?) {
        pokemonDetail?.let {
            with(binding) {
                imageViewDetailsPoke.downloadUrl(getPokemonImageUrl(it.pokemonID), CircularProgressDrawable(requireContext()))
                textViewDetailsPokeName.text = it.pokemonName
                textViewDetailsPokeID.text = "#%03d".format(it.pokemonID)
                textViewWeight.text = "${it.pokemonWeight} kg"
                textViewHeight.text = "${it.pokemonHeight} m"

                val typeNamesList = it.typeNamesList.orEmpty()
                chipType1.text = typeNamesList.getOrNull(0)

                chipType2.apply {
                    visibility = if (typeNamesList.size > 1) View.VISIBLE else View.GONE
                    text = typeNamesList.getOrNull(1)
                }

                textViewMoves.text = it.pokemonMoves

                setStats(pbHP, textViewHPValue, it.pokemonStatsMap["hp"])
                setStats(pbATK, textViewATKValue, it.pokemonStatsMap["attack"])
                setStats(pbDEF, textViewDEFValue, it.pokemonStatsMap["defense"])
                setStats(pbSATK, textViewSATKValue, it.pokemonStatsMap["special-attack"])
                setStats(pbSDEF, textViewSDEFValue, it.pokemonStatsMap["special-defense"])
                setStats(pbSPD, textViewSPDValue, it.pokemonStatsMap["speed"])
            }
        }
    }

    private fun setStats(progressBar: LinearProgressIndicator, textView: TextView, value: String?) {
        progressBar.progress = value?.toIntOrNull() ?: 0
        textView.text = value
    }



}
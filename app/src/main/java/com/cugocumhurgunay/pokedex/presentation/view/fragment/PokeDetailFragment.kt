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
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.cugocumhurgunay.pokedex.R

import com.cugocumhurgunay.pokedex.databinding.FragmentPokeDetailBinding
import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetailItem
import com.cugocumhurgunay.pokedex.presentation.viewmodel.PokeDetailViewModel
import com.cugocumhurgunay.pokedex.utils.downloadUrl
import com.cugocumhurgunay.pokedex.utils.getPokemonImageUrl
import com.cugocumhurgunay.pokedex.utils.goTo
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
        // Retrieve the movie ID from arguments
        val movieId = arguments?.getInt("poke_id")
        // If movieId is not null, load data
        movieId?.let {
            viewModel.loadData(it)
            viewModel.loadSpecies(it)
        }
        // Observe changes in the Pokemon detail LiveData
        viewModel.pokemonDetail.observe(viewLifecycleOwner) { pokemon ->
            initColorToViews(pokemon)
            initPokemonDetails(pokemon)
            // Show or hide the previous Pokemon image view based on the Pokemon ID
            if (pokemon != null) {
                if (pokemon.pokemonID == 1) {
                    binding.imageViewPreviousPoke.visibility = View.GONE
                } else {
                    binding.imageViewPreviousPoke.visibility = View.VISIBLE
                }
            }
            // Set up click listeners to go previous and next Pokemon
            binding.imageViewPreviousPoke.setOnClickListener {
                pokemon?.let {
                    viewModel.loadData(it.pokemonID - 1)
                    viewModel.loadSpecies(it.pokemonID)
                    binding.textViewDetailsPokeName.text = ""
                    binding.textViewDetailsPokeID.text = ""
                }
            }
            binding.imageViewNextPoke.setOnClickListener {
                pokemon?.let {
                    // If data is loading, show the progress ball and hide other views
                    viewModel.loadData(it.pokemonID + 1)
                    viewModel.loadSpecies(it.pokemonID)
                    binding.textViewDetailsPokeName.text = ""
                    binding.textViewDetailsPokeID.text = ""
                }
            }
        }
        // Observe changes in the Pokemon flavor text LiveData and update the view
        viewModel.pokemonFlavorText.observe(viewLifecycleOwner) {
            binding.textViewAboutPoke.text = it
        }
        viewModel.mutableIsLoading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                // If data is loading, show the progress ball and hide other views
                binding.progressPokeDetail.visibility = if (it) View.VISIBLE else View.GONE
                binding.cardFeatures.visibility = if (it) View.GONE else View.VISIBLE
                binding.imageViewDetailsPoke.visibility = if (it) View.INVISIBLE else View.VISIBLE
                binding.imageViewBackGround.visibility = if (it) View.GONE else View.VISIBLE
                binding.imageViewNextPoke.visibility = if (it) View.GONE else View.VISIBLE
                binding.imageViewPreviousPoke.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
        viewModel.mutableError.observe(viewLifecycleOwner) { isError ->
            if (isError == true) {
                binding.errorText.visibility = View.VISIBLE

                binding.errorText.text = "An unknown error occurred"
            }else{
                binding.errorText.visibility = View.GONE
            }
        }
        binding.imageViewArrowBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: PokeDetailViewModel by viewModels()
        viewModel = tempViewModel
    }

    private fun initColorToViews(pokemonDetail: PokeDetailItem?) {
        pokemonDetail?.let { detail ->
            // Extract the base color from the first color in the detail
            val baseColor = Color.parseColor(detail.color[0])
            // Set the alpha component of the base color to create a half transparent color
            val alphaColor = ColorUtils.setAlphaComponent(baseColor, (0.2 * 255).toInt())
            val colorStateList = ColorStateList.valueOf(baseColor)
            // Use binding to access views in the layout
            with(binding) {
                pokeDetailLayout.setBackgroundColor(baseColor)
                chipType1.chipBackgroundColor = colorStateList
                // Set chipType2 chipBackgroundColor if there is a second color
                if (detail.color.size > 1) {
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
            // Use binding to access views in the layout
            with(binding) {
                // Download and display Pokemon image
                imageViewDetailsPoke.downloadUrl(getPokemonImageUrl(it.pokemonID), CircularProgressDrawable(requireContext()))
                // Set Pokemon name, ID, weight, and height
                textViewDetailsPokeName.text = it.pokemonName
                textViewDetailsPokeID.text = "#%03d".format(it.pokemonID)
                textViewWeight.text = "${it.pokemonWeight} kg"
                textViewHeight.text = "${it.pokemonHeight} m"
                // Set the first and second type of the Pokemon using Chip views
                val typeNamesList = it.typeNamesList.orEmpty()
                chipType1.text = typeNamesList.getOrNull(0)
                chipType2.apply {
                    // Show the second type chip only if there is more than one type
                    visibility = if (typeNamesList.size > 1) View.VISIBLE else View.GONE
                    text = typeNamesList.getOrNull(1)
                }
                // Set the moves of the Pokemon
                textViewMoves.text = it.pokemonMoves
                // Set the statistics
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
        // If the value is null or cannot be converted to Int, default to 0
        progressBar.progress = value?.toIntOrNull() ?: 0
        textView.text = value
    }




}
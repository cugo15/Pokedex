package com.cugocumhurgunay.pokedex.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cugocumhurgunay.pokedex.R
import com.cugocumhurgunay.pokedex.databinding.FragmentHomeBinding
import com.cugocumhurgunay.pokedex.databinding.PopMenuSortBinding
import com.cugocumhurgunay.pokedex.presentation.view.adapter.PokeAdapter
import com.cugocumhurgunay.pokedex.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var rb1Checked = true
    private var rb2Checked = false
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var pokeAdapter : PokeAdapter = PokeAdapter()
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.loadData()
        viewModel.searchedPokemonList.observe(viewLifecycleOwner){
            pokeAdapter.updatePokeList(it)
        }
        viewModel.pokemonList.observe(viewLifecycleOwner){
            pokeAdapter.updatePokeList(it)
        }
        viewModel.filtredPokemonList.observe(viewLifecycleOwner){
            pokeAdapter.updatePokeList(it)
        }
        binding.cardViewSort.setOnClickListener {
            showPopupMenu(it)
        }
        binding.rvPoke.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val lastVisibleItemPositions = layoutManager.findLastCompletelyVisibleItemPositions(null)

                // Choose the maximum index from the array, as it represents the last visible item
                val lastVisibleItemPosition = lastVisibleItemPositions.maxOrNull() ?: 0
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    // You have reached the last item. Trigger data loading.
                    viewModel.loadData()
                    rb1Checked = true
                    rb2Checked = false
                }
            }
        })

        binding.searchViewPoke.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {//Harf girdikçe ve sildikçe
                viewModel.searchPokemonList(newText)
                rb1Checked = true
                rb2Checked = false
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {//Arama iconuna bastığında
                viewModel.searchPokemonList(query)
                rb1Checked = true
                rb2Checked = false
                return true
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomeViewModel by viewModels()
        viewModel = tempViewModel
    }
    private fun initView() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvPoke.layoutManager = layoutManager
        binding.rvPoke.adapter = pokeAdapter
    }

    override fun onResume() {
        super.onResume()
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
        activity?.window?.statusBarColor = primaryColor
    }
    private fun showPopupMenu(anchorView: View) {
        val popupView = LayoutInflater.from(anchorView.context).inflate(R.layout.pop_menu_sort, null)
        val binding = PopMenuSortBinding.bind(popupView)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        binding.rb1.isChecked = rb1Checked
        binding.rb2.isChecked = rb2Checked
        binding.rb1.setOnClickListener {
            rb1Checked = true
            rb2Checked = false
            viewModel.loadData()
            Toast.makeText(anchorView.context, "Option 1 selected", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        binding.rb2.setOnClickListener {
            rb1Checked = false
            rb2Checked = true
            viewModel.orderPokemonListByName()
            Toast.makeText(anchorView.context, "Option 2 selected", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView)
    }

}
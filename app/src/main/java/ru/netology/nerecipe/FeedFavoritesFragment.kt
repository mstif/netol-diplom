package ru.netology.nerecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nerecipe.databinding.FragmentFeedBinding
import ru.netology.nerecipe.adapter.RecipeAdapter
import ru.netology.nerecipe.data.viewModel.RecipeViewModel


class FeedFavoritesFragment : Fragment() {

    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFeedBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipeAdapter(viewModel)

        binding.container.adapter = adapter
        viewModel.dataViewModel.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }
        binding.fab.setOnClickListener {
            viewModel.currentRecipe.value = null
            viewModel.onAddClicked()
        }


    }.root

    companion object {
        const val TAG = "FeedFragment"
    }
}
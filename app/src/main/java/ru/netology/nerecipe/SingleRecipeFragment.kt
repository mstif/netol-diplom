package ru.netology.nerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nerecipe.adapter.StageAdapter
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentSingleRecipeBinding

class SingleRecipeFragment : Fragment() {


    private var idRecipe: Long? = null
    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idRecipe = it.getLong(INITIAL_RECIPE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (viewModel.currentRecipe.value == null)
            throw RuntimeException("Error receive recipe parameter")


        val binding =
            FragmentSingleRecipeBinding.inflate(layoutInflater, container, false).also { binding ->
                with(binding) {

                    bind(binding)


                }
            }
        val adapter = StageAdapter(viewModel, false)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.container.layoutManager = linearLayoutManager

        binding.container.adapter = adapter

        viewModel.dataStages.observe(viewLifecycleOwner) { stages ->
            adapter.submitList(stages)
        }
        return binding.root
    }

    fun bind(binding: FragmentSingleRecipeBinding) = with(binding) {

        val recipe = viewModel.currentRecipe.value
        if (recipe == null) return@with
        //  this@PostEditFragment.post = viewModel.currentPost.value ?: Post()
        toggleButtonFavorit.isChecked = recipe.favorites

        describe.text = recipe.describe
        author.text = recipe.author
        category.text = recipe.category


    }

    companion object {

        const val INITIAL_RECIPE_KEY = "openSingleRecipe"


        fun createBundle(idRecipe: Long?) =
            Bundle(2).apply {

                putLong(INITIAL_RECIPE_KEY, idRecipe ?: 0)
            }
    }
}
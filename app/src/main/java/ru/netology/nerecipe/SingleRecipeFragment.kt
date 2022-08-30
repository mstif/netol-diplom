package ru.netology.nerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nerecipe.adapter.StageAdapter
import ru.netology.nerecipe.data.RecipeRepository
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
        val recipe = viewModel.getRecipeByIdFromLiveData(idRecipe)


       //val recipe = viewModel.currentRecipe.value
        if (recipe == null)
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
            binding.emptyPovar.visibility =
                if (stages?.isEmpty() ?: true) View.VISIBLE else View.GONE

        }

        viewModel.dataViewModel.observe(viewLifecycleOwner){ recipes->
            bind(binding)

        }
        viewModel.navigateToRecipeScreenEvent.observe(viewLifecycleOwner) { recipeToEdit ->
            findNavController().navigate(
                R.id.action_singleRecipe_to_editRecipe,
                EditRecipe.createBundle(recipeToEdit?.id ?: RecipeRepository.NEW_RECIPE_ID)
            )
        }

        val popupMenu by lazy {
            PopupMenu(this.context, binding.dropdownMenu).apply {
                inflate(R.menu.option_recipe)
                this.setOnMenuItemClickListener { menuItems ->
                    when (menuItems.itemId) {
                        R.id.remove -> {
                            viewModel.onDeleteClicked(recipe = recipe )
                            findNavController().popBackStack()
                            true
                        }
                        R.id.menu_edit -> {
                            viewModel.onEditClicked(recipe)
                            true
                        }
                        else -> false
                    }

                }
            }
        }
        binding.dropdownMenu.setOnClickListener { popupMenu.show() }
        binding.toggleButtonFavorit.setOnClickListener {
            viewModel.onLikeClicked(recipe)

        }



        setFragmentResultListener(requestKey = EditRecipe.REQUEST_KEY_CHAHGE) { requestKey, bundle ->
            if (requestKey != EditRecipe.REQUEST_KEY_CHAHGE) return@setFragmentResultListener

            viewModel.onSaveButtonClicked()

        }

        return binding.root
    }

    fun bind(binding: FragmentSingleRecipeBinding) = with(binding) {


        val recipe = viewModel.getRecipeByIdFromLiveData(idRecipe)
        //val rec1 = viewModel.getRecipeById(idRecipe?:0)
        //val recipe = viewModel.currentRecipe.value
        if (recipe == null) return@with

        toggleButtonFavorit.isChecked = recipe.favorites

        describe.text = recipe.describe
        author.text = recipe.author
        category.text = recipe.category
        viewModel.dataStages.value=recipe.stages

    }

    companion object {

        const val INITIAL_RECIPE_KEY = "openSingleRecipe"


        fun createBundle(idRecipe: Long?) =
            Bundle(2).apply {

                putLong(INITIAL_RECIPE_KEY, idRecipe ?: 0)
            }
    }
}
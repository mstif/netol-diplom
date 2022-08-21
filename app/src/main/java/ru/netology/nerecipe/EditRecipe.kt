package ru.netology.nerecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentEditRecipeBinding
import ru.netology.nerecipe.databinding.RecipeBinding
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [EditRecipe.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditRecipe : Fragment() {
    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    // TODO: Rename and change types of parameters


    private var param1: String? = null
    private var param2: String? = null
    private var idRecipe: Long = 0
    private lateinit var recipe: Recipe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel.navigateToStageScreenEvent.observe(this){stage->
            findNavController().navigate(
                R.id.action_editRecipe_to_editStage,
                StageFragment.createBundle(stage?.id)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         recipe = viewModel.currentRecipe.value ?: Recipe()


        val binding = FragmentEditRecipeBinding.inflate(layoutInflater, container, false).also { binding ->
            with(binding) {

                bind(binding)


            }
        }
        binding.fabStage.setOnClickListener {
            viewModel.currentStage.value = null
            viewModel.onAddStageClicked()
        }

        return binding.root
    }

    fun bind(binding: FragmentEditRecipeBinding) = with(binding) {
        // val post = viewModel.dataViewModel.value?.find { it.id == idPost }

        recipeDescribe.setText(recipe.describe)



        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditRecipe.
         */
        const val INITIAL_RECIPE_KEY = "openEditedRecipe"

        fun createBundle(postId: Long) = Bundle(1).apply {
            putLong(INITIAL_RECIPE_KEY, postId)
        }
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditRecipe().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
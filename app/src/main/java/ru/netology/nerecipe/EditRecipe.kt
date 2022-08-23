package ru.netology.nerecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nerecipe.adapter.StageAdapter
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentEditRecipeBinding
import ru.netology.nerecipe.databinding.RecipeBinding
import java.lang.RuntimeException


/**
 * A simple [Fragment] subclass.
 * Use the [EditRecipe.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditRecipe : Fragment() {
    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    private var idRecipe: Long = 0
    private lateinit var recipe: Recipe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idRecipe = it.getLong(INITIAL_RECIPE_KEY)

        }

        viewModel.navigateToStageScreenEvent.observe(this){stage->
            findNavController().navigate(
                R.id.action_editRecipe_to_editStage,
                StageFragment.createBundle(stage?.id?:0L)
            )
        }

        setFragmentResultListener(requestKey = EditStage.STAGE_KEY_REQUEST) { requestKey, bundle ->
            if (requestKey != EditStage.STAGE_KEY_REQUEST) return@setFragmentResultListener

            //viewModel.onSaveButtonClicked()
           // viewModel.currentRecipe.value = null
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

            viewModel.onAddStageClicked()
        }

//        binding.spinner.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>,
//                                        view: View, position: Int, id: Long) {
//                val category= binding.spinner.selectedItem.toString()
//                recipe = recipe.copy(category = category)
//                viewModel.currentRecipe.value = recipe
//
//            }
//
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // write code to perform some action
//            }
//        }

//        binding.recipeDescribe.addTextChangedListener{describe ->
//            recipe = recipe.copy(describe = describe.toString())
//            viewModel.currentRecipe.value = recipe
//        }

        binding.ok.setOnClickListener{
            val text = binding.recipeDescribe.text
            val category = binding.spinner.selectedItem.toString()
            recipe = recipe.copy(category = category)
            if (!text.isNullOrBlank()) {
                val resultBundle = Bundle(2)
                resultBundle.putString(RESULT_KEY_DESCRIBE, text.toString())
                resultBundle.putString(RESULT_KEY_CATEGORY, category)
                setFragmentResult(requestKey = REQUEST_KEY_CHAHGE, resultBundle)
                recipe = recipe.copy(describe = text.toString())


            }
            viewModel.currentRecipe.value = recipe
            findNavController().popBackStack()
        }


        val adapter = StageAdapter(viewModel)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.container.layoutManager = linearLayoutManager

        binding.container.adapter = adapter

        viewModel.dataStages.observe(viewLifecycleOwner) { stages ->
            adapter.submitList(stages)
        }
        viewModel.dataStages.value = recipe.stages
        return binding.root
    }

    fun bind(binding: FragmentEditRecipeBinding) = with(binding) {
        // val post = viewModel.dataViewModel.value?.find { it.id == idPost }

        recipeDescribe.setText(recipe.describe)
        val positionCategory = resources.getStringArray(R.array.categories).indexOf(recipe.category)


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


        spinner.setSelection(positionCategory)
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
        const val RESULT_KEY_DESCRIBE = "result key describe"
        const val RESULT_KEY_CATEGORY = "result key category"
        const val REQUEST_KEY_CHAHGE = "request key change"

        fun createBundle(postId: Long) = Bundle(1).apply {
            putLong(INITIAL_RECIPE_KEY, postId)
        }
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditRecipe().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
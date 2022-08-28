package ru.netology.nerecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.adapter.RecipeAdapter
import ru.netology.nerecipe.adapter.StageAdapter
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentEditRecipeBinding
import ru.netology.nerecipe.databinding.RecipeBinding
import java.lang.RuntimeException

class EditRecipe : Fragment() {
    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    private var idRecipe: Long = 0
    private lateinit var recipe: Recipe
   // private var adapterCurrentList: List<Stage> = listOf()
    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                0
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = recyclerView.adapter as StageAdapter
                    val from = viewHolder.absoluteAdapterPosition
                    val to = target.absoluteAdapterPosition

                    viewHolder.itemView.findViewById<TextView>(R.id.stage_number).text=(to+1).toString()
                    target.itemView.findViewById<TextView>(R.id.stage_number).text=(from+1).toString()


                    adapter.notifyItemMoved(from, to)
                    viewModel.onMoveItemStage(
                        from,
                        to,
                        adapter.getStagebyPosition(from),
                        adapter.getStagebyPosition(to)
                    )
                    // val list = viewModel.currentRecipe.value?.stages
                    //adapter.submitList(list)
                   // adapter.differ.submitList(list)
                   // adapterCurrentList = adapter.currentList
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)

                    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                        viewHolder?.itemView?.alpha = 0.5f
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)

                    viewHolder.itemView.alpha = 1.0f

                }
            }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idRecipe = it.getLong(INITIAL_RECIPE_KEY)

        }

        viewModel.navigateToStageScreenEvent.observe(this) { stage ->
            findNavController().navigate(
                R.id.action_editRecipe_to_editStage,
                StageFragment.createBundle(stage?.id ?: 0L)
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
    ): View = FragmentEditRecipeBinding.inflate(layoutInflater, container, false).also { binding ->



        val rec= viewModel.getRecipeById(idRecipe)
        recipe = viewModel.currentRecipe.value ?: Recipe()



        bind(binding)

        itemTouchHelper.attachToRecyclerView(binding.container)
        binding.fabStage.setOnClickListener {
            viewModel.currentRecipe.value = recipe
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

        binding.ok.setOnClickListener {
            val text = binding.recipeDescribe.text
            val category = binding.spinner.selectedItem.toString()

            if (!text.isNullOrBlank()) {
                val resultBundle = Bundle(2)
                resultBundle.putString(RESULT_KEY_DESCRIBE, text.toString())
                resultBundle.putString(RESULT_KEY_CATEGORY, category)
                setFragmentResult(requestKey = REQUEST_KEY_CHAHGE, resultBundle)
                recipe = recipe.copy(describe = text.toString())


            }
            val stages = viewModel.currentRecipe.value?.stages ?: listOf()
            recipe = recipe.copy(category = category,stages = stages)
            viewModel.currentRecipe.value = recipe
            findNavController().popBackStack()
        }


        val adapter = StageAdapter(viewModel, true)



        binding.container.adapter = adapter

        viewModel.dataStages.observe(viewLifecycleOwner) { stages ->
            val list = stages.sortedBy { it.position }
            adapter.submitList(list)
            adapter.differ.submitList(list)
        }
        viewModel.dataStages.value = recipe.stages
//        val list =  viewModel.currentRecipe.value?.stages?.sortedBy { it.position }
//        adapter.submitList(list)
//        adapter.differ.submitList(list)

    }.root

    fun bind(binding: FragmentEditRecipeBinding) = with(binding) {


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

        const val INITIAL_RECIPE_KEY = "openEditedRecipe"
        const val RESULT_KEY_DESCRIBE = "result key describe"
        const val RESULT_KEY_CATEGORY = "result key category"
        const val REQUEST_KEY_CHAHGE = "request key change"

        fun createBundle(postId: Long) = Bundle(1).apply {
            putLong(INITIAL_RECIPE_KEY, postId)
        }


    }
}
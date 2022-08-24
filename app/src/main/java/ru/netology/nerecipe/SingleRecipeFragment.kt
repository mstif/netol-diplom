package ru.netology.nerecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.adapter.StageAdapter
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentSingleRecipeBinding
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingleRecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleRecipeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var idRecipe: Long? = null
    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idRecipe = it.getLong(INITIAL_RECIPE_KEY)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (viewModel.currentRecipe.value == null)
            throw RuntimeException("Error receive post parameter")
        // else post = viewModel.currentPost.value ?: Post()


        val binding =
            FragmentSingleRecipeBinding.inflate(layoutInflater, container, false).also { binding ->
                with(binding) {

                    bind(binding)


                }
            }
        val adapter = StageAdapter(viewModel,false)

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
        // val post = viewModel.dataViewModel.value?.find { it.id == idPost }

        val recipe = viewModel.currentRecipe.value
        if (recipe == null) return@with
        //  this@PostEditFragment.post = viewModel.currentPost.value ?: Post()
        toggleButtonFavorit.isChecked = recipe.favorites

        describe.text = recipe.describe
        author.text = recipe.author
        category.text = recipe.category


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SingleRecipe.
         */
        const val INITIAL_RECIPE_KEY = "openSingleRecipe"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SingleRecipeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        fun createBundle(idRecipe: Long?) =
            Bundle(2).apply {

                putLong(INITIAL_RECIPE_KEY, idRecipe ?: 0)
            }
    }
}
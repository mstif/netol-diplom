package ru.netology.nerecipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.ChipBinding
import ru.netology.nerecipe.databinding.FragmentCategoryFeedBinding
import ru.netology.nerecipe.databinding.FragmentFeedBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFeed.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFeed : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentCategoryFeedBinding.inflate(layoutInflater, container, false).also { binding ->

            val nameList = resources.getStringArray(R.array.categories)
            var id = 0
            nameList.forEach {
                val chip = createChip(it)

                chip.id = id++
                chip.isChecked = viewModel.filter.value?.categories?.contains(chip.id)?:true
                binding.chipGroup.addView(chip)
            }

            binding.CategoryOk.setOnClickListener (onOkButtonClicked(binding))


        }.root

    private fun createChip(label: String): Chip {
        val chip = ChipBinding.inflate(layoutInflater).root

        chip.text = label
        return chip
    }

    private fun onOkButtonClicked(binding: FragmentCategoryFeedBinding): (View) -> Unit =
        {
            //val intent = Intent()
            val checkedIds = binding.chipGroup.checkedChipIds
            if (!checkedIds.isEmpty()) {
                val resultBundle = Bundle(1)
              //  resultBundle.putString(FeedFragment.RESULT_CATEGORY_KEY, checkedIds )
                resultBundle.putIntegerArrayList(FeedFragment.RESULT_CATEGORY_KEY,checkedIds as ArrayList<Int>)
                setFragmentResult(requestKey = FeedFragment.REQUEST_CATEGORY_KEY, resultBundle)
                //setFragmentResult(requestKey = REQUEST_KEY_SINGLE,resultBundle)
                // setResult(Activity.RESULT_CANCELED, intent)
            }
            findNavController().popBackStack()
        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CategoryFeed.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFeed().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
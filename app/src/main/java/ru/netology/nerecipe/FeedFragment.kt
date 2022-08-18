package ru.netology.nerecipe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import ru.netology.nerecipe.databinding.FragmentFeedBinding
import ru.netology.nerecipe.adapter.RecipeAdapter
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.ChipBinding


class FeedFragment : Fragment() {

    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFeedBinding.inflate(layoutInflater, container, false).also { binding ->

        adapter = RecipeAdapter(viewModel)

        binding.container.adapter = adapter
        viewModel.dataViewModel.observe(viewLifecycleOwner) { recipes ->
           // adapter.submitList(recipes)
            val filteredResult = viewModel.getFilteredResultNew()
            adapter.submitList(filteredResult)
        }
        binding.fab.setOnClickListener {
            viewModel.currentRecipe.value = null
            viewModel.onAddClicked()
        }

        setFragmentResultListener(requestKey = REQUEST_CATEGORY_KEY) { requestKey, bundle ->
            if (requestKey != REQUEST_CATEGORY_KEY) return@setFragmentResultListener
            val categories =
                bundle.getIntegerArrayList(RESULT_CATEGORY_KEY) ?: return@setFragmentResultListener
            // val listCategories = categories.split(";")
            val filterFeed = viewModel.filter.value?.copy(categories = categories)?:
            FilterFeed("",categories)

            viewModel.onChangeFilters(filterFeed)
            //viewModel.currentPost.value = null
        }


    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_filter, menu)
                val searchMenuItem = menu.findItem(R.id.action_search_text)
                val searchView = searchMenuItem.actionView as SearchView
                val textView = resources.getString(R.string.search_text)
                searchView.queryHint = textView
                val categoriesList = resources.getStringArray(R.array.categories).toList()
                searchView.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(searchText: String?): Boolean {
                        searchView.clearFocus()

//                        val filteredResult = viewModel.getFilteredResult(
//                            viewModel.dataViewModel.value,
//                            searchText,
//                            null
//                        )

                        //adapter.submitList(filteredResult)

                        val filterFeed = viewModel.filter.value?.copy(searchText = searchText?: "")?:
                        FilterFeed(searchText?: "",List(categoriesList.size){index -> index })

                        viewModel.onChangeFilters(filterFeed)
                        return false
                    }

                    override fun onQueryTextChange(searchText: String?): Boolean {
                        //val filteredResult = viewModel.getFilteredResult(
//                            viewModel.dataViewModel.value,
//                            searchText,
//                            null
//                        )
                       // adapter.submitList(filteredResult)
                        val filterFeed = viewModel.filter.value?.copy(searchText = searchText?: "")?:
                        FilterFeed(searchText?: "",List(categoriesList.size){index -> index })

                        viewModel.onChangeFilters(filterFeed)
                        return true
                    }

                })
            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_search_text -> {

                        true
                    }
                    R.id.action_search_category -> {
                        findNavController().navigate(
                            R.id.categoryFeed,

                            )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoriesList = resources.getStringArray(R.array.categories).toList()
        viewModel.listAllCategories = categoriesList
        viewModel.filter.value = FilterFeed("", List<Int>(categoriesList.size){index -> index })
        viewModel.filter.observe(this) {
            val filteredResult = viewModel.getFilteredResultNew(
               // viewModel.dataViewModel.value

            )
            adapter.submitList(filteredResult)
        }

    }

    companion object {
        const val RESULT_CATEGORY_KEY = "ResultCategoryKey"
        const val REQUEST_CATEGORY_KEY = "requestCategoryKey"
        const val REQUEST_KEY_SINGLE = "singlePost"
        const val INITIAL_CONTENT_KEY = "initialContent"
        const val INITIAL_FRAGMENT_KEY = "initialFragmentKey"

        // fun create(initialContentPost:String?)= PostContentFragment().apply {
        //    this.arguments=createBundle(initialContentPost) }
        fun createBundle(initialContentPost: String?, initialFragmentKey: String) =
            Bundle(2).apply {
                putString(INITIAL_CONTENT_KEY, initialContentPost)
                putString(INITIAL_FRAGMENT_KEY, initialFragmentKey)
            }

    }
}
package ru.netology.nerecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import ru.netology.nerecipe.data.viewModel.RecipeViewModel
import ru.netology.nerecipe.databinding.FragmentEditRecipeBinding
import ru.netology.nerecipe.databinding.FragmentEditStageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditStage.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditStage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var idStage: Long = 0L
    private lateinit var stage: Stage
    val viewModel: RecipeViewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idStage = it.getLong(StageFragment.INITIAL_STAGE_KEY)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        stage = viewModel.currentStage.value ?: Stage()


        val binding = FragmentEditStageBinding.inflate(layoutInflater, container, false).also { binding ->
            with(binding) {

                bind(binding)


            }
        }

        val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
               val uridb =copyToDb(uri)
                stage = stage.copy(photo = uridb.toString())
                bind(binding)
                viewModel.onSetImage(uridb.toString())
            }

        }

        binding.setImage.setOnClickListener {

            selectImageFromGalleryResult.launch("image/*")

        }

        binding.ok.setOnClickListener{

        }

        return binding.root
    }

    private fun copyToDb(uri: Uri):Uri {
        return uri
    }


    fun bind(binding: FragmentEditStageBinding) = with(binding) {
        // val post = viewModel.dataViewModel.value?.find { it.id == idPost }
        stageNumber.text = stage.position.toString()
        textStageEdit.setText(stage.content)
        val imageUrl =stage.photo
        // val imageUrl = "content://com.android.providers.media.documents/document/image%3A27"
        stageImage.setImageURI(Uri.parse(imageUrl))


       stageImage.visibility = if (stage.photo.isBlank()) View.GONE else View.VISIBLE

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditStage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditStage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
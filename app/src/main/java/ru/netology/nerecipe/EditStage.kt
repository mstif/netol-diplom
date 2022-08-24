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
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.data.viewModel.RecipeViewModel

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
        val listStages = viewModel.currentRecipe.value?.stages
        val maxPosition =
            if (listStages.isNullOrEmpty()) 0 else listStages.maxOf { it.position } + 1
        stage = (viewModel.currentStage.value ?: Stage()).copy(position = maxPosition)
        if (idStage == 0L)
            stage = stage.copy(id = viewModel.nextIdStages())


        val binding =
            FragmentEditStageBinding.inflate(layoutInflater, container, false).also { binding ->
                with(binding) {

                    bind(binding)


                }
            }

        val selectImageFromGalleryResult =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
                uri?.let {
                    requireActivity().contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                    val uridb = copyToDb(uri)
                    stage = stage.copy(photo = uridb.toString())
                    binding.stageImage.setImageURI(uridb)
                    viewModel.onSetImage(uridb.toString())
                    binding.stageImage.visibility =
                        if (stage.photo.isBlank()) View.GONE else View.VISIBLE
                }

            }

        binding.setImage.setOnClickListener {

            selectImageFromGalleryResult.launch(arrayOf("image/*"))

        }

        binding.ok.setOnClickListener {
            val text = binding.textStageEdit.text

            if (!text.isNullOrBlank()) {
                val resultBundle = Bundle(2)
                resultBundle.putString(RESULT_KEY_DESCRIBE, text.toString())
                resultBundle.putString(RESULT_KEY_PHOTO, stage.photo)
                setFragmentResult(requestKey = STAGE_KEY_REQUEST, resultBundle)
                stage = stage.copy(content = text.toString())


            }
            //viewModel.currentStage.value = stage
            val recipe = viewModel.currentRecipe.value
            val stages = (recipe?.stages ?: listOf()) + stage
            viewModel.currentRecipe.value = recipe?.copy(stages = stages)
            viewModel.dataStages.value = stages
            findNavController().popBackStack()

        }

        return binding.root
    }

    private fun copyToDb(uri: Uri): Uri {
        return uri
    }


    fun bind(binding: FragmentEditStageBinding) = with(binding) {
        // val post = viewModel.dataViewModel.value?.find { it.id == idPost }
        stageNumber.text = stage.position.toString()
        textStageEdit.setText(stage.content)
        val imageUrl = stage.photo
        // val imageUrl = "content://com.android.providers.media.documents/document/image%3A27"
        stageImage.setImageURI(Uri.parse(imageUrl))


        stageImage.visibility = if (stage.photo.isBlank()) View.GONE else View.VISIBLE

    }

    companion object {

        const val STAGE_KEY_REQUEST = "stage change"
        const val RESULT_KEY_PHOTO = "key photo stage"
        const val RESULT_KEY_DESCRIBE = "key text stage"

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
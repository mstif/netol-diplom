package ru.netology.nerecipe.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Stage
import ru.netology.nerecipe.databinding.RecipeBinding
import ru.netology.nerecipe.databinding.StageBinding


class StageAdapter(

    private val interactionListener: StageInteractionListener,
    private val isEdit:Boolean=false
) : ListAdapter<Stage, StageAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: StageBinding, listener: StageInteractionListener) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var stage: Stage
        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.dropdownMenu).apply {
                inflate(R.menu.option_recipe)
                this.setOnMenuItemClickListener { menuItems ->
                    when (menuItems.itemId) {
                        R.id.remove -> {
                            listener.onDeleteClicked(stage)
                            true
                        }
                        R.id.menu_edit -> {
                            listener.onEditClicked(stage)
                            true
                        }
                        else -> false
                    }

                }
            }
        }



        fun bind(stage: Stage) = with(binding) {
            this@ViewHolder.stage = stage
            textStage.text = stage.content
            stageNumber.text = stage.position.toString()
            val imageUrl =stage.photo
           // val imageUrl = "content://com.android.providers.media.documents/document/image%3A27"
            photo.setImageURI(Uri.parse(imageUrl))
            stageNumber.setText((absoluteAdapterPosition+1).toString())
            dropdownMenu.setOnClickListener { popupMenu.show() }
            photo.visibility = if (stage.photo.isBlank()) View.GONE else View.VISIBLE


        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StageBinding.inflate(inflater, parent, false)
        binding.dropdownMenu.visibility = if (isEdit) View.VISIBLE else View.GONE
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    fun getStagebyPosition(position: Int): Stage {
        return getItem(position)
    }


    private object DiffCallback : DiffUtil.ItemCallback<Stage>() {
        override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean {
            return oldItem == newItem
        }

    }
}
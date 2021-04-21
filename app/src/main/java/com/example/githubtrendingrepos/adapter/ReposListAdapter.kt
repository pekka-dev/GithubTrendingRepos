package com.example.githubtrendingrepos.adapter

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubtrendingrepos.data.model.GithubRepo
import com.example.githubtrendingrepos.databinding.RepoListItemBinding


class ReposListAdapter(
	private val context: Context
) : PagingDataAdapter<GithubRepo, ReposListAdapter.ViewHolder>(REPO_COMPARATOR) {

	private var expandedItem = -1

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding =
			RepoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val currentItem = getItem(position)
		if (currentItem != null) {
			holder.bind(currentItem)
		}
	}


	inner class ViewHolder(private val binding: RepoListItemBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(repo: GithubRepo) {
			binding.apply {
				repoListItemTitle.text = repo.name
				repoListItemLanguage.text = repo.language
				repoListItemFullName.text = repo.fullName
				repoListItemDescription.text = repo.description
				repoListItemPrivate.text = if (repo.private) "Private" else "Public"
				if (expandedItem == RecyclerView.NO_POSITION) {
					expandable(false)
				} else {
					expandable(expandedItem == bindingAdapterPosition)
				}
				layoutCollapsed.setOnClickListener {
					expandable(true)
					expandedItem = if (expandedItem != bindingAdapterPosition) {
						notifyItemChanged(expandedItem)
						bindingAdapterPosition
					} else {
						expandable(false)
						RecyclerView.NO_POSITION
					}
				}
				repoListItemGotoButton.setOnClickListener {
					gotoUrl(repo.htmlUrl)
				}
			}
		}

		private fun expandable(bool: Boolean) {
			binding.apply {
				layoutExpanded.isVisible = bool
				baseCardView.cardElevation = TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP,
					if (bool) 10f else 3f,
					Resources.getSystem().displayMetrics
				)
			}
		}

		private fun gotoUrl(url: String) {
			val uriUrl: Uri = Uri.parse(url)
			val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
			launchBrowser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
			context.startActivity(launchBrowser)
		}
	}

	companion object {
		private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<GithubRepo>() {
			override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo) =
				oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo) =
				oldItem == newItem

		}
	}
}

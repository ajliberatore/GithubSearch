package com.example.githubsearch.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.GithubUser
import com.example.githubsearch.R
import com.example.githubsearch.databinding.SearchResultListItemBinding
import com.squareup.picasso.Picasso

class UserAdapter : ListAdapter<GithubUser, UserAdapter.UserViewHolder>(SearchResultComparator) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            SearchResultListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    class UserViewHolder(private val itemCell: SearchResultListItemBinding) :
        RecyclerView.ViewHolder(itemCell.root) {

        fun bind(item: GithubUser?) {
            item?.let { user ->
                Picasso.with(itemCell.root.context)
                    .load(user.thumbnailUrl)
                    .resize(175, 175)
                    .into(itemCell.thumbnail)
                itemCell.userName.text = user.name
                itemCell.numRepositories.text =
                    itemCell.root.context.getString(
                        if (user.numRepositories == 30) R.string.num_repositories_plus else R.string.num_repositories,
                        user.numRepositories
                    )
            }
        }
    }

    companion object SearchResultComparator : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            // Id is unique.
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem == newItem
        }
    }
}

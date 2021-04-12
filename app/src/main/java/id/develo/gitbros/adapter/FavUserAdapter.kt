package id.develo.gitbros.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.develo.gitbros.R
import id.develo.gitbros.databinding.FavUserItemLayoutBinding
import id.develo.gitbros.db.FavoriteHelper
import id.develo.gitbros.model.FavoriteUser

class FavUserAdapter(activity: Activity): RecyclerView.Adapter<FavUserAdapter.ViewHolder>() {
    var listFavs = ArrayList<FavoriteUser>()
        set(listFavs) {
            if (listFavs.size > 0) {
                this.listFavs.clear()
            }
            this.listFavs.addAll(listFavs)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_user_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavs[position])
    }

    override fun getItemCount(): Int = listFavs.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FavUserItemLayoutBinding.bind(itemView)
        @SuppressLint("StringFormatMatches")
        fun bind(favUser: FavoriteUser) {
            binding.tvUsername.text = favUser.username
            if (favUser.repository > 0) {
                binding.tvRepo.text =
                    itemView.context.resources.getString(R.string.repos, favUser.repository)
            } else {
                binding.tvRepo.text =
                    itemView.context.resources.getString(R.string.repo, favUser.repository)
            }
            Glide.with(itemView.context)
                .load(favUser.avatar_url)
                .into(binding.imgUser)
        }

    }
}
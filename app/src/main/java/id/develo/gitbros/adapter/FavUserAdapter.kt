package id.develo.gitbros.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.develo.gitbros.R
import id.develo.gitbros.databinding.FavUserItemLayoutBinding
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import id.develo.gitbros.db.FavoriteHelper
import id.develo.gitbros.model.FavoriteUser

class FavUserAdapter : RecyclerView.Adapter<FavUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var listFavs = ArrayList<FavoriteUser>()
        set(listFavs) {
            if (listFavs.size > 0) {
                this.listFavs.clear()
            }
            this.listFavs.addAll(listFavs)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_user_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavs[position])
    }

    override fun getItemCount(): Int = listFavs.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FavUserItemLayoutBinding.bind(itemView)
        var favHelper = FavoriteHelper(itemView.context)

        val negativeButtonClick = { dialog: DialogInterface, which: Int -> }

        @SuppressLint("StringFormatMatches")
        fun bind(favUser: FavoriteUser) {
            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + favUser.id)
                Log.d("TEST AGAIN", uriWithId.toString())
                onItemClickCallback.onItemClicked(uriWithId)
            }
            binding.tvUsername.text = favUser.username
            if (favUser.repository > 0) {
                binding.tvRepo.text =
                    itemView.context.resources.getString(R.string.repos, favUser.repository)
            } else {
                binding.tvRepo.text =
                    itemView.context.resources.getString(R.string.repo, favUser.repository)
            }
            binding.imgRemove.setOnClickListener {

                val builder = AlertDialog.Builder(itemView.context)
                with(builder) {
                    setTitle("Remove ${favUser.username}.")
                    setMessage("Are you sure want to remove ${favUser.username}?")
                    setPositiveButton("YES", positiveButtonClick)
                    setNegativeButton("NO", negativeButtonClick)
                    show()
                }
            }
            Glide.with(itemView.context)
                .load(favUser.avatar_url)
                .into(binding.imgUser)
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(uriWithId: Uri)
    }
}
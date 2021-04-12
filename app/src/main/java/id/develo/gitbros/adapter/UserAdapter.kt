package id.develo.gitbros.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.develo.gitbros.DetailActivity
import id.develo.gitbros.databinding.UserItemLayoutBinding
import id.develo.gitbros.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: UserItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(holder) {
            with(mData[position]) {
                binding.tvUsername.text = username
                Glide.with(itemView.context)
                    .load(avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.imgUser)

                itemView.setOnClickListener {
                    Intent(itemView.context, DetailActivity::class.java).also {
                        it.putExtra(DetailActivity.EXTRA_Detail, detail_url)
                        itemView.context.startActivity(it)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int = mData.size
}
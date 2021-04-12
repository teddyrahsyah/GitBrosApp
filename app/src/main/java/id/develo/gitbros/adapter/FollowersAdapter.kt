package id.develo.gitbros.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.develo.gitbros.databinding.UserItemLayoutBinding
import id.develo.gitbros.model.UserFollow

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {

    private val mData = ArrayList<UserFollow>()

    fun setData(items: ArrayList<UserFollow>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class FollowersViewHolder(val binding: UserItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val binding = UserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        with(holder) {
            with(mData[position]) {
                binding.tvUsername.text = username
                Glide.with(itemView.context)
                    .load(avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.imgUser)
            }
        }
    }

    override fun getItemCount(): Int = mData.size
}
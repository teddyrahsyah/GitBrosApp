package id.develo.gitbros.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.develo.gitbros.IParentActivityCallback
import id.develo.gitbros.R
import id.develo.gitbros.adapter.FollowersAdapter
import id.develo.gitbros.adapter.FollowingAdapter
import id.develo.gitbros.databinding.FragmentFollowersBinding
import id.develo.gitbros.databinding.FragmentFollowingBinding
import id.develo.gitbros.model.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FollowingAdapter
    private val followingViewModel: FollowingViewModel by activityViewModels()
    private var parentActivityCallback: IParentActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            parentActivityCallback = context as IParentActivityCallback
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.adapter = adapter

        showLoading(true)

        val user = parentActivityCallback?.giveUserUrl()

        followingViewModel.setFollowers(context, "$user/following")

        followingViewModel.getFollowers().observe(viewLifecycleOwner, { following ->
            if (following != null) {
                adapter.setData(following)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
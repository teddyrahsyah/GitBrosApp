package id.develo.gitbros.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.develo.gitbros.IParentActivityCallback
import id.develo.gitbros.adapter.FollowersAdapter
import id.develo.gitbros.databinding.FragmentFollowersBinding
import id.develo.gitbros.model.FollowersViewModel
import id.develo.gitbros.model.MainViewModel


class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FollowersAdapter
    private val followersViewModel: FollowersViewModel by activityViewModels()
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
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowers.layoutManager = LinearLayoutManager(context)
        binding.rvFollowers.adapter = adapter

        showLoading(true)

        val user = parentActivityCallback?.giveUserUrl()

        followersViewModel.setFollowers(context, "$user/followers")

        followersViewModel.getFollowers().observe(viewLifecycleOwner, { followers ->
            if (followers != null) {
                adapter.setData(followers)
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
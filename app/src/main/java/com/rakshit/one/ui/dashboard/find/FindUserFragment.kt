package com.rakshit.one.ui.dashboard.find

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakshit.one.R
import com.rakshit.one.ui.dashboard.DashboardViewModel
import com.rakshit.one.ui.dashboard.Users
import com.rakshit.one.ui.dashboard.chat.MyChatFragment
import com.test.papers.config.ConstantsFirestore
import com.test.papers.kotlin.KotlinBaseFragment
import com.test.papers.kotlin.replaceFragment
import com.test.papers.utils.extension.gone
import com.test.papers.utils.extension.visible
import org.koin.android.ext.android.inject

class FindUserFragment : KotlinBaseFragment(R.layout.fragment_find_user) {

    private val viewModel: DashboardViewModel by inject()

    private val mNoBill: ConstraintLayout by lazy { requireView().findViewById(R.id.no_list_data) }

    private val adapter by lazy { UserListAdapter(this::onSelect, requireContext()) }

    private fun onSelect(users: Users) {
        replaceFragment<MyChatFragment>(
            userTag = MyChatFragment::class.java.name,
            container = android.R.id.content,
            addToBackStack = true,
            animation = true
        ) {
            putString(ConstantsFirestore.UID, users.uid)
            putString(ConstantsFirestore.NAME, users.name)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        observeViews()
    }

    private fun initView() {
        val mRecycler: RecyclerView = requireView().findViewById(R.id.recycler_chat)
        mRecycler.layoutManager = LinearLayoutManager(requireContext())
        mRecycler.adapter = adapter

        showLoading()
        viewModel.getAllUsers()
    }

    private fun observeViews() {
        viewModel.successUserListing.observe(viewLifecycleOwner) {
            hideLoading()
            if (it.isNotEmpty()) {
                adapter.addNewList(it)
                mNoBill.gone()
            } else {
                mNoBill.visible()
            }
        }
    }
}
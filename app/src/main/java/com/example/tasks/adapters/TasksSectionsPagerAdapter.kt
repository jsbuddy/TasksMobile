package com.example.tasks.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tasks.ui.tasks.CompletedTasksFragment
import com.example.tasks.ui.tasks.PendingTasksFragment

class TasksSectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingTasksFragment()
            1 -> CompletedTasksFragment()
            else -> PendingTasksFragment()
        }
    }
}

package com.example.tasks.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tasks.R
import com.example.tasks.ui.tasks.CompletedTasksFragment
import com.example.tasks.ui.tasks.PendingTasksFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_pending,
    R.string.tab_text_completed
)

class TasksSectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PendingTasksFragment()
            1 -> CompletedTasksFragment()
            else -> PendingTasksFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount() = 2
}

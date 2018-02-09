package id.lesku.lesku.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import id.lesku.lesku.R
import id.lesku.lesku.fragment.DetailStudentParentFragment
import id.lesku.lesku.fragment.DetailStudentProfileFragment
import id.lesku.lesku.fragment.DetailStudentRecapitulationFragment

class DetailStudentFragmentAdapter(private val mContext: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                DetailStudentRecapitulationFragment()
            }
            1->{
                DetailStudentParentFragment()
            }
            2->{
                DetailStudentProfileFragment()
            }
            else ->{
                DetailStudentRecapitulationFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 ->{
                mContext.getString(R.string.add_student_recapitulation_fragment_title)
            }
            1 ->{
                mContext.getString(R.string.add_student_parent_fragment_title)
            }
            2 ->{
                mContext.getString(R.string.add_student_profile_fragment_title)
            }
            else ->{
                null
            }
        }
    }
}
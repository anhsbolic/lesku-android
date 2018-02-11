package id.lesku.lesku.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.lesku.lesku.R
import id.lesku.lesku.activity.DetailStudentActivity
import id.lesku.lesku.model.Student
import kotlinx.android.synthetic.main.fragment_detail_student_parent.*

class DetailStudentParentFragment : Fragment() {

    private var student: Student? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_student_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get student data
        student = (activity as DetailStudentActivity).getStudentData()

        //Set data to UI
        if(student != null){
            setDataToUI(student!!)
        }

    }

    private fun setDataToUI(student: Student) {
        detailStudentParentTxtName.text = student.parent_name!!
        if(student.parent_phone != null){
            detailStudentParentTxtPhone.text = student.parent_phone!!
        }
        if(student.parent_whatsapp != null){
            detailStudentParentTxtWhatsapp.text = student.parent_whatsapp!!
        }
        detailStudentParentTxtAddress.text = student.parent_address!!
    }

    companion object {

        fun newInstance(): DetailStudentParentFragment {
            val fragment = DetailStudentParentFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}

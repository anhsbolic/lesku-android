package id.lesku.lesku.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import id.lesku.lesku.R
import id.lesku.lesku.activity.DetailStudentActivity
import id.lesku.lesku.activity.AddStudentActivity
import id.lesku.lesku.model.Student
import id.lesku.lesku.helper.SqliteDbHelper
import kotlinx.android.synthetic.main.fragment_detail_student_profile.*

class DetailStudentProfileFragment : Fragment() {

    private var student: Student? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get student data
        student = (activity as DetailStudentActivity).getStudentData()

        //Set data to UI
        if(student != null){
            setDataToUI(student!!)
        }

        //UI Handling & listener
        detailStudentProfileBtnEdit.setOnClickListener {
            editStudentData(student!!)
        }

        detailStudentProfileBtnDelete.setOnClickListener {
            val strFirstMessage = getString(R.string.detail_student_profile_dialog_delete_student_message)
            val strLastMessage = getString(R.string.detail_student_profile_dialog_delete_student_message_last)
            val strStudentName = student!!.name!!
            val strDialogMessage = "$strFirstMessage $strStudentName $strLastMessage"
            AlertDialog.Builder(activity as DetailStudentActivity)
                    .setMessage(strDialogMessage)
                    .setPositiveButton(R.string.detail_student_profile_dialog_delete_student_positive,
                            { _ , _ ->
                                deleteStudentData(student!!)
                            })
                    .setNegativeButton(R.string.detail_student_profile_dialog_delete_student_negative,
                            null)
                    .show()
        }

    }

    private fun setDataToUI(student: Student) {
        detailStudentProfileTxtName.text = student.name!!
        detailStudentProfileTxtPhone.text = student.phone!!
        detailStudentProfileTxtWhatsapp.text = student.whatsapp!!
        detailStudentProfileTxtAddress.text = student.address!!
        detailStudentProfileTxtSchool.text = student.school!!
        detailStudentProfileTxtSchoolLevel.text = student.school_level!!
        detailStudentProfileTxtGradeLevel.text = student.grade_level!!
        detailStudentProfileTxtSubject.text = student.subject!!
    }

    private fun editStudentData(student: Student) {
        val intent = Intent(activity, AddStudentActivity::class.java)
        intent.putExtra(AddStudentActivity.IS_EDIT_MODE, true)
        intent.putExtra(AddStudentActivity.EDIT_MODE, AddStudentActivity.EDIT_MODE_DATA_STUDENT_ONLY)
        intent.putExtra(AddStudentActivity.DATA_STUDENT, student)
        startActivityForResult(intent, EDIT_PROFILE)
    }

    private fun deleteStudentData(student: Student) {
        val dbStudent = SqliteDbHelper(activity as DetailStudentActivity)
        val dataUpdated = dbStudent.deleteStudent(student)
        if(dataUpdated == -1) {
            Toast.makeText(activity, "Gagal menghapus siswa",
                    Toast.LENGTH_SHORT).show()
        }else{
            (activity as DetailStudentActivity).deletedStudentResult()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            EDIT_PROFILE ->{
                when(resultCode){
                    EDIT_PROFILE_SUCCESS ->{
                        if(data != null){
                            student = data.getParcelableExtra(EDIT_PROFILE_SUCCESS_DATA_STUDENT)
                            setDataToUI(student!!)
                            (activity as DetailStudentActivity).updateStudentDataAndUI(student!!)
                        }
                    }
                }
            }
            else ->{
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    companion object {
        val EDIT_PROFILE: Int = 11
        val EDIT_PROFILE_SUCCESS: Int = 12
        val EDIT_PROFILE_SUCCESS_DATA_STUDENT = "EditProfileSuccessDataStudent"

        fun newInstance(): DetailStudentProfileFragment {
            val fragment = DetailStudentProfileFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}

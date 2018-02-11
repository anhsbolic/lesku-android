package id.lesku.lesku.fragment

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import id.lesku.lesku.R
import id.lesku.lesku.activity.DetailStudentActivity
import id.lesku.lesku.activity.AddStudentActivity
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

        //Btn Handling & listener
        detailStudentParentBtnEdit.setOnClickListener {
            editParentStudentData(student!!)
        }

        detailStudentParentBtnCall.setOnClickListener {
            val strPhoneNumber = student!!.parent_phone!!
            dialPhoneNumber(strPhoneNumber)
        }

        detailStudentParentBtnSendMessage.setOnClickListener {
            var strPhoneNumber = student!!.parent_phone!!
            if(strPhoneNumber.startsWith("0")){
                strPhoneNumber = strPhoneNumber.substring(1)
                strPhoneNumber = "+62$strPhoneNumber"
            }
            sendMessageToPhoneNumber(strPhoneNumber)
        }

        detailStudentParentBtnWhatsapp.setOnClickListener {
            var strWhatsappNumber = student!!.parent_whatsapp!!
            if(strWhatsappNumber.startsWith("0")){
                strWhatsappNumber = strWhatsappNumber.substring(1)
                strWhatsappNumber = "+62$strWhatsappNumber"
            }
            openWhatsapp(strWhatsappNumber)
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

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + phoneNumber)
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            activity!!.startActivity(intent)
        }
    }

    private fun sendMessageToPhoneNumber(strPhoneNumber: String) {
        activity!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",
                strPhoneNumber, null)))
    }

    private fun openWhatsapp(waNumber: String){
        try{
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT,"")
            sendIntent.putExtra("jid", waNumber+"@s.whatsapp.net")
            sendIntent.`package` = "com.whatsapp"
            activity!!.startActivity(sendIntent)
        }catch (e: Exception){
            Toast.makeText(activity, "Whatsapp belum terinstall di HP Anda",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun editParentStudentData(student: Student) {
        val intent = Intent(activity, AddStudentActivity::class.java)
        intent.putExtra(AddStudentActivity.IS_EDIT_MODE, true)
        intent.putExtra(AddStudentActivity.EDIT_MODE, AddStudentActivity.EDIT_MODE_DATA_STUDENT_PARENT_ONLY)
        intent.putExtra(AddStudentActivity.DATA_STUDENT, student)
        startActivityForResult(intent, EDIT_PARENT_PROFILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            EDIT_PARENT_PROFILE ->{
                when(resultCode){
                    EDIT_PARENT_PROFILE_SUCCESS ->{
                        if(data != null){
                            student = data.getParcelableExtra(EDIT_PARENT_PROFILE_SUCCESS_DATA_STUDENT)
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

        val EDIT_PARENT_PROFILE: Int = 31
        val EDIT_PARENT_PROFILE_SUCCESS: Int = 32
        val EDIT_PARENT_PROFILE_SUCCESS_DATA_STUDENT = "EditParentProfileSuccessDataStudent"

        fun newInstance(): DetailStudentParentFragment {
            val fragment = DetailStudentParentFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}

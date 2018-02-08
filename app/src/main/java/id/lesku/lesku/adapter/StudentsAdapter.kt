package id.lesku.lesku.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import id.lesku.lesku.R
import id.lesku.lesku.model.Student
import kotlinx.android.synthetic.main.fragment_students_adapter_list.view.*

class StudentsAdapter(private val dataStudents: ArrayList<Student>,
                      private val onItemClickListener: StudentsAdapter.OnItemClickListener)
    : RecyclerView.Adapter<StudentsAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    interface OnItemClickListener{
        fun onItemClick(student: Student, position: Int)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        internal var txtStudentName : TextView = itemView.studentsAdapterListName
        internal var txtStudentGradeLevel : TextView = itemView.studentsAdapterListGradeLevel
        internal var txtStudentSchool : TextView = itemView.studentsAdapterListSchool
        internal var txtStudentAddress : TextView = itemView.studentsAdapterListAddress

        fun setOnClickListener(onClickListener: View.OnClickListener) {
            itemView.setOnClickListener(onClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_students_adapter_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtStudentName.text = dataStudents[position].name!!
        holder.txtStudentGradeLevel.text = dataStudents[position].grade_level!!
        holder.txtStudentSchool.text = dataStudents[position].school!!
        holder.txtStudentAddress.text = dataStudents[position].address!!

        holder.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(dataStudents[holder.adapterPosition],
                    holder.adapterPosition)
        })
    }

    override fun getItemCount(): Int {
        return dataStudents.size
    }
}
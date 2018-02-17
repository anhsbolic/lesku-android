package id.lesku.lesku.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.lesku.lesku.R
import id.lesku.lesku.model.ScheduleDailyNotes
import kotlinx.android.synthetic.main.activity_create_schedule_daily_notes_adapter_list.view.*

class CreateScheduleDailyNotesAdapter(private val dataDailyNotes: ArrayList<ScheduleDailyNotes>)
    : RecyclerView.Adapter<CreateScheduleDailyNotesAdapter.ViewHolder>(){

    private lateinit var mContext: Context

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        internal var txtDay = itemView.createScheduleDailyNotesAdapterListTxtDay
        internal var etSubject = itemView.createScheduleDailyNotesAdapterListEtSubject
        internal var etNotes  = itemView.createScheduleDailyNotesAdapterListEtNotes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.activity_create_schedule_daily_notes_adapter_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDay.text = dataDailyNotes[position].day!!

        holder.etSubject.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(editable: Editable?) {
                if(editable != null){
                    if(editable.isNotEmpty()){
                        val subject = editable.toString().trim()
                        dataDailyNotes[holder.adapterPosition].subject = subject
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        holder.etNotes.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(editable: Editable?) {
                if(editable != null){
                    if(editable.isNotEmpty()){
                        val notes = editable.toString()
                        dataDailyNotes[holder.adapterPosition].notes = notes
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun getItemCount(): Int {
        return dataDailyNotes.size
    }
}

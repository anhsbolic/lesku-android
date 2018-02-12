package id.lesku.lesku.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekViewEvent

import id.lesku.lesku.R
import id.lesku.lesku.activity.DashboardActivity
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*

class ScheduleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mMonthChangeListener: MonthLoader.MonthChangeListener = object : MonthLoader.MonthChangeListener {
            override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> {
                val events: MutableList<WeekViewEvent> = ArrayList()
                val startTime = Calendar.getInstance()
                startTime.set(Calendar.HOUR_OF_DAY, 3)
                startTime.set(Calendar.MINUTE, 0)
                startTime.set(Calendar.MONTH, newMonth - 1)
                startTime.set(Calendar.YEAR, newYear)
                val endTime = startTime.clone() as Calendar
                endTime.add(Calendar.HOUR, 1)
                endTime.set(Calendar.MONTH, newMonth - 1)
                val event = WeekViewEvent(1, "tes",null, startTime, endTime)
                event.color = ContextCompat.getColor(activity as DashboardActivity, R.color.colorPrimary)
                events.add(event)

                return events
            }
        }

        scheduleWeekView.monthChangeListener = mMonthChangeListener
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): ScheduleFragment {
            val fragment = ScheduleFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

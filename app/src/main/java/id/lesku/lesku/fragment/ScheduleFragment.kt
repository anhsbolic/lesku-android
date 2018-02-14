package id.lesku.lesku.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode

import id.lesku.lesku.R
import id.lesku.lesku.activity.DashboardActivity
import id.lesku.lesku.utils.EventCalendarDecorator
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleCalendar.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

        scheduleCalendar.setCurrentDate(Date())
        scheduleCalendar.setSelectedDate(Date())
        val color = ContextCompat.getColor(activity as DashboardActivity, R.color.colorPrimary)
        val dates: ArrayList<CalendarDay> = ArrayList()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 5)
        val day = CalendarDay.from(calendar)
        dates.add(day)
        scheduleCalendar.addDecorator(EventCalendarDecorator(color,dates))

        scheduleCalendar.setOnDateChangedListener { widget, date, selected ->
            Log.d("TES", date.toString())
        }

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

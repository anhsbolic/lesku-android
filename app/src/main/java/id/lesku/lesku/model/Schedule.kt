package id.lesku.lesku.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@SuppressLint("ParcelCreator")
@Parcelize
data class Schedule(var id_schedule: Int?,
                    var created_date: Date?,
                    var updated_date: Date?,
                    var id_student: Int?,
                    var another_address: String?,
                    var start_date: Date?,
                    var total_weeks: Int?,
                    var start_time: String?,
                    var end_time: String?,
                    var alarm_time: Long?,
                    var days_details: String?) : Parcelable {
    constructor():this(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null)
}
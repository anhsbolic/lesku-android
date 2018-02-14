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
                    var schedule_date: Date?,
                    var start_time: String?,
                    var end_time: String?,
                    var reminder: Long?,
                    var another_location: String?,
                    var notes: String?) : Parcelable {
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
            null)
}
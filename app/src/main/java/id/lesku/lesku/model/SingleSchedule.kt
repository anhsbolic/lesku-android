package id.lesku.lesku.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@SuppressLint("ParcelCreator")
@Parcelize
data class SingleSchedule(var id_single_schedule: Int?,
                          var created_date: Date?,
                          var updated_date: Date?,
                          var id_schedule: Int?,
                          var date: Date?,
                          var subject: String?,
                          var notes: String?,
                          var status: String?) : Parcelable {
    constructor(): this(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null)
}
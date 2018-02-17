package id.lesku.lesku.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@SuppressLint("ParcelCreator")
@Parcelize
data class ScheduleDailyNotes(var day: String?,
                              var isChecked: Boolean?,
                              var subject: String?,
                              var notes: String?) : Parcelable {
    constructor():this(
            null,
            null,
            null,
            null
    )
}
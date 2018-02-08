package id.lesku.lesku.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.util.*

@SuppressLint("ParcelCreator")
@Keep
@Parcelize
data class Student(var id_student: Int?,
                   var created_date: Date?,
                   var updated_date: Date?,
                   var name: String?,
                   var phone: String?,
                   var whatsapp: String?,
                   var address: String?,
                   var school: String?,
                   var school_level: String?,
                   var grade_level: String?,
                   var subject: String?,
                   var parent_name: String?,
                   var parent_phone: String?,
                   var parent_whatsapp: String?,
                   var parent_address:String?) : Parcelable {
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
            null,
            null,
            null,
            null,
            null
    )

}
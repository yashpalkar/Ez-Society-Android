package com.ezmanagement.society.Model

import android.os.Parcel
import android.os.Parcelable

data class VisitorModel(


val contact_no: String?,
val created_at: Any?,
val guard_id: Any?,
val id: Any?,
val image: String?,
val last_visited_at: Any?,
val society_id: Any?,
val name: String?,
val verified: Boolean,
val updated_at: Any?

) : Parcelable {
    constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readValue(Any::class.java.classLoader),
    parcel.readValue(Any::class.java.classLoader),
    parcel.readValue(Any::class.java.classLoader),
    parcel.readString(),
    parcel.readValue(Any::class.java.classLoader),
    parcel.readValue(Any::class.java.classLoader),
    parcel.readString(),
    parcel.readByte() != 0.toByte(),
    parcel.readValue(Any::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(contact_no)
        parcel.writeValue(created_at)
        parcel.writeValue(guard_id)
        parcel.writeValue(id)
        parcel.writeString(image)
        parcel.writeValue(last_visited_at)
        parcel.writeValue(society_id)
        parcel.writeString(name)
        parcel.writeByte(if (verified) 1 else 0)
        parcel.writeValue(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VisitorModel> {
        override fun createFromParcel(parcel: Parcel): VisitorModel {
            return VisitorModel(parcel)
        }

        override fun newArray(size: Int): Array<VisitorModel?> {
            return arrayOfNulls(size)
        }
    }
}
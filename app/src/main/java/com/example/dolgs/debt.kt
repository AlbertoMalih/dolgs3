package com.example.dolgs

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Debt
(var description: String, var namePartner: String, var typeDebt: TypeDebt, var age: String, var date: Date, var id: Long = -1L) : Parcelable {

    constructor(): this("", "", TypeDebt.HereMust, "", Date())

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            TypeDebt.valueOf(parcel.readString()),
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(namePartner)
        parcel.writeString(typeDebt.name)
        parcel.writeString(age)
        parcel.writeLong(date.time)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    enum class TypeDebt {
        HereMust, IMust;
    }

    companion object CREATOR : Parcelable.Creator<Debt> {
        override fun createFromParcel(parcel: Parcel): Debt {
            return Debt(parcel)
        }

        override fun newArray(size: Int): Array<Debt?> {
            return arrayOfNulls(size)
        }
    }
}

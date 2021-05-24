package com.rfapp.e_statsi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Berkas (
    var nama:String? = "",
    var data_url:String? = "",
): Parcelable
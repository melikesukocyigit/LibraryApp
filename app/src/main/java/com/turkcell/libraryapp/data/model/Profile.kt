package com.turkcell.libraryapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile (
    @SerialName(value = "user_id") val userId: String,
    val role: String,
    @SerialName(value = "full_name")  val fullName: String,
    @SerialName(value = "student_no") val studentNo: String? = null
){


}
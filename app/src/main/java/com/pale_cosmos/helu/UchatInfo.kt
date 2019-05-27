package com.pale_cosmos.helu

import java.io.Serializable

class UchatInfo : Serializable {

    var key: String? = null
    var nickname: String? = null
    var phone: String? = null
    var university: String? = null
    var department: String? = null
    var gender: Boolean? = null

    fun setInfos(
        key: String,
        nickname: String,
        phone: String,
        university: String,
        department: String,
        genderString: String
    ) {
        this.key = key
        this.nickname = nickname
        this.phone = phone
        this.university = university
        this.department = department
        if (genderString == "true") this.gender = true
        else this.gender = false
    }
}
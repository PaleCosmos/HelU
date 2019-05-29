package com.pale_cosmos.helu


import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Friends(
) {


    var nickname: String = ""
    var key: String = ""
    var phone: String = ""
    var photo: String = ""
    var university: String = ""
    var department: String = ""

    fun setValue(
        nickname: String, key: String, phone: String, photo: String?
        , university: String, department: String
    ) {
        this.nickname = nickname
        this.key = key
        this.phone = phone
        if(photo!=null)
        this.photo = photo
        this.university = university
        this.department = department
    }

    @Exclude
    fun toMap(): Map<String, Any?> {

        var result = HashMap<String, Any?>()
        result.put("nickname", nickname)
        result.put("key", key)
        result.put("phone", phone)
        result.put("photo", photo)
        result.put("university", university)
        result.put("department", department)
        return result
    }
}
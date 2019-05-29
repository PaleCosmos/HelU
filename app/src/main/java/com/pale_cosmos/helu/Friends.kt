package com.pale_cosmos.helu


import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Friends(
    nickname: String, key: String, phone: String, photo: String?
    , university: String, department: String
) {

    val nickname = nickname
    val key = key
    val phone = phone
    val photo = photo
    val university = university
    val department = department
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
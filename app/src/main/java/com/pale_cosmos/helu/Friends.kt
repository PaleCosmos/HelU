package com.pale_cosmos.helu


import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Friends(val nickname:String,val key:String, val phone:String,val photo:String?
,val university:String,val department:String) {

    @Exclude
    fun toMap():Map<String,Any?>
    {

        var result = HashMap<String,Any?>()
        result.put("nickname",nickname)
        result.put("key",key)
        result.put("phone",phone)
        result.put("photo",photo)
        result.put("university",university)
        result.put("department",department)
        return result
    }
}
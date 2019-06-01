package com.pale_cosmos.helu

import android.graphics.Bitmap
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class ChatValue :Serializable{
    var nickname:String?=""
    var type:String?=""
    var key:String?=""
    var message:String? =""
    var photo: String=""
    var profile:String=""


    var yourkey:String=""
    var yournickname:String=""
    var yourprofile:String=""

    @Exclude
    fun toMap():Map<String,Any?>
    {

        var result = HashMap<String,Any?>()
        result.put("type",type)
        result.put("nickname",nickname)
        result.put("yournickname",yournickname)
        result.put("key",key)
        result.put("yourprofile",profile)
        result.put("yourkey",yourkey)
        result.put("message",message)
        result.put("photo",photo)
        result.put("profile",profile)
        return result
    }

}
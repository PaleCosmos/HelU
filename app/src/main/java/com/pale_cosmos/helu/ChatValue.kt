package com.pale_cosmos.helu

import android.graphics.Bitmap
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class ChatValue :Serializable{
    var nickname:String??=""
    var type:String?=""
    var key:String?=""
    var message:String? =""
    var photo: String=""



    @Exclude
    fun toMap():Map<String,Any?>
    {

        var result = HashMap<String,Any?>()
        result.put("type",type)
        result.put("nickname",nickname)
        result.put("key",key)
        result.put("message",message)
        result.put("photo",photo)
        return result
    }

}
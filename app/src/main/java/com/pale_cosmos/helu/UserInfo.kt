package com.pale_cosmos.helu
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlin.collections.HashMap

@IgnoreExtraProperties
class UserInfo(){
    var email: String? = ""
    var nickname: String? = ""
    var gender:Boolean?=null
    var count:Boolean?=true
    var phone:String?=""
    var university:String?=""
    var department:String?=""
    /*
    var mon: Array<String>? = null
    var tue: Array<String>? = null
    var wed: Array<String>? = null
    var thu: Array<String>? = null
    var fri: Array<String>? = null
    var sat: Array<String>? = null
    var sun: Array<String>? = null
*/
    @Exclude
    fun toMap():Map<String,Any?>
    {
        var result = HashMap<String,Any?>()
        result.put("email",email)
        result.put("nickname",nickname)
        result.put("count",count)
        result.put("gender",gender)
        result.put("phone",phone)
        result.put("university",university)
        result.put("department",department)
        return result
    }
}
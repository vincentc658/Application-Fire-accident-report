package com.app.fire.model

class UserModelFirestore(
    var id:String= "",
    var name:String= "",
    var email:String= "",
    var noHp:String= "",
    var avatar:String= "",
    var totalQuestion:String= "",
    var typeUser: Int=0
) {
    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val EMAIL = "email"
        const val NO_HANDPHONE = "no_handphone"
        const val JURUSAN = "jurusan"
        const val INSTITUTE = "institute"
        const val AVATAR = "avatar"
    }
    
}
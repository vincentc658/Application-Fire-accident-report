package com.app.fire.model

class AccidentModelFirestore(
    var rumahRusak:Int= 0,
    var korbanTerdampak:Int= 0,
    var jumlahKK:Int= 0,
    var lokasi:String= "",
    var waktu:String= "",
    val time: Long=0L,
) : BaseModel(){
}
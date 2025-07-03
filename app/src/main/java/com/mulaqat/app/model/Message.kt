package com.mulaqat.app.model

data class Message(
    var value: String,
    var id: String,
    var sender_id: String,
    var username: String,
    var country: String? = null,
    var pic: String,
    var msg: String,
    var time: String,
    var readstatus: String,
    var isbusiness: String? = null,
    var rId: String? = null,
    var paid: Int = 0
) {

}
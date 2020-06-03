package com.ainsigne.mobilesocialblogapp.models

data class BodyRequest (var to : String,var priority : String, var data: BodyData)
data class BodyData (var calledId : String, var loc : String)
data class BodyResponse(var multicast_id : Long,var success : Int,var failure : Int,var canonical_ids : Int, var results : Array<BodyResult>)
data class BodyResult(var message_id : String)
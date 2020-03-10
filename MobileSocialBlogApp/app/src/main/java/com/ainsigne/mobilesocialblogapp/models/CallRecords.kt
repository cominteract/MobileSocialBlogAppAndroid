package com.ainsigne.mobilesocialblogapp.models

data class CallRecords (var id : String? = null,
                        var callerId : String? = null,
                        var callerName : String? = null,
                        var calledId : String? = null,
                        var calledName : String? = null,
                        var conferenceName : String? = null,
                        var timestampStarted : String? = null,
                        var timestampEnded : String? = null
                        ){
    companion object{
        fun convertToKeyVal(callRecord : CallRecords) : HashMap<String, Any>{
            var map = HashMap<String,Any>()
            callRecord.id?.let { map["id"] = it }
            callRecord.callerId?.let { map["callerId"] = it }
            callRecord.callerName?.let { map["callerName"] = it }
            callRecord.calledId?.let { map["calledId"] = it }
            callRecord.calledName?.let { map["calledName"] = it}
            callRecord.conferenceName?.let { map["conferenceName"] = it }
            callRecord.timestampStarted?.let { map["timestampStarted"] = it }
            callRecord.timestampEnded?.let { map["timestampEnded"] = it }

            return map
        }
    }


}
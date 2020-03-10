package com.ainsigne.mobilesocialblogapp.manager

import android.util.Log
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.Conversions
import com.facebook.infer.annotation.Mutable
import com.google.firebase.database.*

class CallAPIManager {
    var ref : DatabaseReference = FirebaseDatabase.getInstance().reference

    fun addCall(keyval: Map<String, Any?>, completion: (Error?, String) -> Unit) {

            if (keyval.keys.contains("callerName") && keyval.keys.contains("calledName")) {
                val conferenceName = keyval["conferenceName"] as String
                ref.child(Constants.calls).child(conferenceName).setValue(keyval
                ) { err, ref ->
                    if (err != null)
                        completion(null, "Successfully added ${keyval["id"]}")
                    else
                        completion(err, "Failure added ${keyval["id"]}")
                }
            }
    }
    fun retrieveIncomingCall(userId : String, callsRetrieved: CallsRetrieved) {
        val eventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshots: DataSnapshot){
                if(snapshots.value != null && snapshots.value is HashMap<*,*>){

                    Log.d(" Call from manager"," Call from manager ${snapshots.value}")
                    var callsForUser = Conversions.convertToAllCalls((snapshots.value as HashMap<String,Any>)).filter {
                        it.calledId == userId && it.timestampEnded == null
                    }.first()
                    callsRetrieved.retrievedCalls(callsForUser,"")

                } else {
                    Log.d(" Call from manager"," Call from manager ${snapshots.value}")
                }
            }
        }
        Log.d(" Call from manager"," Call from manager retrieving")
        ref.child(Constants.calls).addValueEventListener(eventListener)
    }
}
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

                    if(!Conversions.convertToAllCalls((snapshots.value as HashMap<String,Any>)).isNullOrEmpty()){
                        val callRecords = Conversions.convertToAllCalls((snapshots.value as HashMap<String,Any>))

                        var callsForUser = callRecords.filter {
                            it.calledId == userId && it.callstate == Constants.CALLSTARTED
                        }
                        var callsEnded = callRecords.filter {
                            it.endedId == userId && it.callstate == Constants.CALLENDED
                        }

                        if(!callsForUser.isNullOrEmpty()){
                            Log.d(" Call from manager"," Call Retrieved from manager ${callRecords[0].calledId} $userId")
                            callsRetrieved.retrievedCalls(callsForUser.first(),"")
                        }

                        if(!callsEnded.isNullOrEmpty()){
                            Log.d(" Leaving "," Leaving from call api manager ")
                            //Log.d(" Call from manager"," Call Ended from manager ${callRecords[0].calledId} $userId")
                            callsRetrieved.endedCalls(callsEnded.first(),"")
                        }

                    }
                } else {
                    Log.d(" Call from manager"," Call from manager ${snapshots.value}")
                }
            }
        }

        ref.child(Constants.calls).addValueEventListener(eventListener)
    }
}
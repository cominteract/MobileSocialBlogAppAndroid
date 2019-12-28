package com.ainsigne.mobilesocialblogapp.manager

import android.util.Log
import androidx.core.net.toUri
import com.ainsigne.mobilesocialblogapp.models.*
import com.ainsigne.mobilesocialblogapp.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import java.io.File


interface ReferencesRetrieved{
    fun didRetrievePostImages(posts : ArrayList<Posts>)
    fun didRetrieveUserImages(users : ArrayList<Users>)
}

/**
 * Implements the apimanager abstract class (which derives from the apiprotocol) and backend
 * for fetching the data
 **/
class FirebaseAPIManager : APIManager() {


    var ref : DatabaseReference
    var storage :  StorageReference

    init {
        ref = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance().reference
    }


    override fun updateById(id: String, endpoint: String, keyval: HashMap<String, Any>) {
        ref.child(endpoint).child(id).setValue(keyval)
    }

    override fun addUser(keyval: Map<String, Any?>, completion: (Error?, String) -> Unit) {
        if (keyval.keys.contains("username")){
            val username = keyval["username"] as String


            ref.child(Constants.users).child(username).setValue(keyval
            ) { err, ref ->
                if (err != null)
                    completion(null, "Successfully added ${keyval["id"]}")
                else
                    completion(err, "Failure added ${keyval["id"]}")
            }

        }

    }

    override fun addComments(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
        if (keyval.keys.contains("id")){
            ref.child(Constants.comments).child(keyval["id"] as String).setValue(keyval
            ) { err, ref ->
                if (err != null)
                    completion(null, "Successfully added ${keyval["id"]}")
                else
                    completion(err, "Failure added ${keyval["id"]}")
            }
        }
    }

    override fun addPosts(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
        if (keyval.keys.contains("id")){
            ref.child(Constants.posts).child(keyval["id"] as String).setValue(keyval
            ) { err, ref ->
                if (err != null)
                    completion(null, "Successfully added ${keyval["id"]}")
                else
                    completion(err, "Failure added ${keyval["id"]}")
            }
        }
    }

    override fun addChats(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
        if (keyval.keys.contains("id")){
            ref.child(Constants.chats).child(keyval["id"] as String).setValue(keyval
            ) { err, ref ->
                if (err != null)
                    completion(null, "Successfully added ${keyval["id"]}")
                else
                    completion(err, "Failure added ${keyval["id"]}")
            }
        }
    }

    override fun addSession(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {

        if (keyval.keys.contains("id")){
            ref.child(Constants.sessions).child(keyval["id"] as String).setValue(keyval
            ) { err, ref ->
                if (err != null)
                    completion(null, "Successfully added ${keyval["id"]}")
                else
                    completion(err, "Failure added ${keyval["id"]}")
            }
        }

    }

    override fun uploadImage(file: File, imageName: String, completion: (Error?, String) -> Unit) {

        Log.d(" Uploading "," Uploading ")
// Create the file metadata
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg")
            .build()

// Upload file and metadata to the path 'images/mountains.jpg'
        val uploadTask = storage.child("images/$imageName").putFile(file.toUri(), metadata)

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            println("Upload is $progress% done")
        }.addOnPausedListener {
            println("Upload is paused")
        }.addOnFailureListener {
            // Handle unsuccessful uploads
            completion(java.lang.Error(), "Failed")
        }.addOnSuccessListener {
            completion(null, "Success")
            // Handle successful uploads on complete
            // ...
        }
    }

    override fun downloadImage(imageName: String, completion: (Error?, String) -> Unit) {
        storage.child("images/$imageName").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            // Use the bytes to display the image
            completion(null,"Success")
        }.addOnFailureListener {
            completion(java.lang.Error(),"Failure")
            // Handle any errors
        }



    }

    override fun getComments(username: String, commentsConversion: CommentsConversion) {
        val commentsReference = ref.child(Constants.comments).child(username)
        Conversions.convertToComments(commentsReference, commentsConversion)
    }

    override fun getPost(username: String, postConversion: PostsConversion) {
        val postReference = ref.child(Constants.posts).child(username)
        Conversions.convertToPosts(postReference,postConversion)
    }

    override fun getUser(username: String, userConversion: UsersConversion) {
        val userReference = ref.child(Constants.users).child(username)
        Conversions.convertToUsers(userReference,userConversion)
    }

    override fun listImagesForPosts(
        posts: ArrayList<Posts>,
        referencesRetrieved: ReferencesRetrieved
    ) {
        val storageReference = storage.child("images")
        var imagedPosts = ArrayList<Posts>()
        storageReference.listAll().addOnSuccessListener { listResult ->
            listResult.prefixes.forEach { prefix ->
                // All the prefixes under listRef.
                // You may call listAll() recursively on them.
            }
            var count = 0
            listResult.items.forEach { item ->
                // All the items under listRef.
                count++
                item.downloadUrl.addOnSuccessListener { uri ->
                    uri.path.let { path ->
                        if(posts.filter {
                                it.id != null && path.contains(it.id!!)
                            }.isNotEmpty())
                        {
                            val post = posts.filter { it.id != null && path.contains(it.id!!) }[0]

                            post.url = "$uri"
                            imagedPosts.add(post)
                        }
                    }

                    if(listResult.items.size == count)
                        referencesRetrieved.didRetrievePostImages(imagedPosts)
                }
            }
        }.addOnFailureListener {
            print(" Error listing post images ")
                // Uh-oh, an error occurred!
        }
    }

    override fun listImagesForUsers(
        users: ArrayList<Users>,
        referencesRetrieved: ReferencesRetrieved
    ) {
        val storageReference = storage.child("images")
        var imagedUsers = ArrayList<Users>()
        storageReference.listAll().addOnSuccessListener { listResult ->
            listResult.prefixes.forEach { prefix ->
                // All the prefixes under listRef.
                // You may call listAll() recursively on them.
            }

            var count = 0
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    count += 1
                    uri.path.let { path ->
                        if(users.filter {
                                it.id != null && path.contains(it.id!!)
                            }.isNotEmpty())
                        {
                            val user = users.filter { it.id != null && path.contains(it.id!!) }[0]

                            user.photoUrl = "$uri"
                            imagedUsers.add(user)
                        }
                    }
                    if(listResult.items.size == count)
                        referencesRetrieved.didRetrieveUserImages(imagedUsers)
                }
                // All the items under listRef.
            }
        }.addOnFailureListener {
            print(" Error listing user images ")
            // Uh-oh, an error occurred!
        }
    }

    override fun postExists(id: String, postConversion: PostsConversion) {

    }

    override fun retrieveAllChatMessages(chatsRetrieved: ChatMessagesRetrieved) {
        val eventListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                chatsRetrieved.retrievedChatMessages(null,"")
            }

            override fun onDataChange(snapshots: DataSnapshot){
                if(snapshots.value != null && snapshots.value is HashMap<*,*>){
                    chatsRetrieved.retrievedChatMessages(Conversions.convertToAllChatMessages((snapshots.value as HashMap<String,Any>)),"")
                    Log.d(" Chat count ", " count ${(snapshots.value as HashMap<*,*>).keys.size}")
                } else {
                    val messages = ArrayList<ChatMessages>()
                    chatsRetrieved.retrievedChatMessages(messages,"")
                }
            }

        }

        ref.child(Constants.chats).addListenerForSingleValueEvent(eventListener)

    }

    override fun retrieveAllChatSession(chatsRetrieved: ChatSessionRetrieved) {
        ref.child(Constants.sessions).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                    chatsRetrieved.retrievedChatSession(null,"")
            }

            override fun onDataChange(snapshots: DataSnapshot){
                if(snapshots.value != null && snapshots.value is HashMap<*,*>){
                    Log.d(" Session count ", " count ${(snapshots.value as HashMap<*,*>).keys.size}")
                    chatsRetrieved.retrievedChatSession(Conversions.convertToAllChatSession((snapshots.value as HashMap<String,Any>)),"")
                }else {
                    val chatSessions = ArrayList<ChatSession>()
                    chatsRetrieved.retrievedChatSession(chatSessions,"")
                }
            }

        })
    }

    override fun retrieveAllComments(commentsRetrieved: CommentsRetrieved) {
        ref.child(Constants.comments).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                    commentsRetrieved.retrievedComments(null,"")
            }

            override fun onDataChange(snapshots: DataSnapshot){
                if(snapshots.value != null && snapshots.value is HashMap<*,*>){
                    Log.d(" Comment count ", " count ${(snapshots.value as HashMap<*,*>).keys.size}")
                    commentsRetrieved.retrievedComments(Conversions.convertToAllComments((snapshots.value as HashMap<String,Any>)),"")
                }else {
                    val comments = ArrayList<Comments>()
                    commentsRetrieved.retrievedComments(comments,"")
                }
            }

        })
    }

    override fun retrieveAllPosts(postsRetrieved: PostsRetrieved) {
        ref.child(Constants.posts).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                    postsRetrieved.retrievedPosts(null,"")
            }

            override fun onDataChange(snapshots: DataSnapshot){
                if(snapshots.value != null && snapshots.value is HashMap<*,*>){
                    postsRetrieved.retrievedPosts(Conversions.convertToAllPosts((snapshots.value as HashMap<String,Any>)),"")
                    Log.d(" Post count ", " count ${(snapshots.value as HashMap<*,*>).keys.size}")
                }else {

                    postsRetrieved.retrievedPosts(ArrayList<Posts>() ,"")
                }
            }

        })
    }

    override fun retrieveAllUsers(usersRetrieved: UsersRetrieved) {
        ref.child(Constants.users).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                usersRetrieved.retrievedUsers(null,"")
            }

            override fun onDataChange(snapshots: DataSnapshot){
                if(snapshots.value != null && snapshots.value is HashMap<*,*>){
                    usersRetrieved.retrievedUsers(Conversions.convertToAllUsers(((snapshots.value as HashMap<String,Any>))),"")
                    Log.d(" User count ", " count ${(snapshots.value as HashMap<*,*>).keys.size}")
                }else {
                    usersRetrieved.retrievedUsers(ArrayList<Users>(),"")
                }

            }

        })
    }

    override fun userExists(username: String, userConversion: UsersConversion) {

    }

}

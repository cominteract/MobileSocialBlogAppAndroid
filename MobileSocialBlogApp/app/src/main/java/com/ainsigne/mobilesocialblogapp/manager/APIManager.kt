package com.ainsigne.mobilesocialblogapp.manager


import com.ainsigne.mobilesocialblogapp.interfaces.APIInterface
import com.ainsigne.mobilesocialblogapp.models.*
import com.ainsigne.mobilesocialblogapp.utils.CommentsConversion
import com.ainsigne.mobilesocialblogapp.utils.PostsConversion
import com.ainsigne.mobilesocialblogapp.utils.UsersConversion
import java.io.File

interface ErrorRetrieved{
    fun retrievedError(error : Error, revoked : Boolean)
}

interface UsersRetrieved{
    fun retrievedUsers(users : ArrayList<Users>?, msg : String)
}

interface CallsRetrieved{
    fun retrievedCalls(callRecords : CallRecords , msg : String)
    fun endedCalls(callRecords : CallRecords , msg : String)
}

interface PostsRetrieved{
    fun retrievedPosts(posts : ArrayList<Posts>?, msg : String)
}

interface CommentsRetrieved{
    fun retrievedComments(comments : ArrayList<Comments>?, msg : String)
}

interface ChatSessionRetrieved{
    fun retrievedChatSession(sessions : ArrayList<ChatSession>?, msg : String)
}

interface ChatMessagesRetrieved{
    fun retrievedChatMessages(messages : ArrayList<ChatMessages>?, msg : String)
}

/**
 * serves as an abstract class for the api protocol and its implementation
 **/
open class APIManager : APIInterface {
    override fun updateById(id: String, endpoint: String, keyval: HashMap<String, Any>) {

    }

    override fun addUser(keyval: Map<String, Any?>, completion: (Error?, String) -> Unit) {
    }

    override fun addComments(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
    }

    override fun addPosts(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
    }

    override fun addChats(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
    }

    override fun addSession(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
    }

    override fun uploadImage(file: File, imageName: String, completion: (Error?, String) -> Unit) {
    }

    override fun downloadImage(imageName: String, completion: (Error?, String) -> Unit) {
    }

    override fun listImagesForPosts(
        posts: ArrayList<Posts>,
        referencesRetrieved: ReferencesRetrieved
    ) {

    }

    override fun getUser(username: String, userConversion: UsersConversion) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPost(username: String, postConversion: PostsConversion) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComments(username: String, commentsConversion: CommentsConversion) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun userExists(username: String, userConversion: UsersConversion) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun postExists(id: String, postConversion: PostsConversion) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAllUsers(usersRetrieved: UsersRetrieved) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAllPosts(postsRetrieved: PostsRetrieved) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAllComments(commentsRetrieved: CommentsRetrieved) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAllChatMessages(chatsRetrieved: ChatMessagesRetrieved) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAllChatSession(chatsRetrieved: ChatSessionRetrieved) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listImagesForUsers(
        users: ArrayList<Users>,
        referencesRetrieved: ReferencesRetrieved
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

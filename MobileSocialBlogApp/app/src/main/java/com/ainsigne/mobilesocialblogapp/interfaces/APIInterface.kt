package com.ainsigne.mobilesocialblogapp.interfaces

import com.ainsigne.mobilesocialblogapp.manager.*
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.CommentsConversion
import com.ainsigne.mobilesocialblogapp.utils.PostsConversion
import com.ainsigne.mobilesocialblogapp.utils.UsersConversion
import java.io.File


/// the template used in implementing the api
interface APIInterface {

    fun deleteToken(completion: (Error?, String) -> Unit)

    fun updateToken(username : String, token: String, completion: (Error?, String) -> Unit)

    fun updateById(id : String, endpoint : String, keyval : HashMap<String,Any>)

    fun addUser(keyval: Map<String,Any?>, completion: (Error?,String) -> Unit)

    fun addComments(keyval: Map<String,Any>, completion: (Error?,String) -> Unit)

    fun addPosts(keyval: Map<String,Any>, completion: (Error?,String) -> Unit)

    fun addChats(keyval: Map<String,Any>, completion: (Error?,String) -> Unit)

    fun addSession(keyval: Map<String,Any>, completion: (Error?,String) -> Unit)

    fun getUser(username : String, userConversion : UsersConversion)

    fun getPost(username : String, postConversion : PostsConversion)

    fun getComments(username : String, commentsConversion : CommentsConversion)

    fun userExists(username : String, userConversion : UsersConversion)

    fun postExists(id : String, postConversion : PostsConversion)

    fun retrieveAllTokens(completion: (Error?, String) -> Unit)

    fun retrieveAllUsers(usersRetrieved : UsersRetrieved)

    fun retrieveAllPosts(postsRetrieved : PostsRetrieved)

    fun retrieveAllComments(commentsRetrieved : CommentsRetrieved)

    fun retrieveAllChatMessages(chatsRetrieved : ChatMessagesRetrieved)

    fun retrieveAllChatSession(chatsRetrieved : ChatSessionRetrieved)

    fun uploadImage(file : File, imageName : String,completion: (Error?,String) -> Unit)

    fun downloadImage(imageName: String, completion: (Error?,String) -> Unit)

    fun listImagesForUsers(users : ArrayList<Users>, referencesRetrieved : ReferencesRetrieved)

    fun listImagesForPosts(posts : ArrayList<Posts>, referencesRetrieved : ReferencesRetrieved)
}
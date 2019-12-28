package com.ainsigne.mobilesocialblogapp.manager

import com.ainsigne.mobilesocialblogapp.models.*
import com.ainsigne.mobilesocialblogapp.utils.CommentsConversion
import com.ainsigne.mobilesocialblogapp.utils.PostsConversion
import com.ainsigne.mobilesocialblogapp.utils.UsersConversion
import java.io.File

/**
 * Implements the api manager abstract class (which derives from the apiprotocol) and  mock
 * implementation for fetching mock data
 **/
class MockAPIManager : APIManager() {


    override fun updateById(id: String, endpoint: String, keyval: HashMap<String, Any>) {

    }

    override fun addUser(keyval: Map<String, Any?>, completion: (Error?, String) -> Unit) {
        completion(null,"success")
    }

    override fun addComments(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
        completion(null,"success")
    }

    override fun addPosts(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
        completion(null,"success")
    }

    override fun addChats(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
        completion(null,"success")
    }

    override fun addSession(keyval: Map<String, Any>, completion: (Error?, String) -> Unit) {
        completion(null,"success")
    }

    override fun uploadImage(file: File, imageName: String, completion: (Error?, String) -> Unit) {
    }

    override fun downloadImage(imageName: String, completion: (Error?, String) -> Unit) {
    }

    override fun getComments(username: String, commentsConversion: CommentsConversion) {

    }

    override fun getPost(username: String, postConversion: PostsConversion) {

    }

    override fun getUser(username: String, userConversion: UsersConversion) {

    }

    override fun listImagesForPosts(
        posts: ArrayList<Posts>,
        referencesRetrieved: ReferencesRetrieved
    ) {
        referencesRetrieved.didRetrievePostImages(posts)
    }

    override fun listImagesForUsers(
        users: ArrayList<Users>,
        referencesRetrieved: ReferencesRetrieved
    ) {
        referencesRetrieved.didRetrieveUserImages(users)
    }

    override fun postExists(id: String, postConversion: PostsConversion) {

    }

    override fun retrieveAllChatMessages(chatsRetrieved: ChatMessagesRetrieved) {
        chatsRetrieved.retrievedChatMessages(ChatMessages.chatMessages(),"success")

    }

    override fun retrieveAllChatSession(chatsRetrieved: ChatSessionRetrieved) {
        chatsRetrieved.retrievedChatSession(ChatSession.chatSessions(),"success")
    }

    override fun retrieveAllComments(commentsRetrieved: CommentsRetrieved) {
        commentsRetrieved.retrievedComments(Comments.comments(),"success")
    }

    override fun retrieveAllPosts(postsRetrieved: PostsRetrieved) {
        postsRetrieved.retrievedPosts(Posts.posts(),"success")
    }

    override fun retrieveAllUsers(usersRetrieved: UsersRetrieved) {
        usersRetrieved.retrievedUsers(Users.users(),"success")
    }

    override fun userExists(username: String, userConversion: UsersConversion) {

    }


}

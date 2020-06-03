package com.ainsigne.mobilesocialblogapp.services

import com.ainsigne.mobilesocialblogapp.models.BodyRequest
import com.ainsigne.mobilesocialblogapp.models.BodyResponse
import retrofit2.Response
import retrofit2.http.*

interface FCMService {
    /**
     * gets the response to retrieve the venue details from id as String
     * @param id as [String] the id for the details to retrieve
     * @return Response<VenueExploreData>
     */
    @Headers("Authorization: key=AAAAS_fP4wA:APA91bEzz0kevkTeDdZz0M36hASPorZYSb9JDYR8k6D42ZjYdQcuAcuyj4sC2RJm7rq4j4TlW9Gg3ASi9Nx7MgzAP8QYBB65-PPXcF1wUO5TstennP1h2yijPX6Qab8pfRCCCdWCW6XF",
            "Content-Type: application/json")
    @POST("send")
    suspend fun sendMessage(@Body body : BodyRequest) : Response<BodyResponse>
}
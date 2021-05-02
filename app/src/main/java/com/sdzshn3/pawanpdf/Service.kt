package com.sdzshn3.pawanpdf

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface Service {

    @Streaming
    @POST("downloadResult")
    suspend fun downloadPdf(
        @Body request: Request
    ): Response<ResponseBody>
}
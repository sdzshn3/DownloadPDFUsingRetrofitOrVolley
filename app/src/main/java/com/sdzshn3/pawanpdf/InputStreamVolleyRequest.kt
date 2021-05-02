package com.sdzshn3.pawanpdf

import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser


internal class InputStreamVolleyRequest(
    method: Int, mUrl: String?, listener: Response.Listener<ByteArray>,
    errorListener: Response.ErrorListener?, params: HashMap<String, String>
) : Request<ByteArray?>(method, mUrl, errorListener) {
    private val mListener: Response.Listener<ByteArray>
    private val mParams: Map<String, String>

    //create a static map for directly accessing headers
    private var responseHeaders: Map<String, String>? = null

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return mParams
    }

    override fun deliverResponse(response: ByteArray?) {
        mListener.onResponse(response)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<ByteArray?> {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response))
    }

    init {
        // this request would never use cache.
        setShouldCache(false)
        mListener = listener
        mParams = params
    }
}
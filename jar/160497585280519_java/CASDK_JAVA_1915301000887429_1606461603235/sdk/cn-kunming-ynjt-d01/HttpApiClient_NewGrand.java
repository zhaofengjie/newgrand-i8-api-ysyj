//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.alibaba.cloudapi.client;
import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.WebSocketApiType;
import com.alibaba.fastjson.JSONObject;


public class HttpApiClientNewGrand extends ApacheHttpClient{
    public final static String HOST = "fce7c77966e045e697a17a194a374035.apigateway.res.ynjstzkg.com";
    static HttpApiClientNewGrand instance = new HttpApiClientNewGrand();
    public static HttpApiClientNewGrand getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }




    public void MDM_TO_NEWGRAND_XMDA(byte[] body , ApiCallback callback) {
        String path = "/secdev/mdmApi/projectInfo/";
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY , path, body);
        


        sendAsyncRequest(request , callback);
    }

    public ApiResponse MDM_TO_NEWGRAND_XMDASyncMode(byte[] body) {
        String path = "/secdev/mdmApi/projectInfo/";
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY , path, body);
        


        return sendSyncRequest(request);
    }
    public void MDM_TO_NEWGRAND_KSDA(byte[] body , ApiCallback callback) {
        String path = "/secdev/mdmApi/enterpriseInfo";
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY , path, body);
        


        sendAsyncRequest(request , callback);
    }

    public ApiResponse MDM_TO_NEWGRAND_KSDASyncMode(byte[] body) {
        String path = "/secdev/mdmApi/enterpriseInfo";
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY , path, body);
        


        return sendSyncRequest(request);
    }

}
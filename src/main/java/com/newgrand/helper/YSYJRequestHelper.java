package com.newgrand.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.domain.BackMsgModel;
import com.newgrand.domain.ResultModel;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class YSYJRequestHelper {


    /**
     * 请求地址
     */
    @Value("${ysyj.postUrl}")
    private String postUrl;

    /**
     * 请求参数postDatUrl
     */
    @Value("${ysyj.postDataUrl}")
    private String postDataUrl;

    /**
     * 请求参数namespace
     */
    @Value("${ysyj.namespace}")
    private String namespace;

    /**
     * 请求参数serverCode
     */
    @Value("${ysyj.serverCode}")
    private String serverCode;

    /**
     * 请求参数interfaceCode
     */
    @Value("${ysyj.interfaceCode}")
    private String interfaceCode;

    /**
     * 请求参数tokenFuncUrl
     */
    @Value("${ysyj.tokenFuncUrl}")
    private String tokenFuncUrl;


    /**
     * 分包申请信息推送请求
     * @param param    请求报文
     * @return
     * @throws IOException
     */
    public ResultModel SendPost(String param) throws IOException {
        ResultModel backMsg = new ResultModel();
        try {
            //获取铝模系统access_token
            BackMsgModel tokenBack = Token();
            if(tokenBack.getResult() == "false"){
                backMsg.setResult("false");
                backMsg.setMessage("获取数据失败,原因:" + tokenBack.getMessage());
                backMsg.setCode("0");
                return backMsg;
            }
            //获取token成功则返回token值
            String token = tokenBack.getData().toString();

            //请求地址 = "服务器ip" + "对应接口路由地址"
            StringBuilder URL = new StringBuilder();
            URL.append(postUrl).append(postDataUrl).append("?");
            URL.append("namespace=").append(namespace).append("&");
            URL.append("serverCode=").append(serverCode).append("&");
            URL.append("interfaceCode=").append(interfaceCode).append("&");
            URL.append("access_token=").append(token);

            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(URL.toString());
            httpPost.addHeader("Content-Type","application/json");
            //设置参数到请求对象中
            StringEntity s = new StringEntity(param, "utf-8");
            //httpPost.setEntity(new StringEntity(param, "utf-8"));
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(s);

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体（返回报文）
            HttpEntity entity2 = response.getEntity();
            if (entity2 != null) {
                //转换报文对象
                String body = EntityUtils.toString(entity2);
                //log.info("接收反馈的报文 = " + body);
                JSONObject strObj = JSON.parseObject(body);
                //if (strObj.getString("failed") == "false" && strObj.getString("code").equals("sucess")) {
                if(strObj.getString("statusCode") == "OK"){
                    backMsg.setCode("1");
                    backMsg.setMessage("推送数据成功");
                    backMsg.setResult("true");
                } else {
                    backMsg.setCode("0");
                    backMsg.setMessage("推送数据失败,原因:"+ strObj.getString("message"));
                    backMsg.setResult("false");
                }
            }
            EntityUtils.consume(entity2);
            //释放链接
            response.close();
        } catch (Exception ex) {
            backMsg.setCode("0");
            backMsg.setMessage("推送数据失败，原因=" + ex.getMessage());
            backMsg.setResult("false");
        }
        return backMsg;
    }




    /**
     * 获取云上营家系统token
     *
     * @return
     * @throws IOException
     */
    public BackMsgModel Token() throws IOException {
        String body = "";
        String token = "";
        BackMsgModel back = new BackMsgModel();
        try {
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            StringBuilder surl = new StringBuilder();
            surl.append(postUrl).append(tokenFuncUrl);//请求地址
            HttpPost httpPost = new HttpPost(surl.toString());
            //httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");

            //添加请求头
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
            urlParameters.add(new BasicNameValuePair("client_id", "xg-client"));
            urlParameters.add(new BasicNameValuePair("client_secret", "xg-client"));

            HttpEntity entity = new UrlEncodedFormEntity(urlParameters,"utf-8");
            httpPost.setEntity(entity);

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体
            HttpEntity entity2 = response.getEntity();
            if (entity2 != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity2);
                JSONObject str = JSON.parseObject(body);
                token = str.getString("access_token");
                back.setResult("true");
                back.setMessage("获取token数据成功");
                back.setData(token);
            }
            EntityUtils.consume(entity2);
            //释放链接
            response.close();
        } catch (Exception ex) {
            back.setResult("false");
            back.setMessage("铝模系统token获取异常，原因 = " + ex.getMessage());
            back.setData(null);
        }
        return back;
    }
}

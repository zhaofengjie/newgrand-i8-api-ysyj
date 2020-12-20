package com.newgrand.secdev.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.newgrand.secdev.domain.BackMsgModel;
import com.newgrand.secdev.domain.LM.ResultModel;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
@Slf4j
@Component
public class LMRequestHelper {
    /**
     * 铝模接口地址
     */
    @Value("${lm.urlLm}")
    private String urlLm;
    /**
     * 请求参数
     */
    @Value("${lm.authorZion}")
    private String authorZion;
    /**
     * 请求格式
     */
    @Value("${lm.contentType}")
    private String contentType;
    /**
     * 请求缓存（本地缓存）
     */
    @Value("${lm.cacheControl}")
    private String cacheControl;
    /**
     * 请求账号
     */
    @Value("${lm.username}")
    private String username;
    /**
     * 请求账号密码
     */
    @Value("${lm.password}")
    private String password;
    /**
     * 请求参数scope
     */
    @Value("${lm.scope}")
    private String scope;
    /**
     * 请求参数grantType
     */
    @Value("${lm.grantType}")
    private String grantType;
    /**
     * 铝模系统获取token的方法名
     */
    @Value("${lm.tokenFunUrl}")
    private String tokenFunUrl;
    /**
     * 物流系统url
     */
    @Value("${wl.urlWL}")
    private String urlWL;
    /**
     * 物流系统token方法名
     */
    @Value("${wl.wlTokenURL}")
    private String wlTokenURL;
    /**
     * 物流apiKey
     */
    @Value("${wl.wlApiKey}")
    private String wlApiKey;
    /**
     * 获取物料信息url
     */
    @Value("${wl.wlInfoUrl}")
    private String wlInfoUrl;
    /**
     * 获取物流信息系统链接url
     */
    @Value("${wl.wlSSOUrl}")
    private String wlSSOUrl;


    /**
     * 周材租入信息推送请求
     *
     * @param param    请求报文
     * @param funcName 请求方法
     * @return
     * @throws IOException
     */
    public ResultModel SendPost(String param, String funcName) throws IOException {
        return SendPost(urlLm, param, funcName);
    }

    /**
     * 周材租入信息推送请求
     *
     * @param url      请求地址或域名
     * @param param    请求报文
     * @param funcName 请求方法
     * @return
     * @throws IOException
     */
    public ResultModel SendPost(String url, String param, String funcName) throws IOException {
        ResultModel backMsg = new ResultModel();
        log.info("推送数据的的报文 = " + param);
        try {
            //获取铝模系统access_token
            String token = Token();
            if (token == "" || token == null) {
                backMsg.setCode("0");
                backMsg.setMessage("推送数据失败,原因 = 获取token失败");
                backMsg.setResult("false");
            }

            //请求地址 = "服务器ip" + "对应接口路由地址"
            String URL = url + funcName;
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(URL);

            //设置参数到请求对象中
            StringEntity s = new StringEntity(param, "utf-8");
            //httpPost.setEntity(new StringEntity(param, "utf-8"));
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(s);

            //Authorization 值拼接 (特别注意：bearer 后面有 空格)
            String authorization = "bearer " + token;

            //设置header信息
            httpPost.addHeader("Cache-Control", cacheControl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", authorization);
            httpPost.addHeader("charset", "UTF-8");
            httpPost.addHeader("Accept", "application/json");

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体（返回报文）
            HttpEntity entity2 = response.getEntity();
            if (entity2 != null) {
                //转换报文对象
                String body = EntityUtils.toString(entity2);
                log.info("接收反馈的报文 = " + body);
                JSONObject strObj = JSON.parseObject(body);
                if (strObj.getString("result") == "true" && strObj.getString("code").equals("1")) {
                    backMsg.setCode("1");
                    backMsg.setMessage("推送数据成功");
                    backMsg.setResult("true");
                } else {
                    backMsg.setCode("0");
                    backMsg.setMessage("推送数据失败,原因:"+ strObj.getString("msg"));
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
            log.error("推送数据失败，原因 = " + ex.getMessage());
        }
        return backMsg;
    }

    /**
     * 获取铝模系统token
     *
     * @return
     * @throws IOException
     */
    public String Token() throws IOException {

        String body = "";
        String token = "";
        try {
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            StringBuilder surl = new StringBuilder();
            surl.append(urlLm).append(tokenFunUrl).append("?");//请求地址
            surl.append("grant_type=").append(grantType).append("&");
            surl.append("scope=").append(scope).append("&");
            surl.append("username=").append(username).append("&");
            surl.append("password=").append(password);
            HttpPost httpPost = new HttpPost(surl.toString());

            //添加请求头
            httpPost.addHeader("Cache-Control", "no-cache");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;Charset=UTF-8");
            httpPost.addHeader("Authorization", "Basic emhpcnVpOnpoaXJ1aTg4OA==");

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体
            HttpEntity entity2 = response.getEntity();
            if (entity2 != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity2);
                JSONObject str = JSON.parseObject(body);
                token = str.getString("access_token");
            }
            EntityUtils.consume(entity2);
            //释放链接
            response.close();
        } catch (Exception ex) {
            log.error("铝模系统token获取异常，原因 = " + ex.getMessage());
        }
        return token;
    }

    /**
     * 获取发货信息请求
     *
     * @param param    请求报文
     * @param funcName 请求方法
     * @return
     * @throws IOException
     */
    public BackMsgModel GetData(String param, String funcName) throws IOException {
        return GetData(urlLm, param, funcName);
    }

    /**
     * 获取发货信息请求
     *
     * @param url      请求地址或域名
     * @param param    请求报文
     * @param funcName 请求方法
     * @return
     * @throws IOException
     */
    public BackMsgModel GetData(String url, String param, String funcName) throws IOException {
        String body = "";
        BackMsgModel back = new BackMsgModel();
        try {
            String token = Token();
            funcName = funcName.trim();
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            StringBuilder surl = new StringBuilder();
            surl.append(urlLm).append(funcName).append("?");//请求地址
            surl.append(param);

            HttpGet httpGet = new HttpGet(surl.toString());

            //添加请求头
            httpGet.addHeader("Cache-Control", "no-cache");
            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded;Charset=UTF-8");
            httpGet.addHeader("Authorization", "bearer " + token);

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpGet);
            //获取结果实体
            HttpEntity entity2 = response.getEntity();
            if (entity2 != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity2);
                JSONObject bodyObj = JSON.parseObject(body);
                String code = bodyObj.getString("code");
                if (code.equals("200")) {
                    back.setResult("true");
                    back.setMessage("获取数据成功");
                    back.setData(body);
                } else {
                    back.setResult("false");
                    back.setMessage("获取数据失败,原因:" + bodyObj.getString("message"));
                    back.setData(body);
                }
            }
            EntityUtils.consume(entity2);
            //释放链接
            response.close();
        } catch (Exception ex) {
            back.setResult("false");
            back.setMessage("获取数据失败,原因:" + ex.getMessage());
            back.setData(null);
        }
        return back;
    }

    /**
     * 获取物流追踪信息token
     *
     * @return
     */
    public BackMsgModel GetWLToken() {
        String token = "";
        BackMsgModel back = new BackMsgModel();
        try {
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            StringBuilder surl = new StringBuilder();
            surl.append(urlWL).append(wlTokenURL).append("?");//请求地址
            surl.append("apikey=").append(wlApiKey);
            HttpGet httpGet = new HttpGet(surl.toString());
            //发送请求
            CloseableHttpResponse response = client.execute(httpGet);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                String body = EntityUtils.toString(entity);
                JSONObject str = JSON.parseObject(body);
                String flag = str.getString("flag");
                if (flag == "true") {
                    token = str.getString("token");
                    back.setResult("true");
                    back.setMessage("获取物流系统token成功");
                    back.setData(token);
                } else {
                    back.setResult("false");
                    back.setMessage("获取物流系统token失败，原因 : {" + str.getString("msg") + "}");
                    back.setData(null);
                }
            }
            EntityUtils.consume(entity);
            //释放链接
            response.close();
        } catch (Exception ex) {
            back.setResult("false");
            back.setMessage("获取物流系统token失败，原因 : {" + ex.getMessage() + "}");
            back.setData("");
        }
        return back;
    }

    /**
     * 根据发货订单ID，取物料系统物流信息
     * @param orderGID
     * @return
     */

    public BackMsgModel GetWLInfo(String orderGID,String token) {
        BackMsgModel back = new BackMsgModel();
        try {
            //创建httpclient对象
            CloseableHttpClient client = HttpClients.createDefault();
            //创建post方式请求对象
            StringBuilder surl = new StringBuilder();
            surl.append(urlWL).append(wlInfoUrl);//请求地址
            HttpPost httpPost = new HttpPost(surl.toString());
            //获取请求报文
            String params = GetWLBody(orderGID, token);

            //设置参数到请求对象中
            StringEntity s = new StringEntity(params, "UTF-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(s);

            httpPost.addHeader("Content-Type", "application/json");

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体（返回报文）
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //转换报文对象
                String body = EntityUtils.toString(entity);
                log.info("接收反馈的报文 = " + body);
                JSONObject strObj = JSON.parseObject(body);
                //String resultCode = strObj.get("resultCode").toString();
                String flag = strObj.get("flag").toString();
                if (flag == "true") {
                    JSONObject data = strObj.getJSONObject("data");
                    JSONArray list = data.getJSONArray("list");
                    JSONObject item = list.getJSONObject(0);
                    //获取ORDER_GID
                    String ORDER_GID = item.getString("ORDER_GID");
                    back.setData(ORDER_GID);
                    back.setMessage("获取数据成功");
                    back.setResult("true");
                } else {
                    back.setData("");
                    back.setMessage("获取数据失败,原因 :" + strObj.getString("msg"));
                    back.setResult("false");
                }
            }
            EntityUtils.consume(entity);
            //释放链接
            response.close();
        } catch (Exception ex) {
            back.setData("");
            back.setMessage("获取数据失败,原因 :" + ex.getMessage());
            back.setResult("false");
        }
        return back;
    }

    public BackMsgModel GetWLUrl(String orderGID) {
        BackMsgModel back = GetWLToken();

        /** 如果获取token失败，则直接返回失败结果 */
        if (back.getResult() == "false") {
            return back;
        }
        /** 得到token */
        String token = back.getData().toString();
        /** 获取物流信息 */

        BackMsgModel result = GetWLToken();
        result = GetWLInfo(orderGID, token);
        if (result.getResult() == "false") {
            return result;
        }
        /** 得到结果集中 ORDER_GID */
        String ORDER_GID = result.getData().toString();

        /** 拼接链接地址 */
        StringBuilder url = new StringBuilder();
        url.append(urlWL).append(wlSSOUrl).append("?");//请求地址
        url.append("orderGID=").append(orderGID);
        url.append("&token=").append(token);

        result.setData(url.toString());
        result.setMessage("获取链接成功");
        result.setResult("true");
        return result;
    }

    public String  GetWLBody(String orderGID ,String token) {
        /** 截取字符串 . 后面数字*/
        String orderID = orderGID.substring(orderGID.lastIndexOf(".") + 1);

        JSONObject item = new JSONObject();
        item.put("Field", "ORDER_ID");
        item.put("Method", "Equal");
        item.put("Value", orderID);

        JSONArray itemArr = new JSONArray();
        itemArr.add(item);

        JSONObject queryModel = new JSONObject();
        queryModel.put("Items", itemArr);

        JSONObject pageInfo = new JSONObject();
        pageInfo.put("CurrentPage", "1");
        pageInfo.put("SkipCount", "0");
        pageInfo.put("PageSize", "10");
        pageInfo.put("SortField", "CREATED_DATE");
        pageInfo.put("SortDirection", "DESC");
        pageInfo.put("IsPaging", true);
        pageInfo.put("IsGetTotalCount", true);
        pageInfo.put("TotalCount", "0");

        JSONObject search = new JSONObject();
        search.put("PageInfo", pageInfo);
        search.put("QueryModel", queryModel);

        JSONObject body = new JSONObject();
        body.put("Search", search);
        body.put("token", token);
        return JSONObject.toJSONString(body);
    }
}

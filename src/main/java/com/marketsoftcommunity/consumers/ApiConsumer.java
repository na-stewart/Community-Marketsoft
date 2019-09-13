package main.java.com.marketsoftcommunity.consumers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ApiConsumer<T> {

    private Gson gson = new Gson();
    private String apiUrl = "http://localhost:8080/";
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public int post(String mapping) throws Exception {
       return post(new ArrayList<>(), mapping);
    }

    public int post(List<BasicNameValuePair> pairs, String mapping) throws Exception {
        HttpPost post = new HttpPost(apiUrl + mapping);
        if (!pairs.isEmpty())
             post.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse response = httpClient.execute(post);
        return response.getStatusLine().getStatusCode();
    }



    public int put(List<BasicNameValuePair> pairs, String mapping) throws IOException{
        HttpPut put = new HttpPut(apiUrl + mapping);
        put.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse response = httpClient.execute(put);
        return response.getStatusLine().getStatusCode();
    }



    public T get(String mapping, Class<T> objClass) throws IOException{
        return gson.fromJson(get(mapping), objClass);
    }

    public String get(String mapping) throws IOException {
        HttpGet getRequest = new HttpGet(apiUrl + mapping);
        HttpResponse response = httpClient.execute(getRequest);
        HttpEntity httpEntity = response.getEntity();
        return EntityUtils.toString(httpEntity);

    }

    public List<T> getAll(String mapping, Class<T> objClass) throws IOException {
        List<T> lst = new ArrayList<T>();
        HttpGet getRequest = new HttpGet(apiUrl + mapping);
        HttpResponse response = httpClient.execute(getRequest);
        HttpEntity httpEntity = response.getEntity();
        String apiOutput = EntityUtils.toString(httpEntity);
        if (response.getStatusLine().getStatusCode() == 200)
            lst = gson.fromJson(apiOutput, TypeToken.getParameterized(ArrayList.class, objClass).getType());
        return lst;

    }


    public void delete(String mapping) throws IOException {
        HttpDelete delete = new HttpDelete(apiUrl + mapping);
        HttpResponse response = httpClient.execute(delete);
        System.out.println(response.getStatusLine().getStatusCode());
    }



}

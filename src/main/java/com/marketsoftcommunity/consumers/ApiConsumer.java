package main.java.com.marketsoftcommunity.consumers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ApiConsumer<T> {
//TODO try with resources

    private Gson gson = new Gson();
    private String apiUrl = "https://api.communitymarketsoft.com/";
    private CloseableHttpClient httpClient = HttpClients.createDefault();


    public void post(String mapping) throws IOException, ApiException {
        post(new ArrayList<>(), mapping);
    }
    public void post(BasicNameValuePair basicNameValuePair, String mapping) throws IOException, ApiException {
        post(Collections.singletonList(basicNameValuePair), mapping);
    }

    public void post(List<BasicNameValuePair> pairs, String mapping) throws IOException, ApiException {

        HttpPost post = new HttpPost(apiUrl + mapping);
        if (!pairs.isEmpty())
             post.setEntity(new UrlEncodedFormEntity(pairs));
        try(  CloseableHttpResponse response = httpClient.execute(post)){
            if (response.getStatusLine().getStatusCode() != 200)
                throw new ApiException(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        }
    }


    public void put(BasicNameValuePair basicNameValuePair, String mapping) throws IOException, ApiException {
        put(Collections.singletonList(basicNameValuePair), mapping);
    }

    public void put(List<BasicNameValuePair> pairs, String mapping) throws IOException, ApiException {
        HttpPut put = new HttpPut(apiUrl + mapping);
        if (!pairs.isEmpty())
            put.setEntity(new UrlEncodedFormEntity(pairs));

        try( CloseableHttpResponse response = httpClient.execute(put)) {
            if (response.getStatusLine().getStatusCode() != 200)
                throw new ApiException(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        }
    }


    public T get(String mapping, Class<T> objClass) throws IOException, ApiException {
        return gson.fromJson(get(mapping), objClass);
    }

    public String get(String mapping) throws IOException, ApiException {
        HttpGet getRequest = new HttpGet(apiUrl + mapping);
        HttpEntity httpEntity;
        try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
            httpEntity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200)
                throw new ApiException(response.getStatusLine().getStatusCode(), EntityUtils.toString(httpEntity));
            return EntityUtils.toString(httpEntity);
        }

    }

    public List<T> getAll(String mapping, Class<T> objClass) throws IOException, ApiException {
        List<T> lst;
        HttpGet getRequest = new HttpGet(apiUrl + mapping);
        HttpEntity httpEntity;
        try(CloseableHttpResponse response = httpClient.execute(getRequest)){
            httpEntity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200)
                throw new ApiException(response.getStatusLine().getStatusCode(),  EntityUtils.toString(httpEntity));
            lst = gson.fromJson( EntityUtils.toString(httpEntity), TypeToken.getParameterized(ArrayList.class, objClass).getType());
            return lst;
        }


    }


    public void delete(String mapping) throws IOException, ApiException {
        HttpDelete delete = new HttpDelete(apiUrl + mapping);
        try(CloseableHttpResponse response = httpClient.execute(delete)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new ApiException(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
            }
        }
    }



}

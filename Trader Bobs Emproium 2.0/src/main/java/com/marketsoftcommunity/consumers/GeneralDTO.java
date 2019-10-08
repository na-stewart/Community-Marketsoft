package main.java.com.marketsoftcommunity.consumers;



import com.google.gson.Gson;

import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */


//Use string entities list entities.
    //https://www.javaguides.net/2018/10/apache-httpclient-get-post-put-and-delete-methods-example.html.
public class GeneralDTO<T> {
    private String model;
    private Class<T> objClass;
    private ApiConsumer<T> consumer = new ApiConsumer<T>();

    public GeneralDTO(String model, Class<T> objClass) {
        this.model = model;
        this.objClass = objClass;
    }

    public T get(int id) throws IOException, ApiException {
        return consumer.get(model + "?id=" + id, objClass);
    }

    public List<T> getAll() throws IOException, ApiException {
        return consumer.getAll(model + "/all", objClass);
    }

    public List<T> getAll(String whereClause, String param) throws IOException, ApiException {
        String where = replace(whereClause);
        return consumer.getAll(model + "/all/where?where=" + where + "&param=" + param, objClass);
    }

    public List<T> getAll(String whereClause, String[] params) throws IOException, ApiException {
        String where = replace(whereClause);
        return consumer.getAll(model +"/all/whereparams?whereClause=" + where +  "&params=" +
                new Gson().toJson(params), objClass);
    }

    public void delete(int id) throws IOException, ApiException {
        consumer.delete(model + "?id=" + id);
    }

    public void update(T updated) throws IOException, ApiException {
        consumer.put(new BasicNameValuePair(model, new Gson().toJson(updated)), model);
    }

    public void add(T model) throws IOException, ApiException {
        consumer.post(new BasicNameValuePair(this.model, new Gson().toJson(model)), this.model);
    }

    public String replace(String str) {
        String[] words = str.split(" ");
        StringBuilder sentence = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; ++i) {
            sentence.append("%20");
            sentence.append(words[i]);
        }

        return sentence.toString();
    }

    public String getModel() {
        return model;
    }
}

package main.java.com.marketsoftcommunity.model.exceptions;

import com.amazonaws.services.xray.model.Http;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ApiException extends Exception {
    private HttpResponse httpResponse;


    public ApiException(int errorCode, String errorMessage)  {
        super(errorCode + "\n" + errorMessage);
    }
}

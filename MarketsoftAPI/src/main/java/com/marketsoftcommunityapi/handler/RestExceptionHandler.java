package main.java.com.marketsoftcommunityapi.handler;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@Controller
public class RestExceptionHandler extends AbstractErrorController {


        public RestExceptionHandler(ErrorAttributes errorAttributes) {
            super(errorAttributes);
        }

        @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public Map<String, Object> handleError(HttpServletRequest request) {
            Map<String, Object> errorAttributes = super.getErrorAttributes(request, true);
            return errorAttributes;
        }

        @Override
        public String getErrorPath() {
            return "/error";
        }
}

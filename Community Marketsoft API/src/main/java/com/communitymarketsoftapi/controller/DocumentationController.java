package com.communitymarketsoftapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@Controller
public class DocumentationController {

    @RequestMapping(value = "/")
    public String index(){
        return "index.html";

    }
}

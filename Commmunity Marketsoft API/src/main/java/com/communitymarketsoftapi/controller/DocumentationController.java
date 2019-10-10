package com.communitymarketsoftapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@Controller
public class DocumentationController {

    @RequestMapping("/")
    public String index(){
        return "index.html";

    }
}

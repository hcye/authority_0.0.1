package com.rbac.demo.controller.layer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class layerController {
    @GetMapping("/layer/{uri}")
    public String getPage(@PathVariable("uri") String pageUri){

        return "/layer/"+pageUri;
    }
}

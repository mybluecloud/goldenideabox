package com.triz.goldenideabox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PersonCenterController {

    @RequestMapping(value = "/person")
    public String person(Model model) {

        // model.addAttribute("list", lst);
        return "frontstage/blank";
    }
}

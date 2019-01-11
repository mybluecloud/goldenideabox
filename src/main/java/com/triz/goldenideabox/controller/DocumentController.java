package com.triz.goldenideabox.controller;

import com.triz.goldenideabox.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;



}

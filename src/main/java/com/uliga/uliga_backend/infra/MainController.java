package com.uliga.uliga_backend.infra;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String main() {
        return "index";
    }

    @GetMapping("/rest-docs")
    public String restDocs() {
        return "api_doc";
    }

}

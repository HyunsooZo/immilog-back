package com.backend.immilog.global.doc;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "API 문서", description = "API 문서")
@Controller
public class DocumentController {
    @GetMapping("/api-docs")
    public String getDocument() {
        return "api-docs";
    }
}

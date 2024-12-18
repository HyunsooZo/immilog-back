package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.services.UserSignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User View API", description = "사용자 뷰 관련 API")
@RequestMapping("/api/v1/users")
@Controller
public class UserViewController {
    private final UserSignUpService userSignUpService;

    public UserViewController(UserSignUpService userSignUpService) {
        this.userSignUpService = userSignUpService;
    }

    @GetMapping("/{userSeq}/verification")
    @Operation(summary = "사용자 이메일 인증", description = "사용자 이메일 인증 진행")
    public String verifyEmail(
            @PathVariable("userSeq") Long userSeq,
            Model model
    ) {
        final Pair<String, Boolean> result = userSignUpService.verifyEmail(userSeq);
        model.addAttribute("message", result.getFirst());
        model.addAttribute("isLoginAvailable", result.getSecond());
        return "verification-result";
    }
}

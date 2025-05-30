package com.david.auth_layer_architecture.presentation.controller;

import com.david.auth_layer_architecture.application.facade.interfaces.IUserFacade;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.infrestructure.utils.constants.routes.UserRoutes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;

@AllArgsConstructor
@RestController
@RequestMapping(path = CommonConstants.SECURE_URL, produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController {
    private final IUserFacade userFacade;

    @GetMapping("/user")
    public ResponseEntity<MessageResponse> home() {
        return ResponseEntity.ok(new MessageResponse("welcome!"));
    }

    @PostMapping(UserRoutes.SIGN_OUT_URL)
    public ResponseEntity<MessageResponse> signOut( HttpServletRequest request ) {
        String accessTokenId =(String) request.getAttribute("accessTokenId");

        return ResponseEntity.ok(this.userFacade.signOut(accessTokenId));
    }
}

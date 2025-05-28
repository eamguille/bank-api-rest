package com.udb.bankapirest.controller;

import com.udb.bankapirest.model.SigninResponse;
import com.udb.bankapirest.model.User;
import com.udb.bankapirest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(@RequestBody Map<String, String> payload){
        try{
            Optional<User> user = userRepository.findByEmail(payload.get("email"));
            if(user.isEmpty()){
                return  ResponseEntity.badRequest().body(new SigninResponse("contraseña o email invalidos",false));
            }
            System.out.println(user.get().getRole() + user.get().getRole() != "dependiente" + " - " + !user.get().getRole().equals("dependiente"));
            if (!user.get().getRole().equals("dependiente")){
                return  ResponseEntity.badRequest().body(new SigninResponse("contraseña o email invalidos - 403",false));
            }
            return ResponseEntity.ok(new SigninResponse("exito", true));
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(new SigninResponse("contraseña o email invalidos",false));
        }
    }
}

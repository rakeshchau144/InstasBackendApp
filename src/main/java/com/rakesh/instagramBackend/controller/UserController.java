package com.rakesh.instagramBackend.controller;

import com.rakesh.instagramBackend.model.*;
import com.rakesh.instagramBackend.model.dto.SignInInput;
import com.rakesh.instagramBackend.model.dto.SignUpOutput;
import com.rakesh.instagramBackend.service.AuthoService;
import com.rakesh.instagramBackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthoService authenticationService;

    @PostMapping("user/signup")
    public SignUpOutput signUpInstaUser(@RequestBody User user)
    {

        return userService.signUpUser(user);
    }

    @PostMapping("user/signIn")
    public String sigInInstaUser(@RequestBody @Valid SignInInput signInInput)
    {
        return userService.signInUser(signInInput);
    }

    @DeleteMapping("user/signOut")
    public String sigOutInstaUser(String email, String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.sigOutUser(email);
        }
        else {
            return "Sign out not allowed for non authenticated user.";
        }

    }

    @PostMapping("post")
    public String createInstaPost(@RequestBody Post post , @RequestParam String email, @RequestParam String token){
        if(authenticationService.authenticate(email,token)) {
            return userService.createInstaPost(post);
        }
        else{
            return "Not found user !!!";
        }
    }
    @DeleteMapping("post/delete")
    public String removeInstaPost(@RequestParam Integer postId , @RequestParam String email, @RequestParam String token){
        if(authenticationService.authenticate(email,token)) {
            return userService.removeInstaPost(postId,email);
        }
        else{
            return "Not found user !!!";
        }
    }

    @PostMapping("post/comment")
    public String addComment(@RequestBody Comment comment ,@RequestParam String commenterEmail, @RequestParam String token){
        return userService.addComment(comment,commenterEmail);
    }


    @DeleteMapping("post/comment/delete")
    public String removeInstaCommment(@RequestParam Integer commentId, @RequestParam String email, @RequestParam String token){
        if(authenticationService.authenticate(email,token)) {
            return userService.removeInstaCommment(commentId,email);
        }
        else{
            return "Not found user !!!";
        }
    }
    @PostMapping("post/like")
    public String addLike(@RequestParam Like like, @RequestParam String likerEmail, @RequestParam String likerToke){
        if(authenticationService.authenticate(likerEmail,likerToke)) {
            return userService.addInstaLike(like,likerEmail);
        }
        return "Not Provide access !!";
    }
    @GetMapping("post/getAll/like")
    public String getLikes(@RequestParam Integer postId, @RequestParam String userEmail, @RequestParam String userToke){
        if(authenticationService.authenticate(userEmail,userToke)) {
            return userService.getLikes(postId,userEmail);
        }
        return "Not Provide access !!";
    }
    @DeleteMapping("post/like/delete")
    public String removeInstaPostLike(@RequestParam Integer likeId, @RequestParam String email, @RequestParam String token){
        if(authenticationService.authenticate(email,token)) {
            return userService.removeInstaPostLike(likeId,email);
        }
        else{
            return "Not found user !!!";
        }
    }

    @PostMapping("follow")
    public String followUser(@RequestParam Follow follow, @RequestParam String followerEmail, @RequestParam String followerToke){
        if(authenticationService.authenticate(followerEmail,followerToke)) {
            return userService.followUser(follow,followerEmail);
        }
        return "Not Provide access !!";
    }
    @DeleteMapping("unFollow/{followingId}")
    public String unFollowUser(@RequestParam Integer followingId , @RequestParam String followerEmail, @RequestParam String followerToke){
        if(authenticationService.authenticate(followerEmail,followerToke)) {
            return userService.unFollowUser(followingId,followerEmail);
        }
        return "Not Provide access !!";
    }
}

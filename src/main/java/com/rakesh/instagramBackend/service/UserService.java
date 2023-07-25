package com.rakesh.instagramBackend.service;

import com.rakesh.instagramBackend.model.*;
import com.rakesh.instagramBackend.model.dto.SignInInput;
import com.rakesh.instagramBackend.model.dto.SignUpOutput;
import com.rakesh.instagramBackend.repository.IUserRepo;
import com.rakesh.instagramBackend.service.emailUtility.EmailHandler;
import com.rakesh.instagramBackend.service.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    IUserRepo userRepo;

    @Autowired
    AuthoService authenticationService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    public SignUpOutput signUpUser(User user) {

        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        String newEmail = user.getUserEmail();

        if(newEmail == null)
        {
            signUpStatusMessage = "Invalid email";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }

        //check if this user email already exists ??
        User existingUser = userRepo.findFirstByUserEmail(newEmail);

        if(existingUser != null)
        {
            signUpStatusMessage = "Email already registered!!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getUserPassword());

            //saveAppointment the user with the new encrypted password

            user.setUserPassword(encryptedPassword);
            userRepo.save(user);

            return new SignUpOutput(signUpStatus, "User registered successfully!!!");
        }
        catch(Exception e)
        {
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpStatusMessage);
        }
    }


    public String signInUser(SignInInput signInInput) {


        String signInStatusMessage = null;

        String signInEmail = signInInput.getEmail();

        if(signInEmail == null)
        {
            signInStatusMessage = "Invalid email";
            return signInStatusMessage;


        }

        //check if this user email already exists ??
        User existingUser = userRepo.findFirstByUserEmail(signInEmail);

        if(existingUser == null)
        {
            signInStatusMessage = "Email not registered!!!";
            return signInStatusMessage;

        }

        //match passwords :

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if(existingUser.getUserPassword().equals(encryptedPassword))
            {
                //session should be created since password matched and user id is valid
                AuthenticationToken authToken  = new AuthenticationToken(existingUser);
                authenticationService.saveAuthToken(authToken);

                EmailHandler.sendEmail("mainakgh1@gmail.com","email testing",authToken.getTokenValue());
                return "Token sent to your email";
            }
            else {
                signInStatusMessage = "Invalid credentials!!!";
                return signInStatusMessage;
            }
        }
        catch(Exception e)
        {
            signInStatusMessage = "Internal error occurred during sign in";
            return signInStatusMessage;
        }

    }


    public String sigOutUser(String email) {

        User user = userRepo.findFirstByUserEmail(email);
        AuthenticationToken token = authenticationService.findFirstByUser(user);
        authenticationService.removeToken(token);
        return "User Signed out successfully";
    }

    public String createInstaPost(Post post){

        return postService.createInstaPost(post);
    }

    public String removeInstaPost(Integer postId, String email) {
        User user = userRepo.findFirstByUserEmail(email);
        return postService.removeInstaPost(postId,user);
    }

    public String  addComment(Comment comment, String commenterEmail) {

        boolean validPost = postService.validPost(comment.getInstaPost());

        if(validPost){
            User commenterUser = userRepo.findFirstByUserEmail(commenterEmail);
            comment.setCommenter(commenterUser);
            return commentService.addComment(comment);
        }
        return "Invalid Post !!";

    }

    public String removeInstaCommment(Integer commentId, String email) {
        Comment comment = commentService.findComment(commentId);
        if(comment != null){
            if(authorizeCommentRemove(comment,email)){
                commentService.removeCommment(comment);
                return "Comment Deleted !!";

            }else {
                return "Unauthorized User !!";
            }

        }else{
            return "Unauthorized User !!";
        }
    }

    private boolean authorizeCommentRemove(Comment comment, String email) {
        String commentOwnerEmail = comment.getCommenter().getUserEmail();
        String postOwnerEmail = comment.getInstaPost().getPostOwner().getUserEmail();
        return commentOwnerEmail.equals(email) || postOwnerEmail.equals(email);
    }

    public String addInstaLike(Like like, String likerEmail) {
        Post instaPost = like.getInstaPost();
        boolean validPost = postService.validPost(instaPost);

        if(validPost){
            User liker = userRepo.findFirstByUserEmail(likerEmail);
            if(likeService.isLikeAllowedOnThisPost(instaPost,liker)) {
                like.setLiker(liker);
                return likeService.addLike(like);
            }
            else {
                return "Already liked";
            }
        }
        return "Not Liked something wrong !!";


    }

    public String getLikes(Integer postId, String userEmail) {
        Post validPost = postService.validPost(postId);
        if(validPost !=null){
            Integer likeCount = likeService.getLikeCountForPost(validPost);
            return String.valueOf(likeCount);
        }else{
            return "Post invalid !!";
        }

    }

    public String removeInstaPostLike(Integer likeId, String email) {
        Like like = likeService.findLike(likeId);
        if(like != null){
            if(authorizeLikeRemove(like,email)){
                likeService.removeLike(like);
                return "Like remove !!";

            }else {
                return "Unliked User !!";
            }

        }else{
            return "Unauthorized User !!";
        }
    }

    private boolean authorizeLikeRemove(Like like, String email) {
        String likeOwnerEmail = like.getLiker().getUserEmail();
        return likeOwnerEmail.equals(email);
    }

    public String followUser(Follow follow, String followerEmail) {
        User followerTargetUser = userRepo.findById(follow.getCurrentUser().getUserId()).orElse(null);

        User follower = userRepo.findFirstByUserEmail(followerEmail);

        if(followerTargetUser != null){
            if(followService.isFollowAllowed(followerTargetUser, follower)){
                followService.addfollowing(follow, follower);
                return follower.getUserHandle() + " is now follow "+ followerTargetUser.getUserHandle();
            }else{
                return follower.getUserHandle() + " Already follow "+ followerTargetUser.getUserHandle();
            }

        }else{
            return "Not Authorized to follow";
        }
    }

    public String unFollowUser(Integer followingId, String followerEmail) {
        Follow follow = followService.findFollow(followingId);
        if(follow != null){
            if(authorizeUnFollowRemove(follow,followerEmail)){
                followService.unFollow(follow);
                return "UnFollowed!! "+ follow.getCurrentUser().getUserHandle();

            }else {
                return "Unauthorized User !!";
            }

        }else{
            return "Invalid User !!";
        }
    }

    private boolean authorizeUnFollowRemove(Follow follow, String email) {
        String targetEmail = follow.getCurrentUser().getUserEmail();
        String followerEmail = follow.getCurrentUserFollower().getUserEmail();
        return targetEmail.equals(email) || followerEmail.equals(email);

    }
}

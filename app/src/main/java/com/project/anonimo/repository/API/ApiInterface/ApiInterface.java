package com.project.anonimo.repository.API.ApiInterface;

import com.project.anonimo.data.model.Comment;
import com.project.anonimo.data.model.Post;
import com.project.anonimo.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
//    String BASE_URL = "http://103.144.185.216:9090/apis/";
    String BASE_URL = "http://192.168.1.8:9090/apis/";


    @POST("signup")
    Call<String> signUpUser(@Body User user);

    @GET("signin/{userEmail}/{userPassword}")
    Call<User> signInUser(@Path("userEmail") String userEmail, @Path("userPassword") String userPassword);

    @GET("getAllPosts")
    Call<List<Post>> getAllPosts();

    @POST("createPost")
    Call<String> createPost(@Body Post post);

    @PUT("changeUserName")
    Call<String> updateUserName(@Body User user);

    @DELETE("deletePost/{postID}")
    Call<String> deletePost(@Path("postID") String postID);

    @GET("getPost/{postID}")
    Call<Post> getPost(@Path("postID") String postID);

    @POST("addComment")
    Call<String> addComment(@Body Post post);
}

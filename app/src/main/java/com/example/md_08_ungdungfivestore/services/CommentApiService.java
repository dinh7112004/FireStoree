package com.example.md_08_ungdungfivestore.services;

import com.example.md_08_ungdungfivestore.models.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentApiService {

    // Lấy tất cả comment
    @GET("api/comments")
    Call<List<Comment>> getAllComments();

    // Lấy comment theo id sản phẩm
    @GET("api/comments/{productId}")
    Call<List<Comment>> getCommentsByProduct(@Path("productId") String productId);

    // Thêm comment mới
    @POST("api/comments")
    Call<Comment> addComment(@Body Comment comment);

    // Xóa comment theo id
    @DELETE("api/comments/{id}")
    Call<Void> deleteComment(@Path("id") String id);
}

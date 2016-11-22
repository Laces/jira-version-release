package com.halcyonmobile.jiraversionrelease;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

interface JiraService {

    @GET("project/{board}/versions")
    Call<List<JiraVersion>> versions(@Path("board") String board);

    @PUT("version/{versionId}")
    Call<JiraVersion> updateVersion(@Path("versionId") String versionId, @Body JiraVersion jiraVersion);

    @POST("version")
    Call<JiraVersion> createVersion(@Body JiraVersion jiraVersion);
}



package com.halcyonmobile.jiraversionrelease;

import retrofit2.Call;

import java.util.List;

class RestApiWrapper {

    static Call<List<JiraVersion>> getJiraVersions(String baseUrl, String board, String credentials) {
        return RestClient.getService(JiraService.class, baseUrl, credentials).versions(board);
    }

    static Call<JiraVersion> updateVersion(JiraVersion jiraVersion, String baseUrl, String credentials) {
        return RestClient.getService(JiraService.class, baseUrl, credentials).updateVersion(jiraVersion.getId(), jiraVersion);
    }

    static Call<JiraVersion> createVersion(JiraVersion jiraVersion, String baseUrl, String credentials) {
        return RestClient.getService(JiraService.class, baseUrl, credentials).createVersion(jiraVersion);
    }
}

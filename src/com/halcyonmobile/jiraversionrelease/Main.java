package com.halcyonmobile.jiraversionrelease;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class Main {

    private static final int MIN_PARAM_COUNT = 8;
    private static final String OPTION_BASE_URL = "-url";
    private static final String OPTION_JIRA_BOARD = "-prefix";
    private static final String OPTION_VERSION = "-ver";
    private static final String OPTION_CREDENTIALS = "-cred";
    private static final String OPTION_DESCRIPTION = "-desc";

    private static String jiraBaseUrl;
    private static String jiraProject;
    private static String releaseVersion;
    private static String jiraBase64Credentials;
    private static String versionDescription;

    public static void main(String[] args) {
        if (args.length < MIN_PARAM_COUNT) {
            showHelp();
            return;
        }

        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case OPTION_BASE_URL:
                        jiraBaseUrl = args[i + 1];
                        break;
                    case OPTION_JIRA_BOARD:
                        jiraProject = args[i + 1];
                        break;
                    case OPTION_VERSION:
                        releaseVersion = args[i + 1];
                        break;
                    case OPTION_CREDENTIALS:
                        jiraBase64Credentials = args[i + 1];
                        break;
                    case OPTION_DESCRIPTION:
                        versionDescription = args[i + 1];
                        break;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            showHelp();
            return;
        }

        System.out.println("Getting all the versions from Jira...");
        RestApiWrapper.getJiraVersions(jiraBaseUrl, jiraProject, jiraBase64Credentials).enqueue(new Callback<List<JiraVersion>>() {
            @Override
            public void onResponse(Call<List<JiraVersion>> call, Response<List<JiraVersion>> response) {
                for (JiraVersion jv : response.body()) {
                    if (jv.getName().equals(releaseVersion)) {
                        if (!jv.isReleased()) {
                            System.out.println("The version: " + releaseVersion + " already exists, updating the release state...");
                            RestApiWrapper.updateVersion(jv.release(versionDescription), jiraBaseUrl, jiraBase64Credentials).enqueue(new Callback<JiraVersion>() {
                                @Override
                                public void onResponse(Call<JiraVersion> call, Response<JiraVersion> response) {
                                    System.out.println("Version: " + releaseVersion + " has been released successfully.");
                                    System.exit(0);
                                }

                                @Override
                                public void onFailure(Call<JiraVersion> call, Throwable throwable) {
                                    System.err.println("Failed to update version: " + throwable.getMessage());
                                    throwable.printStackTrace();
                                    System.exit(0);
                                }
                            });
                        } else {
                            System.out.println("The version: " + releaseVersion + " exists and it is already released, so nothing to do here.");
                            System.exit(0);
                        }
                        return;
                    }
                }

                System.out.println("The version: " + releaseVersion + " doesn't exists. Creating a new one...");
                RestApiWrapper.createVersion(new JiraVersion(releaseVersion, jiraProject).release(versionDescription), jiraBaseUrl, jiraBase64Credentials).enqueue(new Callback<JiraVersion>() {
                    @Override
                    public void onResponse(Call<JiraVersion> call, Response<JiraVersion> response) {
                        System.out.println("The new version: " + releaseVersion + " was created & released successfully. Jod bone!");
                        System.exit(0);
                    }

                    @Override
                    public void onFailure(Call<JiraVersion> call, Throwable throwable) {
                        System.err.println("Failed to create version: " + throwable.getMessage());
                        throwable.printStackTrace();
                        System.exit(0);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<JiraVersion>> call, Throwable throwable) {
                System.err.println("Failed to get version numbers from jira: " + throwable.getMessage());
                throwable.printStackTrace();
                System.exit(0);
            }
        });
    }

    private static void showHelp() {
        System.out.println("Usage: [-options] [args...]");
        System.out.println("where options include:");
        System.out.println("    " + OPTION_BASE_URL + "         your Jira root url");
        System.out.println("    " + OPTION_JIRA_BOARD + "      the Jira project prefix");
        System.out.println("    " + OPTION_VERSION + "         the desired version name to create and/or update");
        System.out.println("    " + OPTION_CREDENTIALS + "        Jira credentials in: Base64.encode(\"username:password\")");
        System.out.println("optional options:");
        System.out.println("    " + OPTION_DESCRIPTION + "        the description for the released version");
        System.out.println("Example: " + OPTION_BASE_URL + " mycompany.jira.com " + OPTION_JIRA_BOARD + " LAC " + OPTION_VERSION + " 1.2.3.4 " + OPTION_CREDENTIALS + " Zm9vOmJhcg== ");
    }
}


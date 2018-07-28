package com.example.maximus.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class GitHubModel {
    @SerializedName("full_name")
    private String repoName;
    @SerializedName("html_url")
    private String repoUrl;
    @SerializedName("stargazers_count")
    private String starsCount;
    @SerializedName("description")
    private String repoDescription;

    public String getRepoName() {
        return repoName;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getStarsCount() {
        return starsCount;
    }

    public String getRepoDescription() {
        return repoDescription;
    }


    public GitHubModel(String repoName, String starsCount, String repoDescription) {
        this.repoName = repoName;
        this.starsCount = starsCount;
        this.repoDescription = repoDescription;
    }


    public void setStarsCount(String starsCount) {
        this.starsCount = starsCount;
    }
}

package com.example.maximus.myapplication.utils;

import com.example.maximus.myapplication.model.GitHubModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GithubSearchResponse {
    List<GitHubModel> gitHubModels;


    public GithubSearchResponse() {
        gitHubModels = new ArrayList<>();
    }

    public static GithubSearchResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        GithubSearchResponse githubSearchResponse = gson.fromJson(response, GithubSearchResponse.class);
        return githubSearchResponse;
    }
 }

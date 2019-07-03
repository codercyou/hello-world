package com.changyou.community.provider;

import com.alibaba.fastjson.JSON;
import com.changyou.community.dto.AccessTokenDTO;
import com.changyou.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String url = "https://github.com/login/oauth/access_token";

        String s = com.alibaba.fastjson.JSON.toJSONString(accessTokenDTO);
        RequestBody body = RequestBody.create(JSON, s);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println("string:"+string);
            String accessToken = string.split("&")[0].split("=")[1];
            System.out.println("accessToken:"+accessToken);
            return accessToken;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public GithubUser getUser(String accessToken){
        String url = "https://api.github.com/user?access_token="+accessToken;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println("githubUser:"+string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

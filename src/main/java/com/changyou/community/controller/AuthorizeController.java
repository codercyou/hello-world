package com.changyou.community.controller;

import com.changyou.community.dto.AccessTokenDTO;
import com.changyou.community.dto.GithubUser;
import com.changyou.community.dto.User;
import com.changyou.community.mapper.UserMapper;
import com.changyou.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Controller
public class AuthorizeController {


    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("github.redirect.uri")
    private String redirectId;

    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(String code, String state, Model model, HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("fb8df772723c425b9d3a");
        accessTokenDTO.setClient_secret("79d3017a50fc3223d49065cfc0e39e02f74e515c");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri("http://localhost:8081/callback");
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);

        GithubUser githubUser = gitHubProvider.getUser(accessToken);

        System.out.println(githubUser);

        model.addAttribute("user",githubUser);

        if(githubUser!=null){
            //登录成功
            request.getSession().setAttribute("user",githubUser);
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountId(Long.toString(githubUser.getId()));
            user.setToken(UUID.randomUUID().toString());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            userMapper.insert(user);
            return "redirect:/";
        }else{
            return "redirect:/";
            //登录失败
        }
        //return "index";
    }


}

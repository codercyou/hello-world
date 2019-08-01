package com.changyou.community.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {
    @GetMapping("/getItemNames/{itemName}")
    public Map<String, List<String>> getItemNameByName(@PathVariable(name = "itemName") String itemName, HttpServletRequest request, HttpServletResponse response) {
        //String itemName = request.getParameter("itemName");
        System.out.println("调用finditemName 接口..itemName:" + itemName);

        //List<LogItem> items = formSubmitDao.findLogItemByName(itemName);
        //List<String> itemNames = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray();

        Map<String, List<String>> maps = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            jsonArray.add(itemName + i);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemName",jsonArray);
        if (maps != null) {
            System.out.println(jsonObject.toString());
            try {
                response.getWriter().write(jsonObject.toString());
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                response.getWriter().write(jsonObject.toString());
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

}

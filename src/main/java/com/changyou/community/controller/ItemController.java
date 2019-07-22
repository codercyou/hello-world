package com.changyou.community.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ItemController {
    @GetMapping("/getItemNames/{itemName}")
    public Map<String, List<String>> getItemNameByName(@PathVariable(name = "itemName") String itemName, HttpServletRequest request, HttpServletResponse response){
        //String itemName = request.getParameter("itemName");
        System.out.println("调用finditemName 接口..itemName:"+itemName);

        //List<LogItem> items = formSubmitDao.findLogItemByName(itemName);
        List<String> itemNames = new ArrayList<String>();
        Map<String, List<String>> maps = new HashMap<>();
        for (int i=0;i<5;i++) {
            itemNames.add(itemName+i);
        }
        maps.put("itemName", itemNames);
        /*if(items != null){
            for(LogItem logItem:items){
                itemNames.add(logItem.getItemName());
            }
        }*/
        return maps;
        /*if(maps != null){
            System.out.println("itemNames:"+maps.toString());
            try {
                response.getWriter().write(maps.toString());
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            try {
                response.getWriter().write("没有结果..");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/


    }

}

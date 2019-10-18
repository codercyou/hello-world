package com.changyou.community.cache;


import com.changyou.community.dto.HotTagDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HotTagCache {


    private Map<String,Integer> tags = new HashMap<>();

    public List<String> getHots() {
        return hots;
    }

    public void setHots(List<String> hots) {
        this.hots = hots;
    }

    private List<String> hots = new ArrayList<>();

    public Map<String, Integer> getTags() {
        return tags;
    }

    public void setTags(Map<String, Integer> tags) {
        this.tags = tags;
    }

    //使用java top N  小顶堆
    public void updateTags(Map<String,Integer> tags){
        int max=3;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        System.out.println("---------------------->"+tags.size());
        tags.forEach(
                (name,priority)->{
                    HotTagDTO hotTagDTO = new HotTagDTO();
                    hotTagDTO.setName(name);
                    hotTagDTO.setPriority(priority);

                    if(priorityQueue.size()<3){
                        priorityQueue.add(hotTagDTO);

                    }else{
                        HotTagDTO minHot = priorityQueue.peek();
                        if(hotTagDTO.compareTo(minHot)>0){
                            priorityQueue.poll();
                            priorityQueue.add(hotTagDTO);
                        }
                    }
                }
        );

        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111");
        ArrayList<String> sortedTags = new ArrayList<>();
        HotTagDTO poll = priorityQueue.poll();
        while(poll!=null){
            sortedTags.add(0,poll.getName());
            poll = priorityQueue.poll();
        }
        System.out.println("sortedTags:"+sortedTags);
        hots = sortedTags;


    }
}

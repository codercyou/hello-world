package com.changyou.community.schedule;

import com.changyou.community.cache.HotTagCache;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.*;

@Component
@Slf4j
public class HotTagTasks {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    //@Scheduled(fixedRate = 50000)
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 20;
        System.out.println("hotTagSchedule start {}" + new Date());
        List<Question> list = new ArrayList<>();
try{
        Map<String, Integer> priorities = new HashMap<>();
        while (offset == 0 || list.size() == limit) {
            list = questionMapper.list(offset, limit);
            System.out.println("list.size():" + list.size());
            if(list.size()>0) {
                for (Question question : list) {
                    System.out.println("question.getTag():" + question.getTag());
                    String[] tags = question.getTag().split(",");
                    //System.out.println("tags:"+tags);
                    for (String tag : tags) {
                        Integer priority = priorities.get(tag);
                        if (priority != null) {
                            priorities.put(tag, priority + 5 + question.getCommentCount());
                        } else {
                            priorities.put(tag, 5 + question.getCommentCount());
                        }
                    }
                }
            }
            offset += limit;
        }
    hotTagCache.updateTags(priorities);
    hotTagCache.setTags(priorities);
    }catch (Exception e){
    e.printStackTrace();
}

        System.out.println(hotTagCache.getHots());

        hotTagCache.getTags().forEach(
                (k,v) ->{
                    System.out.println(k+":"+v);
                }
        );
        System.out.println("hotTagSchedule stop {}"+ new Date());
    }
}

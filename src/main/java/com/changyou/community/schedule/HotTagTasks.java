package com.changyou.community.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class HotTagTasks {
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime(){
        System.out.println("----------------->the time is :"+new Date());
    }
}

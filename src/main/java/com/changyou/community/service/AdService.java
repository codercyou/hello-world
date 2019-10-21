package com.changyou.community.service;


import com.changyou.community.mapper.AdMapper;
import com.changyou.community.model.Ad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdService {
    @Autowired
    private AdMapper adMapper;

    public List<Ad> list() {
        List<Ad> ads =  adMapper.getAds(System.currentTimeMillis());
        return ads;
    }
}
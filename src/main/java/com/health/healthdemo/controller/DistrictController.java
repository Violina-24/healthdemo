package com.health.healthdemo.controller;

import com.health.healthdemo.entity.MDistrict;
import com.health.healthdemo.repository.MDistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class DistrictController {

    @Autowired
    private MDistrictRepository mDistrictRepository;

    @GetMapping("/api/districts")
    public List<Map<String, Object>> getDistricts() {
        List<MDistrict> districts = mDistrictRepository.findAll();

        return districts.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("d_id", d.getD_id());
            map.put("districtname", d.getDistrictname());
            return map;
        }).collect(Collectors.toList());
    }

}

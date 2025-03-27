package com.health.healthdemo.services;


import com.health.healthdemo.entity.TApplication;
import com.health.healthdemo.repository.TApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private TApplicationRepository applicationRepository;

    public TApplication saveApplication(TApplication application){
        return
                applicationRepository.save(application);
    }

    public Optional<TApplication> getApplicationById(Long A_id){
        return applicationRepository.findById(A_id);
    }

    public List<TApplication> getAllApplications(){
        return applicationRepository.findAll();
    }
    public void deleteApplicationById(Long A_id){
        applicationRepository.deleteById(A_id);
    }
}

package com.health.healthdemo.services;


import com.health.healthdemo.entity.MCourse;
import com.health.healthdemo.entity.MPreference;
import com.health.healthdemo.entity.MUsers;
import com.health.healthdemo.repository.MPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreferenceService {
    @Autowired
    private MPreferenceRepository Preferencerepository;
    public MPreference savePreference(MPreference preference) {
        return Preferencerepository.save(preference);
    }
    public Optional<MPreference> getPreferenceById(Long Pid) {
        return Preferencerepository.findById(Pid);
    }
    public List<MPreference> getAllPreferences() {
        return Preferencerepository.findAll();
    }
    public void deletePreference(Long Pid) {
        Preferencerepository.deleteById(Pid);
    }
    public boolean existsByMUsersAndMCourse(MUsers mUsers, MCourse mCourse) {

        return Preferencerepository.existsByMUsersAndMCourse(mUsers,mCourse);
    }

}

package com.health.healthdemo.controller;


import com.health.healthdemo.entity.MPreference;
import com.health.healthdemo.services.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

@Controller
@RequestMapping("/preferences")
public class PreferenceController {
    @Autowired

    private PreferenceService preferenceService;
    @PostMapping("/add")
    public ResponseEntity<?> addPreference(@RequestBody MPreference preference) {
        try {
            MPreference savedPreference = preferenceService.savePreference(preference);
            return ResponseEntity.ok(savedPreference);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        }

    @GetMapping("/id")
    public ResponseEntity<MPreference> getPreference(@PathVariable Long Pid)
    {
        Optional<MPreference> preference = preferenceService.getPreferenceById(Pid);
        return preference.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<MPreference> getAllPreference(){
        return preferenceService.getAllPreferences();
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id) {
            preferenceService.deletePreference(id);
            return ResponseEntity.noContent().build();
    }
}

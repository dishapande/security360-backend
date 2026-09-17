package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Site;
import com.security360.security360_backend.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
@CrossOrigin(origins = "http://localhost:8081")
public class SiteController {

    @Autowired private SiteRepository siteRepository;

    @GetMapping
    public ResponseEntity<List<Site>> getAllSites() {
        return ResponseEntity.ok(siteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> getSiteById(@PathVariable Long id) {
        return siteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createSite(@RequestBody Site site) {
        siteRepository.save(site);
        return ResponseEntity.ok("Site created successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSite(@PathVariable Long id, @RequestBody Site siteDetails) {
        return siteRepository.findById(id)
                .map(existingSite -> {
                    existingSite.setSiteName(siteDetails.getSiteName());
                    existingSite.setSiteCode(siteDetails.getSiteCode());
                    existingSite.setAddress(siteDetails.getAddress());
                    existingSite.setCity(siteDetails.getCity());
                    existingSite.setState(siteDetails.getState());
                    existingSite.setLatitude(siteDetails.getLatitude());
                    existingSite.setLongitude(siteDetails.getLongitude());
                    existingSite.setStatus(siteDetails.getStatus());
                    siteRepository.save(existingSite);
                    return ResponseEntity.ok("Site updated successfully!");
                })
                .orElse(ResponseEntity.badRequest().body("Site not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSite(@PathVariable Long id) {
        siteRepository.deleteById(id);
        return ResponseEntity.ok("Site deleted successfully!");
    }

    // 6. GET: Site Report (For Dashboard)
    @GetMapping("/report")
    public ResponseEntity<String> getSiteReport() {
        long totalSites = siteRepository.count();
        long activeSites = siteRepository.countByStatus("Active");
        long inactiveSites = siteRepository.countByStatus("Inactive");
        return ResponseEntity.ok("{\"totalSites\": " + totalSites + ", \"activeSites\": " + activeSites + ", \"inactiveSites\": " + inactiveSites + "}");
    }
}
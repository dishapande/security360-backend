package com.security360.security360_backend.controller;

import com.security360.security360_backend.entity.Asset;
import com.security360.security360_backend.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "http://localhost:8081")
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    // 1. GET: Fetch all assets
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetRepository.findAllByOrderByCreatedAtDesc());
    }

    // 2. GET: Fetch a single asset by ID
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return assetRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST: Create a new asset
    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset) {
        // Auto-generate asset code
        if (asset.getAssetCode() == null || asset.getAssetCode().isEmpty()) {
            asset.setAssetCode("AST-" + System.currentTimeMillis());
        }
        Asset saved = assetRepository.save(asset);
        return ResponseEntity.ok(saved);
    }

    // 4. PUT: Update an asset (Used for issue, return, maintenance)
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset updated) {
        Asset asset = assetRepository.findById(id).orElseThrow();
        
        if (updated.getIssuedTo() != null) asset.setIssuedTo(updated.getIssuedTo());
        if (updated.getSite() != null) asset.setSite(updated.getSite());
        if (updated.getStatus() != null) asset.setStatus(updated.getStatus());
        if (updated.getNextService() != null) asset.setNextService(updated.getNextService());
        if (updated.getName() != null) asset.setName(updated.getName());
        if (updated.getCategory() != null) asset.setCategory(updated.getCategory());
        
        return ResponseEntity.ok(assetRepository.save(asset));
    }

    // 5. DELETE: Delete an asset
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
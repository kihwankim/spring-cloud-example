package com.example.catalogservice.controller;

import com.example.catalogservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {
    private final Environment env;
    private final CatalogService catalogService;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in Catalog Service on Port %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<?> getCatalogs() {
        return ResponseEntity.ok(catalogService.getAllCatalogs());
    }
}

package com.example.catalogservice.service;

import com.example.catalogservice.domain.vo.ResponseCatalog;

import java.util.List;

public interface CatalogService {
    List<ResponseCatalog> getAllCatalogs();
}

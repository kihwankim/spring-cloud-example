package com.example.catalogservice.service;

import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.domain.vo.ResponseCatalog;
import com.example.catalogservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public List<ResponseCatalog> getAllCatalogs() {
        List<Catalog> catalogs = catalogRepository.findAll();
        List<ResponseCatalog> responseCatalogs = new ArrayList<>();
        for (Catalog v : catalogs) {
            responseCatalogs.add(new ModelMapper().map(v, ResponseCatalog.class));
        }

        return responseCatalogs;
    }
}

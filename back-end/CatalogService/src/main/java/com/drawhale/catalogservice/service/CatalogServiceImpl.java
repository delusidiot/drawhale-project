package com.drawhale.catalogservice.service;

import com.drawhale.catalogservice.repository.CatalogEntity;
import com.drawhale.catalogservice.repository.CatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService{

    private final CatalogRepository catalogRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        Iterable<CatalogEntity> catalogs = catalogRepository.findAll();
        return catalogs;
    }
}

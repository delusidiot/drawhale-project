package com.drawhale.catalogservice.service;

import com.drawhale.catalogservice.repository.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}

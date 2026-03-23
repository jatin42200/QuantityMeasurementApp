package com.example.repository;

import com.example.entity.QuantityMeasurementEntity;
import com.example.exception.DatabaseException;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

	@Override
	public QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
		// Legacy implementation - not used in Spring Boot application
		// This is kept for backwards compatibility
		return entity;
	}

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
		// Legacy implementation - not used in Spring Boot application
		// Return empty list
		return new ArrayList<>();
    }

    @Override
    public void deleteAll() {
		// Legacy implementation - not used in Spring Boot application
    }

	@Override
	public List<QuantityMeasurementEntity> findAll() {
		// Legacy implementation - not used in Spring Boot application
		return new ArrayList<>();
	}
}
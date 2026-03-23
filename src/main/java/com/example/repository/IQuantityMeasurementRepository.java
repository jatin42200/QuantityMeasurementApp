package com.example.repository;

import com.example.entity.QuantityMeasurementEntity;
import java.util.List;

public interface IQuantityMeasurementRepository {

    QuantityMeasurementEntity save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> findAll();

	void deleteAll();

	List<QuantityMeasurementEntity> getAllMeasurements();
}
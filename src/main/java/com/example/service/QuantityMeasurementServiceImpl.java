package com.example.service;

import com.example.dto.QuantityDTO;
import com.example.dto.QuantityInputDTO;
import com.example.entity.QuantityMeasurementEntity;
import com.example.repository.QuantityMeasurementRepository;
import com.example.repository.QuantityMeasurementCacheRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl
        implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;
    
    private QuantityMeasurementCacheRepository cacheRepository;
    
    public QuantityMeasurementServiceImpl() {
    }
    
    public QuantityMeasurementServiceImpl(QuantityMeasurementCacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    // ============ OVERLOADED METHODS FOR DIRECT DTO USAGE ============
    
    // Comparison method for two QuantityDTOs
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            return false;
        }
        
        double value1 = q1.getValue();
        double value2 = q2.getValue();
        
        String unit1 = q1.getUnit();
        String unit2 = q2.getUnit();
        
        // Unit conversion for LENGTH measurements
        if ("LENGTH".equals(q1.getMeasurementType()) && "LENGTH".equals(q2.getMeasurementType())) {
            // Convert to base unit (INCHES)
            if ("FEET".equals(unit1)) {
                value1 = value1 * 12;
            }
            if ("FEET".equals(unit2)) {
                value2 = value2 * 12;
            }
        }
        
        return Math.abs(value1 - value2) < 0.0001; // Handle floating point comparison
    }
    
    // Addition method for two QuantityDTOs
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Quantities cannot be null");
        }
        
        double value1 = q1.getValue();
        double value2 = q2.getValue();
        String unit1 = q1.getUnit();
        
        // Just add the values without unit conversion
        // Result is in the unit of the first quantity
        double result = value1 + value2;
        
        return new QuantityDTO(result, unit1, q1.getMeasurementType());
    }
    
    // Subtraction method for two QuantityDTOs
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Quantities cannot be null");
        }
        
        double value1 = q1.getValue();
        double value2 = q2.getValue();
        String unit1 = q1.getUnit();
        
        // Just subtract the values without unit conversion
        // Result is in the unit of the first quantity
        double result = value1 - value2;
        
        return new QuantityDTO(result, unit1, q1.getMeasurementType());
    }
    
    // Division method for two QuantityDTOs
    public double divide(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Quantities cannot be null");
        }
        
        double value2 = q2.getValue();
        
        if (Math.abs(value2) < 0.0001) {
            throw new ArithmeticException("Division by zero");
        }
        
        double value1 = q1.getValue();
        
        // No conversion needed for division of same type units
        return value1 / value2;
    }
    
    // Conversion method for QuantityDTO to a different unit
    public QuantityDTO convert(QuantityDTO q, String targetUnit) {
        if (q == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        
        double value = q.getValue();
        String unit = q.getUnit();
        
        // Conversion logic for LENGTH
        if ("LENGTH".equals(q.getMeasurementType())) {
            if ("FEET".equals(unit) && "INCHES".equals(targetUnit)) {
                value = value * 12;
            } else if ("INCHES".equals(unit) && "FEET".equals(targetUnit)) {
                value = value / 12;
            }
            // If same unit, no conversion needed
        }
        
        return new QuantityDTO(value, targetUnit, q.getMeasurementType());
    }

    // ============ ORIGINAL METHODS ============

    @Override
    public QuantityMeasurementEntity compare(QuantityInputDTO input) {

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

        double thisValue = input.getThisQuantityDTO().getValue();
        double thatValue = input.getThatQuantityDTO().getValue();

        String thisUnit = input.getThisQuantityDTO().getUnit();
        String thatUnit = input.getThatQuantityDTO().getUnit();

        entity.setThisValue(thisValue);
        entity.setThisUnit(thisUnit);
        entity.setThisMeasurementType(input.getThisQuantityDTO().getMeasurementType());

        entity.setThatValue(thatValue);
        entity.setThatUnit(thatUnit);
        entity.setThatMeasurementType(input.getThatQuantityDTO().getMeasurementType());

        // unit conversion
        if (thisUnit.equals("FEET") && thatUnit.equals("INCHES")) {
            thisValue = thisValue * 12;
        }

        if (thisUnit.equals("INCHES") && thatUnit.equals("FEET")) {
            thatValue = thatValue * 12;
        }

        boolean result = thisValue == thatValue;

        entity.setOperation("COMPARE");
        entity.setResultString(String.valueOf(result));

        return repository.save(entity);
    }

    // ---------------- CONVERT ----------------

    @Override
    public QuantityMeasurementEntity convert(QuantityInputDTO input) {

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

        entity.setThisValue(input.getThisQuantityDTO().getValue());
        entity.setThisUnit(input.getThisQuantityDTO().getUnit());
        entity.setThisMeasurementType(input.getThisQuantityDTO().getMeasurementType());

        entity.setOperation("CONVERT");

        double value = input.getThisQuantityDTO().getValue();

        if (input.getThisQuantityDTO().getUnit().equals("FEET")) {
            entity.setResultValue(value * 12);
            entity.setResultUnit("INCHES");
        }

        return repository.save(entity);
    }

    // ---------------- ADD ----------------

    @Override
    public QuantityMeasurementEntity add(QuantityInputDTO input) {

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

        entity.setThisValue(input.getThisQuantityDTO().getValue());
        entity.setThisUnit(input.getThisQuantityDTO().getUnit());
        entity.setThisMeasurementType(input.getThisQuantityDTO().getMeasurementType());

        entity.setThatValue(input.getThatQuantityDTO().getValue());
        entity.setThatUnit(input.getThatQuantityDTO().getUnit());
        entity.setThatMeasurementType(input.getThatQuantityDTO().getMeasurementType());

        double thisValue = input.getThisQuantityDTO().getValue();
        double thatValue = input.getThatQuantityDTO().getValue();

        String thisUnit = input.getThisQuantityDTO().getUnit();
        String thatUnit = input.getThatQuantityDTO().getUnit();

        // Convert everything to inches
        if(thisUnit.equals("FEET")){
            thisValue = thisValue * 12;
        }

        if(thatUnit.equals("FEET")){
            thatValue = thatValue * 12;
        }

        double resultInInches = thisValue + thatValue;

        // convert back to feet
        double resultInFeet = resultInInches / 12;

        entity.setResultValue(resultInFeet);
        entity.setResultUnit("FEET");
        entity.setOperation("ADD");

        return repository.save(entity);
    }
    // ---------------- HISTORY ----------------

    @Override
    public List<QuantityMeasurementEntity> getHistoryByOperation(String operation) {
        return repository.findByOperation(operation);
    }

    // ---------------- COUNT ----------------

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndErrorFalse(operation);
    }
}
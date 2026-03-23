package com.example.dto;

public class QuantityDTO {

    private Double value;
    private String unit;
    private String measurementType;

    public QuantityDTO() {
    }

    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    public QuantityDTO(int value, String unit, String measurementType) {
        this.value = (double) value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

	
}
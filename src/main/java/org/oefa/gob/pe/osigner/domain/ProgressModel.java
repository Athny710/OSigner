package org.oefa.gob.pe.osigner.domain;

public class ProgressModel {

    private double initialValue;
    private double partialValue;

    public ProgressModel(double initialValue, double partialValue) {
        this.initialValue = initialValue;
        this.partialValue = partialValue;
    }


    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getPartialValue() {
        return partialValue;
    }

    public void setPartialValue(double partialValue) {
        this.partialValue = partialValue;
    }
}

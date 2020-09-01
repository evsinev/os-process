package com.payneteasy.osprocess.api;

public class ProcessEnvVariable {

    private final String name;
    private final String value;

    public ProcessEnvVariable(String name, String value) {
        this.name  = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSafeValue() {
        return value != null ? value : "";
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}

package com.payneteasy.osprocess.api;

import lombok.Data;

@Data
public class ProcessRunResult {

    private final int    exitCode;
    private final String output;

}

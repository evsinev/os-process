package com.payneteasy.osprocess.api;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class ProcessDescriptor {

    private final String                   command;
    private final List<String>             args;
    private final List<ProcessEnvVariable> envVariables;
    private final File                     workingDir;

}

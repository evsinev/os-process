package com.payneteasy.osprocess.impl;

import com.payneteasy.osprocess.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;

import static com.payneteasy.osprocess.impl.Safe.safeList;
import static java.lang.ProcessBuilder.Redirect.PIPE;
import static java.util.Collections.singletonList;

public class ProcessServiceImpl implements IProcessService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessServiceImpl.class);


    public ProcessServiceImpl() {

    }
    
    public ProcessServiceImpl(ExecutorService executor) {
    }

    @Override
    public ProcessRunResult runProcess(ProcessDescriptor aDescriptor) throws ProcessException {
        Exchanger<ProcessRunResult> exchanger = new Exchanger<>();
        startProcess(aDescriptor, new ProcessorListenerWaiter(exchanger));
        try {
            return exchanger.exchange(null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ProcessRunResult(-1, "Cannot exchange value");
        }
    }

    @Override
    public Process startProcess(ProcessDescriptor aDescriptor, IProcessListener aListener) throws ProcessException {
        Process process;
        try {
            List<String> commandArray = createCommandArray(aDescriptor.getCommand(), aDescriptor.getArgs());

            LOG.debug("Executing {} with working dir {} and env {} ...", commandArray, aDescriptor.getWorkingDir(), aDescriptor.getEnvVariables());

            process = putEnvVariables(new ProcessBuilder(commandArray), aDescriptor.getEnvVariables())
                    .directory(aDescriptor.getWorkingDir())
                    .redirectInput       ( PIPE )
                    .redirectOutput      ( PIPE )
                    .redirectError       ( PIPE )
                    .redirectErrorStream ( true )
                    .start();

        } catch (IOException e) {
            throw new ProcessException("Cannot run " + aDescriptor.getCommand(), e);
        }

        Thread inputThread = new Thread(new ProcessOutputStreamReader(process.getInputStream(), aListener::onStandardOut, aListener::onReadError));
        Thread errorThread = new Thread(new ProcessOutputStreamReader(process.getInputStream(), aListener::onStandardOut, aListener::onReadError));

        inputThread.start();
        errorThread.start();

        Thread waitingThread = new Thread( () -> {
            try {
                int result = process.waitFor();
                aListener.onExit(result);
            } catch (InterruptedException e) {
                LOG.error("Cannot execute process.waitFor(). Exiting with code -1", e);
                aListener.onExit(-1);
                Thread.currentThread().interrupt();
            }
        });

        waitingThread.start();
        return process;
    }

    private ProcessBuilder putEnvVariables(ProcessBuilder aProcessBuilder, List<ProcessEnvVariable> aVariables) {
        putEnvVariables(aProcessBuilder.environment(), aVariables);
        return aProcessBuilder;
    }

    private void putEnvVariables(Map<String, String> aMap, List<ProcessEnvVariable> aVariables) {
        for (ProcessEnvVariable variable : safeList(aVariables)) {
            aMap.put(variable.getName(), variable.getValue());
        }
    }

    private List<String> createCommandArray(String aCommand, List<String> aArgs) {
        if (aArgs == null) {
            return singletonList(aCommand);
        }

        List<String> command = new ArrayList<>(aArgs.size() + 1);
        command.add(aCommand);
        command.addAll(aArgs);
        return command;
    }
}

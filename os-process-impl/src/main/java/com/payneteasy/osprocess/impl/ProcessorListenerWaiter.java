package com.payneteasy.osprocess.impl;

import com.payneteasy.osprocess.api.IProcessListener;
import com.payneteasy.osprocess.api.ProcessRunResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Exchanger;

public class ProcessorListenerWaiter implements IProcessListener {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorListenerWaiter.class);

    private final Queue<String>               lines = new ConcurrentLinkedQueue<>();
    private final Exchanger<ProcessRunResult> exchanger;

    public ProcessorListenerWaiter(Exchanger<ProcessRunResult> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void onStandardOut(String aLine) {
        LOG.debug("onStandardOut: {}", aLine);
        lines.add(aLine);
    }

    @Override
    public void onErrorOut(String aLine) {
        LOG.debug("onErrorOut: {}", aLine);
        lines.add(aLine);
    }

    @Override
    public void onExit(int aExitCode) {
        LOG.debug("onExit: {}", aExitCode);
        try {
            exchanger.exchange(new ProcessRunResult(aExitCode, getLines()));
        } catch (InterruptedException e) {
            LOG.error("Cannot exchange", e);
            Thread.currentThread().interrupt();
        }
    }

    private String getLines() throws InterruptedException {
        for(int i = 0; i < 10 && lines.isEmpty(); i++) {
            LOG.warn("No any output from process. Sleeping 100 ms ...");
            Thread.sleep(100);
        }
        
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    @Override
    public synchronized void onReadError(IOException e) {
        lines.add("error: " + e.getMessage());
    }
}

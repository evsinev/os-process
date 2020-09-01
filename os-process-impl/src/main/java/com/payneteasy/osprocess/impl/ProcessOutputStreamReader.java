package com.payneteasy.osprocess.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;

public class ProcessOutputStreamReader implements Runnable {

    private final InputStream                 in;
    private final IProcessOutputLineListener  listener;
    private final IProcessOutputErrorListener errorListener;

    public ProcessOutputStreamReader(InputStream in, IProcessOutputLineListener listener, IProcessOutputErrorListener aErrorListener) {
        this.in       = in;
        this.listener = listener;
        errorListener = aErrorListener;
    }

    @Override
    public void run() {
        try {
            try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ( (line = reader.readLine()) != null) {
                    listener.onOutput(line);
                }
            }
        } catch (IOException e) {
            errorListener.onError(e);
        }
    }
}

package com.payneteasy.osprocess.api;

import java.io.IOException;

public interface IProcessListener {

    void onStandardOut(String aLine);

    void onErrorOut(String aLine);

    void onExit(int aExitCode);

    void onReadError(IOException e);

}

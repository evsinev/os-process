package com.payneteasy.osprocess.impl;

import java.io.IOException;

public interface IProcessOutputErrorListener {

    void onError(IOException e);
}

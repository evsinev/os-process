package com.payneteasy.osprocess.api;

public interface IProcessService {

    ProcessRunResult runProcess(ProcessDescriptor aDescriptor) throws ProcessException;

    Process startProcess(ProcessDescriptor aDescriptor, IProcessListener aListener) throws ProcessException;

}

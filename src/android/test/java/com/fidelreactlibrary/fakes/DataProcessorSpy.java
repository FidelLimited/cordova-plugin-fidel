package com.fidelcordovalibrary.fakes;

import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;

public final class DataProcessorSpy<T> implements DataProcessor<T> {

    public T dataToProcess;
    public Boolean hasAskedToProcessData = false;

    @Override
    public void process(T data) {
        hasAskedToProcessData = true;
        dataToProcess = data;
    }
}

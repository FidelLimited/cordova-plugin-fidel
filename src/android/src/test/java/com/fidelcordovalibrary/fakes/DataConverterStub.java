package com.fidelcordovalibrary.fakes;

import com.fidelcordovalibrary.adapters.abstraction.DataConverter;

public final class DataConverterStub<D, C> implements DataConverter<D, C> {

    public D dataReceived;
    public C convertedDataToReturn;

    @Override
    public C getConvertedDataFor(D data) {
        dataReceived = data;
        return convertedDataToReturn;
    }
}

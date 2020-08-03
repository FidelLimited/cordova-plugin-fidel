package com.fidelcordovalibrary.adapters.abstraction;

public interface DataProcessor<RawDataType> {
    void process(RawDataType data);
}

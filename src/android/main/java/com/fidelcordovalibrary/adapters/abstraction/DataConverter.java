package com.fidelcordovalibrary.adapters.abstraction;

public interface DataConverter<DataType, ConvertedDataType> {
    ConvertedDataType getConvertedDataFor(DataType data);
}

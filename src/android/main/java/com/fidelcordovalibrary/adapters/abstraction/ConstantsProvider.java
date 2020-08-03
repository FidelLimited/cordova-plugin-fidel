package com.fidelcordovalibrary.adapters.abstraction;

import java.util.Map;

import javax.annotation.Nonnull;

public interface ConstantsProvider {
    @Nonnull Map<String, Object> getConstants();
}

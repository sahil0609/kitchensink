package com.sahil.kitchensink.model;

import lombok.Data;

import java.util.List;

@Data
public class ErrorData {
    private List<String> message;
    private String code;
    private boolean retryable;
}

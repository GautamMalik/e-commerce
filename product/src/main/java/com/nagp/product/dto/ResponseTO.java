package com.nagp.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTO<T> {
    private boolean error;
    private List<com.nagp.product.dto.Error> errors;
    private T data;
}

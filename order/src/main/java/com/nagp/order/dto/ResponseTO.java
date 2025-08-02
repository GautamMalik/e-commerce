package com.nagp.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTO<T> {
    private boolean error;
    private List<com.nagp.order.dto.Error> errors;
    private T data;
}

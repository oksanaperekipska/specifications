package com.demo.service.filter.criteria.base;

import lombok.Data;

@Data
public class RangeFilter<T extends Comparable<? super T>> {
    private T greaterThanOrEqual;
    private T lessThanOrEqual;
}

package com.demo.service.filter.criteria.base;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
public class InstantFilter extends RangeFilter<Instant> implements Serializable {

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setGreaterThanOrEqual(Instant equals) {
        super.setGreaterThanOrEqual(equals);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setLessThanOrEqual(Instant equals) {
        super.setLessThanOrEqual(equals);
    }

    public InstantFilter(Instant greaterThanOrEqual, Instant lessThanOrEqual) {
        super.setGreaterThanOrEqual(greaterThanOrEqual);
        super.setLessThanOrEqual(lessThanOrEqual);

    }
}

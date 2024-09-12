package com.taboola.rest.api.model;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiRequestHeadersSupplier implements RequestHeadersSupplier {
    private final Collection<RequestHeadersSupplier> suppliers;

    public MultiRequestHeadersSupplier(RequestHeadersSupplier... suppliers) {
        this.suppliers = Stream.of(suppliers == null ? new RequestHeadersSupplier[0] : suppliers)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<RequestHeader> get() {
        return suppliers.stream()
                .map(RequestHeadersSupplier::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}

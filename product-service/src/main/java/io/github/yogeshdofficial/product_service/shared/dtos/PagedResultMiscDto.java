package io.github.yogeshdofficial.product_service.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResultMiscDto {
    long totalElements;
    int pageNumber;
    int totalPages;
    boolean isFirst;
    boolean isLast;
    boolean hasNext;
    boolean hasPrevious;
}

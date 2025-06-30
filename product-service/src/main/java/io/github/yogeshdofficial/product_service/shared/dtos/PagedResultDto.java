package io.github.yogeshdofficial.product_service.shared.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResultDto<T> {
    private List<T> data;
    private PagedResultMiscDto misc;
}

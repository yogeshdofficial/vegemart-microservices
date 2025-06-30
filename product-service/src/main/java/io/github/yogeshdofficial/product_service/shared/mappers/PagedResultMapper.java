package io.github.yogeshdofficial.product_service.shared.mappers;

import io.github.yogeshdofficial.product_service.shared.dtos.PagedResultDto;
import io.github.yogeshdofficial.product_service.shared.dtos.PagedResultMiscDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PagedResultMapper {

  default <T> PagedResultDto<T> toPagedResultDto(Page<T> page) {
    return PagedResultDto.<T>builder()
        .data(page.getContent().stream().toList())
        .misc(
            PagedResultMiscDto.builder()
                .totalElements(page.getTotalElements())
                .pageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build())
        .build();
  }
}

package io.github.yogeshdofficial.product_service.shared.dtos;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorDto {
    private HttpStatusCode status;
    private String message;
    private List<String> errors;
    private String serviceName;
    private Instant timestamp;
}

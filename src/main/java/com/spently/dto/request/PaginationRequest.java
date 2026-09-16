package com.spently.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaginationRequest {
    @Schema(
            description = "Current page number",
            example = "0"
    )
    @NotNull
    @Min(0)
    private int pageNo;

    @Schema(
            description = "Number of items per page",
            example = "10"
    )
    @NotNull
    @Min(1)
    private int pageSize;

}
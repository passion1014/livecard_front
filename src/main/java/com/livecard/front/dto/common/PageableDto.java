package com.livecard.front.dto.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PageableDto {
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean empty;
    private Boolean sorted;
    private Boolean unsorted;
    private Long offset;
    private Boolean paged;
    private Boolean unpaged;
}

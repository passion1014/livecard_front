package com.livecard.front.dto.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class ListDto {
    private List<?> content;
    private PageableDto pageable;
    private Integer totalPages;
    private Long totalElements;
    private Boolean last;
    private Integer size;
    private Integer number;
    private SpringDataJaxb.SortDto sort; // SortDto는 정렬 정보를 나타내는 별도의 클래스로 정의
    private Integer numberOfElements;
    private Boolean first;
    private Boolean empty;
    private Map<String, Object> customField = new HashMap<>();
}

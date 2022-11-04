package org.testmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Pagination {

    private int pageNumber;

    private int pageSize;

    private int totalPages;

    private long totalElements;

}

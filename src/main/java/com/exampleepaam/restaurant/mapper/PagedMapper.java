package com.exampleepaam.restaurant.mapper;

import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.model.entity.paging.Paging;

import java.util.List;

/**
 * Mapper class for Paged object
 */
public class PagedMapper {
    private PagedMapper() {
    }

    /**
     * Creates a new Paged object
     *
     * @param items       list of items
     * @param currentPage current page
     * @param totalRows   total rows
     * @param pageSize    rows per page
     * @return paged object
     */
    public static <T> Paged<T> toPaged(List<T> items, int currentPage, long totalRows, int pageSize) {
        int totalPages = Math.toIntExact(totalRows / pageSize);
        if (totalRows % pageSize > 0) {
            totalPages++;
        }
        return new Paged<>(items, Paging.of(totalPages, currentPage, pageSize));

    }
}

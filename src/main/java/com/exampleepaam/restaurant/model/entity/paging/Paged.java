package com.exampleepaam.restaurant.model.entity.paging;


import java.util.List;
import java.util.Objects;

/**
 * Class for Pagination.
 * It contains objects for rows in a list and pagination data in a Paging object.
 */
public class Paged<T> {

    List<T> pageContent;
    private Paging paging;


    public Paged() {
    }

    public Paged(List<T> pageContent, Paging paging) {
        this.pageContent = pageContent;
        this.paging = paging;
    }

    @Override
    public String toString() {
        return "Paged{" +
                "pageContent=" + pageContent +
                ", paging=" + paging +
                '}';
    }

    public List<T> getPageContent() {
        return pageContent;
    }

    public void setPageContent(List<T> pageContent) {
        this.pageContent = pageContent;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paged<?> paged = (Paged<?>) o;
        return Objects.equals(pageContent, paged.pageContent) && Objects.equals(paging, paged.paging);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageContent, paging);
    }
}

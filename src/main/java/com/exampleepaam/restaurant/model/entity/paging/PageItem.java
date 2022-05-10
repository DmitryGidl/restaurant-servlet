package com.exampleepaam.restaurant.model.entity.paging;


import java.util.Objects;

/**
 * PageItems describes a Page for Pagination.
 * Uses a builder pattern
 */
public class PageItem {

    private PageItemType pageItemType;

    private int index;

    private boolean active;

    public static PageBuilder builder() {
        return new PageItem().new PageBuilder();
    }

    public class PageBuilder {


        private PageBuilder() {
        }

        public PageBuilder active(boolean active) {
            PageItem.this.active = active;
            return this;
        }

        public PageBuilder index(int index) {
            PageItem.this.index = index;
            return this;

        }

        public PageBuilder pageItemType(PageItemType pageItemType) {
            PageItem.this.pageItemType = pageItemType;
            return this;

        }

        public PageItem build() {
            return PageItem.this;
        }

    }


    private PageItem() {
    }

    public PageItemType getPageItemType() {
        return pageItemType;
    }


    public int getIndex() {
        return index;
    }


    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "PageItem{" +
                "pageItemType=" + pageItemType +
                ", index=" + index +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageItem pageItem = (PageItem) o;
        return index == pageItem.index && active == pageItem.active && pageItemType == pageItem.pageItemType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageItemType, index, active);
    }
}

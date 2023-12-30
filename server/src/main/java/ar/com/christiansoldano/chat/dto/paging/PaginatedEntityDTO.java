package ar.com.christiansoldano.chat.dto.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedEntityDTO<T> {
    private List<T> entities;
    private PagingDTO paging;

    private PaginatedEntityDTO(List<T> entities, PagingDTO paging) {
        this.entities = entities;
        this.paging = paging;
    }

    public static <T> PaginatedEntityDTO<T> fromPage(Page<T> page) {
        PagingDTO pagingDTO = new PagingDTO(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber() + 1
        );

        return new PaginatedEntityDTO<>(page.getContent(), pagingDTO);
    }

    public List<T> getEntities() {
        return entities;
    }

    public PagingDTO getPaging() {
        return paging;
    }

    private static class PagingDTO {
        private long total;
        private int pages;

        @JsonProperty("page_size")
        private int pageSize;

        @JsonProperty("current_page")
        private int currentPage;

        public PagingDTO(long total, int pages, int pageSize, int currentPage) {
            this.total = total;
            this.pages = pages;
            this.pageSize = pageSize;
            this.currentPage = currentPage;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }
    }
}

package ar.com.christiansoldano.chat.util;

import org.springframework.data.domain.PageRequest;

public class Paging {

    public static PageRequest of(int page, int size) {
        validPage(page);
        validSize(size);

        return PageRequest.of(page - 1, size);
    }

    private static void validPage(int page) {
        if (page <= 0) {
            throw new IllegalArgumentException("Page must be > 0");
        }
    }

    private static void validSize(int size) {
        if (size <= 0 || size > 20) {
            throw new IllegalArgumentException("Size must be between 1 and 20 (inclusive)");
        }
    }
}
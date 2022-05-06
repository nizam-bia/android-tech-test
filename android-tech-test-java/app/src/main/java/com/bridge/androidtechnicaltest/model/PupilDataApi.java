package com.bridge.androidtechnicaltest.model;

import java.util.List;

public class PupilDataApi {

    private List<PupilDetailApi> items;

    public List<PupilDetailApi> getItems() {
        return items;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    private int pageNumber;

    private int itemCount;

    private int totalPages;

    public PupilDataApi(List<PupilDetailApi> pupilDetailApis, int itemCount, int pageNumber, int totalPages) {
        this.items = pupilDetailApis;
        this.itemCount = itemCount;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
    }
}

package me.wjz.creeperhub.sqlfilter;

import me.wjz.creeperhub.constant.ErrorType;
import me.wjz.creeperhub.exception.CreeperException;

public class PostFilter {
    private Long categoryId;
    private final String sortField = "create_time";
    private final boolean desc = true;
    private int page;
    private int size;

    @Override
    public String toString() {
        return String.format("category=%s_page=%d_size=%d_sort=%s_desc=%s",
                categoryId, page, size, sortField, desc);
    }

    public void validate(){
        if(size>50) throw new CreeperException(ErrorType.MAX_ITEMS_PER_PAGE_EXCEEDED);
    }
}

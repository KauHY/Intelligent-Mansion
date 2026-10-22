package com.crlink.smartpark;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

/***
 * 查询参数基类
 */
@Data
@Accessors(chain = true)
public class BaseQuery<T> {

    protected static final ThreadLocal<Page> PAGE = new ThreadLocal<Page>();
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 页码
     */
    private int pageNum = 1;
    /**
     * 每页大小
     */
    private int pageSize = 20;

    @ApiModelProperty(value = "参数")
    private T param;

    public void startPage() {
        Page page = PageHelper.startPage(this.pageNum, this.pageSize);
        PAGE.set(page);
    }

    public <T> PageInfo<T> createPageInfo(List<T> list) {
        PageInfo pageInfo = new PageInfo(list);
        Page page = PAGE.get();
        pageInfo.setTotal(Optional.ofNullable(page).map(Page::getTotal).orElse(0L));
        completePageInfo(Optional.ofNullable(page).orElse(new Page()), pageInfo);
        PAGE.remove();
        return pageInfo;
    }

    private void completePageInfo(Page page, PageInfo pageInfo) {
        this.pageNum = page.getPageNum();
        this.pageSize = page.getPageSize();

        pageInfo.setPages(page.getPages());
        pageInfo.setSize(page.size());
        pageInfo.setSize(page.size());
        //由于结果是>startRow的，所以实际的需要+1
        if (pageInfo.getSize() == 0) {
            pageInfo.setStartRow(0);
            pageInfo.setEndRow(0);

        } else {
            pageInfo.setStartRow(page.getStartRow() + 1);
            //this.startRow = page.getStartRow() + 1;
            //计算实际的endRow（最后一页的时候特殊）
            pageInfo.setEndRow(pageInfo.getStartRow() - 1 + pageInfo.getSize());
        }
        pageInfo.calcByNavigatePages(PageInfo.DEFAULT_NAVIGATE_PAGES);
    }

}

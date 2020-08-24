package com.ner.common.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ner.utils.HttpContextUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述：默认的分页参数创建
 *
 * @author jll
 * @date 2019/4/8 11:40
 */
public class PageFactory {

    /**
     * 功能描述：table的分页参数
     *
     * @param
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     * @author jll
     * @date 2019/4/8 11:40
     */
    public static Page defaultPage() {
        HttpServletRequest request = HttpContextUtil.getRequest();

        //每页多少条数据
        String lt = request.getParameter("pageSize");
        if (StringUtils.isEmpty(lt)) {
            lt = "20";
        }
        int limit = Integer.valueOf(lt);

        //第几页
        String pg = request.getParameter("currentPage");
        if (StringUtils.isEmpty(pg)) {
            pg = "1";
        }
        int page = Integer.valueOf(pg);

        return new Page(page, limit);
    }

    /**
     * 功能描述：创建树型响应参数
     *
     * @param page
     * @return com.ner.core.page.PageInfo
     * @author jll
     * @date 2019/4/8 11:40
     */
    public static PageInfo createPageInfo(IPage page) {
        PageInfo result = new PageInfo();
        result.setSuccess(true);
        if (page.getTotal() > 0) {
            result.setMessage("查询成功");
        } else {
            result.setMessage("暂无数据");
        }
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}

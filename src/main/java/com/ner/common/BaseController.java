package com.ner.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ner.common.page.PageFactory;
import com.ner.common.page.PageInfo;
import com.ner.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 增删改差基础Controller
 *
 * @param <S>  Service类型
 * @param <M>  Mapper类型
 * @param <T>  增删改差的对象类型
 * @param <PK> id的类型，主键的数据类型
 */
@Slf4j
public class BaseController<S extends ServiceImpl<M, T>, M extends BaseMapper<T>, T, PK extends Serializable> {

    @Autowired
    protected S service;
    @Autowired
    protected M mapper;

    protected HttpServletRequest request;
    protected static ApiResult SUCCESS_TIP = ApiResult.ok();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringEscapeEditor());
        binder.registerCustomEditor(String[].class, new StringEscapeEditor());
    }

    /**
     * 功能描述：获取所有数据
     *
     * @param
     * @return java.utils.List<Entity>
     * @author jll
     * @date 2019/4/1 9:33
     */
    @RequestMapping(value = "/whole", method = RequestMethod.GET)
    @ResponseBody
    public List<T> all() {
        return service.list();
    }

    /**
     * 功能描述：分页获取数据
     *
     * @param params
     * @return com.ner.core.page.PageInfo<Entity>
     * @author jll
     * @date 2019/4/2 11:17
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<T> list(@RequestParam Map<String, String> params) {
        //查询列表数据
        Page<T> page = PageFactory.defaultPage();
        QueryWrapper<T> queryWrapper = new QueryWrapper();

        params.remove("currentPage");
        params.remove("pageSize");
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                String key = StringHelper.underscoreName(entry.getKey());
                queryWrapper.and(wrapper -> wrapper.like(key, entry.getValue()));
            }
        }

        IPage result = service.page(page, queryWrapper);
        return PageFactory.createPageInfo(result);
    }

    /**
     * 功能描述：查询单个对象
     *
     * @param id
     * @return com.home.common.model.ApiResult<Entity>
     * @author jll
     * @date 2019/4/1 9:32
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult<T> get(@PathVariable PK id) {
        Object o = service.getById(id);
        return ApiResult.okData((T) o);
    }

    /**
     * 增
     **/
    @PostMapping("/add")
    public ApiResult add(@RequestBody @Valid T entity, BeanPropertyBindingResult result) {
        if (result.hasErrors()) {
            return ApiResult.errMessage("参数有误！");
        }
        try {
            service.save(entity);
            return ApiResult.okData(entity);
        } catch (Exception e) {
            log.info("新增失败===", e);
            return ApiResult.errMessage("新增失败！");
        }
    }

    /**
     * 删
     **/
    @DeleteMapping("/del/{id}")
    public ApiResult del(@PathVariable("id") PK id) {
        try {
            service.removeById(id);
            return ApiResult.ok();
        } catch (Exception e) {
            log.info("删除失败===", e.getMessage());
            return ApiResult.errMessage("删除失败！");
        }
    }

    /**
     * 改
     **/
    @PutMapping("/upd")
    public ApiResult upd(@RequestBody @Valid T entity, BeanPropertyBindingResult result) throws InterruptedException {
        if (result.hasErrors()) {
            return ApiResult.errMessage("参数有误！");
        }
        try {
            service.updateById(entity);
            return ApiResult.okData(entity);
        } catch (Exception e) {
            log.info("更新失败===", e);
            return ApiResult.errMessage("更新失败！");
        }
    }

}
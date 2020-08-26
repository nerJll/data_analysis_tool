package com.ner.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ner.common.exception.BizException;
import com.ner.entity.Stu;
import com.ner.mapper.StuMapper;
import com.ner.service.StuService;
import org.springframework.stereotype.Service;

/**
 * @autor jiangll
 * @date 2020/8/23
 */
@Service
public class StuServiceImpl extends ServiceImpl<StuMapper, Stu> implements StuService {
    @Override
    public Long updStu(Stu stu) {
        synchronized (this) {
            if (null == stu || null == stu.getId()) {
                throw new BizException("参数不全");
            }
            Stu oldStu = baseMapper.selectById(stu.getId());
            if (oldStu == null) {
                throw new BizException("学生不存在或已被删除");
            }
            baseMapper.updateById(stu);
            return stu.getId();
        }
    }
}

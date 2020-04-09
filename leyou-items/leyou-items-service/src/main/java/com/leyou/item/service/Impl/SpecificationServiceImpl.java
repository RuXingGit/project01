package com.leyou.item.service.Impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.api.SpecificationService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-28 16:35
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;


    // 根据分类id查询分类参数组
    @Override
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup record=new SpecGroup();
        record.setCid(cid);
        return specGroupMapper.select(record);
    }

    // 根据组id查询规格参数
    @Override
    public List<SpecParam> queryParamsByGroupId(Long gid,Long cid,Boolean generic,Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return specParamMapper.select(record);
    }

    @Override
    public List<SpecGroup> queryGroupWithParamByCid(Long cid) {
        List<SpecGroup> groups = queryGroupsByCid(cid);
        groups.forEach(group->{
            List<SpecParam> params = queryParamsByGroupId(
                    group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
    }
}

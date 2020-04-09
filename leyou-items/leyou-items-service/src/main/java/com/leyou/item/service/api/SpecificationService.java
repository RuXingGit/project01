package com.leyou.item.service.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-28 16:34
 */
public interface SpecificationService {
    List<SpecGroup> queryGroupsByCid(Long cid);

    List<SpecParam> queryParamsByGroupId(Long gid,Long cid,Boolean generic,Boolean searching);

    List<SpecGroup> queryGroupWithParamByCid(Long cid);
}

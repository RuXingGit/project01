package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *
 * 继承接口可以使用通用的查询方法
 *      Mapper  通用查询方式
 *      SelectByIdListMapper    提供通过id集合查询的方法
 * @author RuXing
 * @create 2020-03-27 12:16
 */
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
}

package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 13:42
 */
public interface BrandMapper extends Mapper<Brand> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid,@Param("bid") Long bid);

    @Select("select b.id,name,image,letter from tb_brand b inner join tb_category_brand c on b.id=c.brand_id where c.category_id = #{cid}")
    List<Brand> selectBrandsByCid(@Param("cid")Long cid);
}

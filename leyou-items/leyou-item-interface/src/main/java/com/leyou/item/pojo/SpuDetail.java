package com.leyou.item.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用来记录SPU的详细信息
 * 因为详细信息数据量比较大，会影响数据库的存取速度
 * 所以单独建表保存，方便以后优化
 * @author RuXing
 * @create 2020-03-28 17:29
 */
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;
    private String description;         // 商品描述信息
    private String specialSpec;         // 商品特殊规格的名称和可选项的模板
    private String genericSpec;         // 商品的全局规格属性
    private String packingList;         // 包装清单
    private String afterService;        // 售后服务

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }
}

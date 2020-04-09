package com.leyou.item.vo;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * 用来扩展Spu，添加用来在页面渲染的属性
 * @author RuXing
 * @create 2020-03-28 17:44
 */
public class SpuVO extends Spu {

    // 分类名称（Spu的各种分类信息）
    private String cname;

    // 品牌名称（那家公司的货）
    private String bname;

    // spu详细信息
    private SpuDetail spuDetail;

    // sku集合
    private List<Sku> skus;

    public SpuVO() {
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }



    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}

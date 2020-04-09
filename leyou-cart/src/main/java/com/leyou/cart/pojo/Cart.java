package com.leyou.cart.pojo;

/**
 * @author RuXing
 * @create 2020-04-05 11:22
 */
public class Cart {
    // 用户id
    private Long userId;
    // skuId
    private Long skuId;
    // 标题
    private String title;
    // 图片
    private String image;
    // 单价
    private Long price;
    // 购买数量
    private Integer num;
    // sku的特殊规格参数
    private String ownSpec;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }
}

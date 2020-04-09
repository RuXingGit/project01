package com.leyou.item.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用来保存品牌信息
 * @author RuXing
 * @create 2020-03-27 13:38
 */
@Table(name = "tb_brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 品牌名称
    private String name;
    // 品牌logo图片
    private String image;
    // 品牌首字母
    private Character letter;

    public Brand() {
    }

    public Brand(String name, String image, Character letter) {
        this.name = name;
        this.image = image;
        this.letter = letter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }
}

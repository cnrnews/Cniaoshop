package cnaio.imooc.com.cniao5.model;

import java.io.Serializable;

/**
 * 热卖
 * @author：lihl on 2017/11/20 21:28
 * @email：1601796593@qq.com
 */
public class Wares implements Serializable {


    /**
     * imgUrl : http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_54b78bf0N24c00fc2.jpg
     * id : 10
     * campaignId : 1
     * price : 42.9
     * sale : 8442
     * name : 金士顿（Kingston）DTM30R 16GB USB3.0 精致炫薄金属U盘
     * categoryId : 5
     */

    private String imgUrl;
    private int id;
    private int campaignId;
    private double price;
    private int sale;
    private String name;
    private int categoryId;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}

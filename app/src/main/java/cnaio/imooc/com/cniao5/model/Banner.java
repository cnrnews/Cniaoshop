package cnaio.imooc.com.cniao5.model;

/**
 * @author：lihl on 2017/11/19 18:30
 * @email：1601796593@qq.com
 */
public class Banner {

    /**
     * id : 3
     * name : IT生活
     * imgUrl : http://7mno4h.com2.z0.glb.qiniucdn.com/5608cae6Nbb1a39f9.jpg
     * type : 1
     */

    private int id;
    private String name;
    private String imgUrl;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

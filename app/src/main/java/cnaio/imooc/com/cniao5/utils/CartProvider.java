package cnaio.imooc.com.cniao5.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cnaio.imooc.com.cniao5.model.ShoppingCart;
import cnaio.imooc.com.cniao5.model.Wares;

/**
 * 购物车存储类
 *
 * @author：lihl on 2017/11/24 21:28
 * @email：1601796593@qq.com
 */
public class CartProvider {

    private static final java.lang.String CART_JSON = "cart_json";
    private SparseArray<ShoppingCart> sparseArray;
    private Context mContext;

    public CartProvider(Context context) {
        mContext = context;
        sparseArray = new SparseArray(10);
        listToSparser();
    }

    /**
     * 添加购物车
     *
     * @param shoppingCart
     */
    public void put(ShoppingCart shoppingCart) {
        ShoppingCart tmp = (ShoppingCart) sparseArray.get(shoppingCart.getId());
        if (null != tmp) {
            tmp.setCount(tmp.getCount() + 1);
        } else {
            tmp = shoppingCart;
            tmp.setCount(1);
        }
        sparseArray.put(shoppingCart.getId(), tmp);
        commit();
    }
    public void put(Wares wares) {
       ShoppingCart shoppingCart=covertShoppingCart(wares);
       put(shoppingCart);
    }
    public ShoppingCart covertShoppingCart(Wares wares)
    {
        ShoppingCart shoppingCart=new ShoppingCart();

        shoppingCart.setId(wares.getId());
        shoppingCart.setCampaignId(wares.getCampaignId());
        shoppingCart.setCategoryId(wares.getCategoryId());
        shoppingCart.setImgUrl(wares.getImgUrl());
        shoppingCart.setName(wares.getName());
        shoppingCart.setPrice( wares.getPrice());
        shoppingCart.setSale(wares.getSale());
        return shoppingCart;
    }
    /**
     * 更改
     *
     * @param shoppingCart
     */
    public void update(ShoppingCart shoppingCart) {
        sparseArray.put(shoppingCart.getId(), shoppingCart);
        commit();
    }

    /***
     * 删除
     * @param shoppingCart
     */
    public void delete(ShoppingCart shoppingCart) {
        if (sparseArray.get(shoppingCart.getId())!=null)
        {
            sparseArray.delete(shoppingCart.getId());
            commit();
        }
    }

    public List<ShoppingCart> getAll() {
        return getDataFormat();
    }

    public List<ShoppingCart> getDataFormat() {
        List<ShoppingCart> shoppingCartList = null;
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        if (json != null) {
            shoppingCartList = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return shoppingCartList;
    }

    /***
     * 存储购物车集合
     */
    public void commit() {
        List<ShoppingCart> carts = spartToList();
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts));
    }

    public List<ShoppingCart> spartToList() {
        List<ShoppingCart> list = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            list.add(sparseArray.valueAt(i));
        }
        return list;
    }
    public void listToSparser() {
        List<ShoppingCart> shoppingCartList = getDataFormat();
        if (shoppingCartList != null && shoppingCartList.size() > 0) {
            for (ShoppingCart shoppingCart : shoppingCartList) {
                sparseArray.put(shoppingCart.getId(), shoppingCart);
            }
        }
    }

}

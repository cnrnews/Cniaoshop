package cnaio.imooc.com.cniao5.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.model.Address;

/**
 * 作者:candy
 * 创建时间:2017/12/3 8:14
 * 邮箱:1601796593@qq.com
 * 功能描述:
 * 收货地址管理适配器
 **/
public class AddressAdapter extends CommonAdapter<Address> {
    private AddressLisneter lisneter;

    public AddressAdapter(Context context, int layoutId, List<Address> datas, AddressLisneter lisneter) {
        super(context, layoutId, datas);
        this.lisneter = lisneter;
    }

    @Override
    protected void convert(ViewHolder holder, final Address item, final int position) {
        holder.setText(R.id.txt_name, item.getConsignee());
        holder.setText(R.id.txt_phone, replacePhoneNum(item.getPhone()));
        holder.setText(R.id.txt_address, item.getAddr());
        holder.setOnClickListener(R.id.txt_edit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lisneter!=null)
                {
                    lisneter.updateAddress(item);
                }
            }
        });
        holder.setOnClickListener(R.id.txt_del, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lisneter!=null)
                {
                    lisneter.delAddress(item);
                }
            }
        });

        final CheckBox checkBox = holder.getView(R.id.cb_is_defualt);

        final boolean isDefault = item.getIsDefault();
        checkBox.setChecked(isDefault);
        if (isDefault) {
            checkBox.setText("默认地址");
        } else {
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked && lisneter != null) {
                        item.setIsDefault(true);
                        lisneter.setDefault(item);
                    }
                }
            });
        }
    }
    public String replacePhoneNum(String phone) {

        return phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
    }
    public interface AddressLisneter {
        public void setDefault(Address address);
        public void delAddress(Address address);
        public void updateAddress(Address address);
    }
}

package com.hclz.client.kitchen.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.adapter.SanmiAdapter;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.bean.Cart;
import com.hclz.client.base.bean.Product;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.view.RoundImageView;
import com.hclz.client.base.view.TextViewDialog;
import com.hclz.client.base.view.TextViewDialog.onDialogClickListener;
import com.hclz.client.mall.bean.MallType;

import java.util.ArrayList;

public class ProductAdapter extends SanmiAdapter {
    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;
    WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 11) {//

                return true;
            } else if (msg.what == 0X101) {
                return true;
            }
            return true;
        }
    });
    private LayoutInflater inflater;
    private ArrayList<MallType.Type1Entity.Type2Entity> list;
    private TypeAdapter typeAdapter;
    private int priceNum = 4;//	[成本价0, 城代采购价1, 厨房采购价2, (在厨房吃)半成品价3,(带回家做)堂食价4]
    private ListView mListView;
    private OnAddToCartListener mOnAddToCartListener;
    private Cart cart;
    private String type;
    private String cid;
    private int eatType;
    private long lastTip = 0;

    public ProductAdapter(Context mContext, ArrayList<MallType.Type1Entity.Type2Entity> list, int priceNum, TypeAdapter typeAdapter) {
        super(mContext);
        this.mContext = mContext;
        this.list = list;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.priceNum = priceNum;
        this.typeAdapter = typeAdapter;
    }

    @Override
    public boolean isEmpty() {
        if (list == null || list != null && list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getCount() {
        if (list.size() > 0) {
            int count = 0;
            for (MallType.Type1Entity.Type2Entity type : list) {
                count += type.getItemCount();
            }
            return count;
        } else {
            return VIEWTYPE_EMPTY;
        }
    }

    @Override
    public Object getItem(int position) {
        if (null == list || position < 0 || position > getCount()) {
            return null;
        }
        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = 0;

        for (MallType.Type1Entity.Type2Entity type : list) {
            int size = type.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return type.getItem(categoryIndex);
            }

            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }
        return list != null ? list.get(position) : null;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == list || position < 0 || position > getCount()) {
            return TYPE_ITEM;
        }
        int categroyFirstIndex = 0;

        for (MallType.Type1Entity.Type2Entity type : list) {
            int size = type.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == 0) {
                return TYPE_CATEGORY_ITEM;
            }
            categroyFirstIndex += size;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        int itemViewType = getItemViewType(position);
        View v = convertView;
        switch (itemViewType) {
            case TYPE_CATEGORY_ITEM:
                if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
                    v = inflater.inflate(R.layout.common_item_header, null);
                }
                TextView textView = (TextView) v.findViewById(R.id.header);
                String itemValue = (String) getItem(position);
                textView.setText(itemValue);
                break;
            case TYPE_ITEM:
                final Product product = (Product) getItem(position);
                final ViewHolder vh;
                if (v == null || v.getTag(R.id.VIEWTYPE_NORMAL) == null) {
                    v = inflater.inflate(R.layout.item_product_list, null);
                    vh = new ViewHolder();
                    vh.txt_name = (TextView) v.findViewById(R.id.txt_name);
                    vh.txt_dshop2cshop = (TextView) v.findViewById(R.id.txt_dshop2cshop);
                    vh.txt_cshop2user = (TextView) v.findViewById(R.id.txt_cshop2user);
                    vh.txt_name_append = (TextView) v.findViewById(R.id.txt_name_append);
                    vh.txt_price = (TextView) v.findViewById(R.id.txt_price);
                    vh.txt_num = (TextView) v.findViewById(R.id.txt_num);
                    vh.txt_people_like = (TextView) v.findViewById(R.id.txt_people_like);

                    vh.img_logo = (RoundImageView) v.findViewById(R.id.img_logo);
                    vh.iv_add = (ImageView) v.findViewById(R.id.iv_add);
                    vh.iv_del = (ImageView) v.findViewById(R.id.iv_del);
                    vh.txt_account = (TextView) v.findViewById(R.id.txt_account);
                    vh.ll_huodong_list = (LinearLayout) v.findViewById(R.id.ll_huodong_list);
                    vh.lv_huodong = (ListView) v.findViewById(R.id.lv_huodong);

                    vh.ll_product = (LinearLayout) v.findViewById(R.id.ll_product);
                    v.setTag(R.id.VIEWTYPE_NORMAL, vh);
                } else {
                    vh = (ViewHolder) v.getTag(R.id.VIEWTYPE_NORMAL);
                }
                vh.txt_name.setText(product.getName());
                vh.txt_name_append.setText(product.getName_append());
                if (type.equals("cshopmall")) {
                    vh.txt_num.setText(mContext.getString(R.string.product_num, product.getInventory()));
                } else if (type.equals("dshopmall")) {
                    vh.txt_num.setVisibility(View.GONE);
                }
                vh.txt_price.setText("¥" + CommonUtil.getMoney(product.getPrice()[priceNum]));
                if (priceNum == 1) {//城代
                    vh.txt_dshop2cshop.setVisibility(View.VISIBLE);
                    vh.txt_cshop2user.setVisibility(View.VISIBLE);
                    vh.txt_dshop2cshop.setText("合伙人价格:¥" + CommonUtil.getMoney(product.getPrice()[priceNum + 1]) +
                            ",您盈利" + (product.getPrice()[priceNum + 1] - product.getPrice()[priceNum]) * 100 / product.getPrice()[priceNum] + "%");
                    vh.txt_cshop2user.setText("终端价格:¥" + CommonUtil.getMoney(product.getPrice()[priceNum + 2]) +
                            ",合伙人盈利" + (product.getPrice()[priceNum + 2] - product.getPrice()[priceNum + 1]) * 100 / product.getPrice()[priceNum + 1] + "%");
                } else if (priceNum == 2) {//合伙人
                    vh.txt_dshop2cshop.setVisibility(View.GONE);
                    vh.txt_cshop2user.setVisibility(View.VISIBLE);
                    vh.txt_cshop2user.setText("终端价格:¥" + CommonUtil.getMoney(product.getPrice()[priceNum + 1]) +
                            ",您盈利" + (product.getPrice()[priceNum + 1] - product.getPrice()[priceNum]) * 100 / product.getPrice()[priceNum] + "%");
                } else {//普通用户
                    vh.txt_dshop2cshop.setVisibility(View.GONE);
                    vh.txt_cshop2user.setVisibility(View.GONE);
                }
                vh.txt_people_like.setText(mContext.getString(R.string.product_like, product.getPraise()));

                vh.iv_add.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        cart = HclzApplication.getCart();
                        //1.判断购物车是新建状态，初始化购物车并开始添加商品到购物车
                        if (cart.getCartType() == null || TextUtils.isEmpty(cart.getCartType())) {
                            cart.clear();
                            cart.setCartType(type);
                            cart.setKitchenId(cid);
                            cart.setEatType(eatType);

                            int count = product.getCount();
                            count++;
                            product.setCount(count);
                            if (count == 1) {
                                vh.txt_account.setVisibility(View.VISIBLE);
                                vh.iv_del.setVisibility(View.VISIBLE);
                            }
                            vh.txt_account.setText(product.getCount() + "");
                            if (count >= product.getInventory()) {
                                vh.iv_add.setImageResource(R.mipmap.btn_add);
                                vh.iv_add.setClickable(false);
                            } else {
                                vh.iv_add.setImageResource(R.mipmap.btn_add_pre);
                                vh.iv_add.setClickable(true);
                            }
                            typeAdapter.notifyDataSetChanged();

                            int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                            v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                            if (mOnAddToCartListener != null) {
                                mOnAddToCartListener.onAddToCart(startLocation, product);
                            }
                            return;
                        }
                        StringBuilder typeErrorTips = null;
                        //2.判断购物车类型与当前购物车类型不一致，提示用户会清空
                        if (!cart.getCartType().equals(type)) {
                            typeErrorTips = new StringBuilder();
                            typeErrorTips.append("该操作会清空");
                            typeErrorTips.append(!cart.getCartType().equals("cshopmall") ? cart.getCartType().equals("dshophaiwai") ? "海外精品" : "好吃懒做" : "社区厨房");
                            typeErrorTips.append("，确认吗？");
                        }
                        //3.判断购物车类型是社区厨房
                        if (cart.getCartType().equals("cshopmall")) {
                            //3-1.判断购物车b不是当前厨房的，提示用户会清空
                            if (!cart.getKitchenId().equals(cid)) {
                                // 弹出提示将清除其它厨房购物车
                                typeErrorTips = new StringBuilder();
                                typeErrorTips.append("该操作会清空其它厨房购物车，确认吗？");
                            }
                            //3-2.判断购物车成品、半成品类型,提示清空
                            if (cart.getEatType() != eatType) {
                                // 弹出提示将删除（本页面另一个购物车）
                                // 弹出提示将清除商城购物车1在厨房吃2带回家做
                                typeErrorTips = new StringBuilder();
                                typeErrorTips.append("该操作会清空");
                                typeErrorTips.append(eatType == 1 ? "海外精品" : "在厨房吃");
                                typeErrorTips.append("，确认吗？");
                            }
                        }
                        long thisTip = System.currentTimeMillis();
                        // 弹出提示将清除商城购物车
                        if (typeErrorTips != null && !TextUtils.isEmpty(typeErrorTips.toString()) && thisTip - lastTip > 1000) {
                            TextViewDialog dialog = new TextViewDialog(mContext, typeErrorTips.toString(), handler);
                            dialog.setDialogClickListener(new onDialogClickListener() {
                                @Override
                                public void onSureClick() {
                                    cart.clear();
                                    cart.setCartType(type);
                                    cart.setKitchenId(cid);
                                    cart.setEatType(eatType);

                                    int count = product.getCount();
                                    count++;
                                    product.setCount(count);
                                    if (count == 1) {
                                        vh.txt_account.setVisibility(View.VISIBLE);
                                        vh.iv_del.setVisibility(View.VISIBLE);
                                    }
                                    vh.txt_account.setText(product.getCount() + "");
                                    if (count >= product.getInventory()) {
                                        vh.iv_add.setImageResource(R.mipmap.btn_add);
                                        vh.iv_add.setClickable(false);
                                    } else {
                                        vh.iv_add.setImageResource(R.mipmap.btn_add_pre);
                                        vh.iv_add.setClickable(true);
                                    }
                                    typeAdapter.notifyDataSetChanged();

                                    int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                                    v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                                    if (mOnAddToCartListener != null) {
                                        mOnAddToCartListener.onAddToCart(startLocation, product);
                                    }
                                }

                                @Override
                                public void onCancelClick() {

                                }
                            });
                            if (thisTip - lastTip > 1000) {
                                dialog.showDialog();
                            }
                            lastTip = thisTip;
                        } else {
                            int count = product.getCount();
                            count++;
                            product.setCount(count);
                            if (count == 1) {
                                vh.txt_account.setVisibility(View.VISIBLE);
                                vh.iv_del.setVisibility(View.VISIBLE);
                            }
                            vh.txt_account.setText(product.getCount() + "");
                            if (count >= product.getInventory()) {
                                vh.iv_add.setImageResource(R.mipmap.btn_add);
                                vh.iv_add.setClickable(false);
                            } else {
                                vh.iv_add.setImageResource(R.mipmap.btn_add_pre);
                                vh.iv_add.setClickable(true);
                            }
                            typeAdapter.notifyDataSetChanged();

                            int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                            v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                            if (mOnAddToCartListener != null) {
                                mOnAddToCartListener.onAddToCart(startLocation, product);
                            }
                        }
                    }
                });
                vh.iv_del.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = product.getCount();
                        count--;
                        product.setCount(count);
                        if (count < product.getInventory()) {
                            vh.iv_add.setImageResource(R.mipmap.btn_add_pre);
                            vh.iv_add.setClickable(true);
                        } else {
                            vh.iv_add.setImageResource(R.mipmap.btn_add);
                            vh.iv_add.setClickable(false);
                        }
                        if (count <= 0) {
                            vh.txt_account.setVisibility(View.GONE);
                            vh.iv_del.setVisibility(View.GONE);
                        }
                        vh.txt_account.setText(product.getCount() + "");
                        typeAdapter.notifyDataSetChanged();
                        if (mOnAddToCartListener != null) {
                            mOnAddToCartListener.onDelFromCart(product);
                        }
                    }
                });

                if (product.getCount() == 0) {
                    vh.iv_del.setVisibility(View.GONE);
                    vh.txt_account.setVisibility(View.GONE);
                    if (product.getCount() >= product.getInventory()) {
                        vh.iv_add.setImageResource(R.mipmap.btn_add);
                        vh.iv_add.setClickable(false);
                    } else {
                        vh.iv_add.setImageResource(R.mipmap.btn_add_pre);
                        vh.iv_add.setClickable(true);
                    }
                } else {
                    vh.txt_account.setText(product.getCount() + "");
                    vh.iv_del.setVisibility(View.VISIBLE);
                    vh.txt_account.setVisibility(View.VISIBLE);
                    if (product.getCount() >= product.getInventory()) {
                        vh.iv_add.setImageResource(R.mipmap.btn_add);
                        vh.iv_add.setClickable(false);
                    } else {
                        vh.iv_add.setImageResource(R.mipmap.btn_add_pre);
                        vh.iv_add.setClickable(true);
                    }
                }
                if (product.getPromotions() != null) {
                    vh.ll_huodong_list.setVisibility(View.VISIBLE);
                    HuoDongAdapter adapter = new HuoDongAdapter(mContext, product.getPromotions());
                    vh.lv_huodong.setAdapter(adapter);
                } else {
                    vh.ll_huodong_list.setVisibility(View.GONE);
                }
                ImageUtility.getInstance(mContext).showImage(
                        product.getAlbum_thumbnail()[0], vh.img_logo);
                vh.img_logo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnAddToCartListener != null) {
                            mOnAddToCartListener.gotoProductDetail(product);
                        }
                    }
                });
                return v;
        }
        return v;
    }

    /**
     * 获取该分类名所在位置
     * 方法说明：
     *
     * @param name
     * @return
     */
    public int getPosition(String name) {
        int categroyFirstIndex = 0;
        for (MallType.Type1Entity.Type2Entity type : list) {
            if (type.getName().equals(name)) {
                break;
            } else {
                int size = type.getItemCount();
                categroyFirstIndex += size;
            }
        }
        return categroyFirstIndex;
    }

    /**
     * 获取该商品id所在位置
     * 方法说明：
     *
     * @param pid
     * @return
     */
    public int getProductPosition(String pid) {
        int categroyFirstIndex = 0;
        for (MallType.Type1Entity.Type2Entity type : list) {
            categroyFirstIndex += 1;
            for (Product product : type.getProducts()) {
                if (product.getPid().equals(pid)) {
                    return categroyFirstIndex;
                }
                categroyFirstIndex += 1;
            }
        }
        return 0;
    }

    public void setmOnAddToCartListener(OnAddToCartListener mOnAddToCartListener) {
        this.mOnAddToCartListener = mOnAddToCartListener;
    }

    public void initCart(String type, String cid, int eatType) {
        this.type = type;
        this.cid = cid;
        this.eatType = eatType;
    }

    public void setmListView(ListView mListView) {
        this.mListView = mListView;
    }

    public void clearProductCount() {
        for (MallType.Type1Entity.Type2Entity type : list) {
            for (Product pro : type.getProducts()) {
                pro.setCount(0);
            }
        }
        this.notifyDataSetChanged();
        typeAdapter.notifyDataSetChanged();
    }

    public void setProductCount(String pid, int num) {
//		for(Type type:list){
//			for(Product pro:type.getProducts()){
//				if(pid.equals(pro.getPid())){
//					pro.setCount(num);
//				}
//			}
//		}
//		this.notifyDataSetChanged();
//		typeAdapter.notifyDataSetChanged();
        int index = getProductPosition(pid);
        if (mListView == null) {
            return;
        }
        // 获取当前可以看到的item位置
        int visiblePosition = mListView.getFirstVisiblePosition();
        // 获取点击的view
        View view = mListView.getChildAt(index - visiblePosition);
        Product pro = (Product) getItem(index);
        pro.setCount(num);
        if (view != null) {
            TextView txt_account = (TextView) view.findViewById(R.id.txt_account);
            ImageView iv_del = (ImageView) view.findViewById(R.id.iv_del);
            ImageView iv_add = (ImageView) view.findViewById(R.id.iv_add);
            // 获取mDataList.set(ids, item);更新的数据
            // 重新设置界面显示数据
            txt_account.setText(num + "");
            if (num == 0) {
                txt_account.setVisibility(View.GONE);
                iv_del.setVisibility(View.GONE);
            }
            if (num >= pro.getInventory()) {
                iv_add.setImageResource(R.mipmap.btn_add);
                iv_add.setClickable(false);
            } else {
                iv_add.setImageResource(R.mipmap.btn_add_pre);
                iv_add.setClickable(true);
            }
        }
        typeAdapter.notifyDataSetChanged();
    }

    public interface OnAddToCartListener {
        //跳转到产品详情页
        void gotoProductDetail(Product product);

        //添加产品到购物车
        void onAddToCart(int[] startLocation, Product product);

        //删除购物车商品
        void onDelFromCart(Product product);
    }

    class ViewHolder {
        TextView txt_name;
        TextView txt_name_append;
        TextView txt_price;
        TextView txt_num;
        TextView txt_people_like;
        TextView txt_dshop2cshop;
        TextView txt_cshop2user;

        RoundImageView img_logo;
        ImageView iv_add, iv_del;
        TextView txt_account;

        LinearLayout ll_huodong_list;
        ListView lv_huodong;

        LinearLayout ll_product;
    }

}

package com.hclz.client.shouye.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import cn.iwgang.countdownview.CountdownView;

/**
 * 秒杀产品倒计时展示item
 * Created by handsome on 16/8/10.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    public static final String TAG = ProductAdapter.class.getSimpleName();
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    //===============屏幕宽度/屏幕高度
    protected int mScreenWidth;
    protected int mScreenHeight;
    private Activity mContext;
    private List<ProductBean.ProductsBean> mDatas = new ArrayList<>();
    private ProductListener mListener;
    private View mHeaderView;


    //================用来倒计时的组件
    private final ConcurrentHashMap<String, ProductHolder> mCountdownVHList;
    private Handler mHandler = new Handler();
    private Timer mTimer;
    private boolean isCancel = true;

    public ProductAdapter(Activity context, ProductListener listener, int screenwidth, int screenheight) {
        mContext = context;
        mListener = listener;
        mCountdownVHList = new ConcurrentHashMap<>();
        mScreenWidth = screenwidth;
        mScreenHeight = screenheight;
        startRefreshTime();
    }

    public void startRefreshTime() {
        if (!isCancel) return;

        if (null != mTimer) {
            mTimer.cancel();
        }

        isCancel = false;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mRefreshTimeRunnable);
            }
        }, 0, 10);
    }

    public void cancelRefreshTime() {
        isCancel = true;
        if (null != mTimer) {
            mTimer.cancel();
        }
        mHandler.removeCallbacks(mRefreshTimeRunnable);
    }


    public void setData(List<ProductBean.ProductsBean> datas) {
        mDatas = datas == null ? new ArrayList<ProductBean.ProductsBean>() : datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) {
            return TYPE_NORMAL;
        } else {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_NORMAL;
            }
        }
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new ProductHolder(mHeaderView);
        ProductHolder holder = new ProductHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_product, parent,
                false));
        return holder;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public boolean isHeader(int position) {
        if (mHeaderView == null) return false;
        return position == 0;
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        int pos = getRealPosition(holder);
        final ProductBean.ProductsBean item = mDatas.get(pos);
        holder.bindData(item, pos);

        // 处理倒计时
        if (item.limit_price != null && item.limit_price.count_down > 0) {
            synchronized (mCountdownVHList) {
                mCountdownVHList.put(item.pid, holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    @Override
    public void onViewRecycled(ProductHolder holder) {
        super.onViewRecycled(holder);

        ProductBean.ProductsBean curAnnounceGoodsInfo = holder.getBean();
        if (null != curAnnounceGoodsInfo && curAnnounceGoodsInfo.limit_price != null && curAnnounceGoodsInfo.limit_price.count_down > 0) {
            mCountdownVHList.remove(curAnnounceGoodsInfo.pid);
        }
    }

    private Runnable mRefreshTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCountdownVHList.size() == 0) return;

            synchronized (mCountdownVHList) {
                long currentTime = System.currentTimeMillis();
                Iterator iter = mCountdownVHList.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    ProductHolder holder = (ProductHolder) entry.getValue();
                    if (currentTime >= holder.getBean().limit_price.end_time_local) {
                        //倒计时结束
                        holder.getBean().limit_price.count_down = 0;
                        mCountdownVHList.remove(key);
                        notifyDataSetChanged();
                    } else {
                        holder.refreshTime(currentTime);
                    }
                }
            }
        }
    };

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    public interface ProductListener {
        void onClick(ProductBean.ProductsBean productsBean);

        void addCart(ProductBean.ProductsBean productsBean);
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        ImageView iv_img, iv_quehuo;
        TextView tv_name, tv_price, tv_price_previous, tv_miaosha, tv_julikaishiorjieshu;
        CountdownView tv_miaosha_shijian;
        CardView card_main;
        RelativeLayout rl_cart;
        ProductBean.ProductsBean mItem;
        LinearLayout ll_miaosha;


        public ProductHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_price_previous = (TextView) itemView.findViewById(R.id.tv_price_previous);
            tv_price_previous.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            iv_quehuo = (ImageView) itemView.findViewById(R.id.iv_quehuo);
            tv_miaosha = (TextView) itemView.findViewById(R.id.tv_miaosha);
            rl_cart = (RelativeLayout) itemView.findViewById(R.id.rl_cart);
            tv_miaosha_shijian = (CountdownView) itemView.findViewById(R.id.tv_miaosha_shijian);
            tv_julikaishiorjieshu = (TextView) itemView.findViewById(R.id.tv_julikaishiorjieshu);
            ll_miaosha = (LinearLayout) itemView.findViewById(R.id.ll_miaosha);
            card_main = (CardView) itemView.findViewById(R.id.card_main);
            card_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(mDatas.get(getRealPosition(ProductHolder.this)));
                }
            });
            rl_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductBean.ProductsBean item = mDatas.get(getRealPosition(ProductHolder.this));
                    if (item.virtual_goods == null) {
                        add(mDatas.get(getRealPosition(ProductHolder.this)));
                    } else {
                        if (item.virtual_goods.real_goods != null && item.virtual_goods.real_goods.size() > 0) {
                            for (ProductBean.ProductsBean bean : item.virtual_goods.real_goods) {
                                add(bean);
                            }
                        }
                    }
                    mListener.addCart(mDatas.get(getRealPosition(ProductHolder.this)));
                }
            });
        }

        public void bindData(ProductBean.ProductsBean itemInfo, final int pos) {
            mItem = itemInfo;
            if (itemInfo.limit_price != null) {
                tv_miaosha.setVisibility(View.VISIBLE);
                ll_miaosha.setVisibility(View.VISIBLE);
                if (itemInfo.limit_price.count_down > 0) {
                    tv_julikaishiorjieshu.setText(itemInfo.limit_price.isStarted ? "距离结束：" : "距离开始：");
                    refreshTime(System.currentTimeMillis());
                } else {
                    tv_miaosha_shijian.allShowZero();
                }
            } else {
                tv_miaosha.setVisibility(View.GONE);
                ll_miaosha.setVisibility(View.GONE);
            }

            if (pos % 2 == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int) (CommonUtil.convertDpToPixel(8, mContext)), (int) (CommonUtil.convertDpToPixel(0, mContext)),
                        (int) (CommonUtil.convertDpToPixel(4, mContext)), (int) (CommonUtil.convertDpToPixel(8, mContext)));
                card_main.setLayoutParams(params);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int) (CommonUtil.convertDpToPixel(4, mContext)), (int) (CommonUtil.convertDpToPixel(0, mContext)),
                        (int) (CommonUtil.convertDpToPixel(8, mContext)), (int) (CommonUtil.convertDpToPixel(8, mContext)));
                card_main.setLayoutParams(params);
            }
            RelativeLayout.LayoutParams imgparams = new RelativeLayout.LayoutParams(
                    mScreenWidth / 2 - (int) CommonUtil.convertDpToPixel(16, mContext), mScreenWidth / 2 - (int) CommonUtil.convertDpToPixel(16, mContext));
            iv_img.setLayoutParams(imgparams);
            iv_img.setPadding((int) CommonUtil.convertDpToPixel(24, mContext), (int) CommonUtil.convertDpToPixel(24, mContext), (int) CommonUtil.convertDpToPixel(24, mContext), (int) CommonUtil.convertDpToPixel(24, mContext));
            ImageUtility.getInstance(mContext).showImage(
                    mItem.album_thumbnail, iv_img, R.mipmap.perm_pic);
            StringBuilder sb = new StringBuilder();
            sb.append(mItem.name);
            if (mItem.name_append != null) sb.append(mItem.name_append);
            tv_name.setText(sb.toString());
            tv_price.setText(CommonUtil.getMoney(mItem.price));
            tv_price_previous.setText(CommonUtil.getMoney(mItem.normal_detail.price_market));
            if (mItem.price < mItem.normal_detail.price_market) {
                tv_price_previous.setVisibility(View.VISIBLE);
            } else {
                tv_price_previous.setVisibility(View.GONE);
            }

            if (mItem.inventory <= 0 && mItem.is_bookable == 0) {
                iv_quehuo.setVisibility(View.VISIBLE);
            } else {
                iv_quehuo.setVisibility(View.GONE);
            }
        }

        public void refreshTime(long curTimeMillis) {
            if (null == mItem || mItem.limit_price.count_down <= 0) return;

            tv_miaosha_shijian.updateShow(mItem.limit_price.end_time_local - curTimeMillis);
        }

        public ProductBean.ProductsBean getBean() {
            return mItem;
        }

        private void add(ProductBean.ProductsBean item) {
            Integer num = 0;
            if (DiandiCart.getInstance().contains(item.pid)) {
                DiandiCartItem cartItem = DiandiCart.getInstance().get(item.pid);
                num = cartItem.num;
            }
            int previous_num = num;
            if (num < item.minimal_quantity) {
                num = item.minimal_quantity;
            } else {
                num = num + item.minimal_plus;
            }

            if (num > item.inventory && item.is_bookable == 0) {
                ToastUtil.showToastCenter(mContext, "商品已被抢购一空,请选购其他商品");
                return;
            }
            DiandiCartItem newItem = new DiandiCartItem(item, num);
            DiandiCart.getInstance().put(newItem, mContext);
            ToastUtil.showToastCenter(mContext, "已加入购物袋");
        }
    }
}

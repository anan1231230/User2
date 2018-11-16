package com.hclz.client.order;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.GuideActivity;
import com.hclz.client.MainActivity;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.bean.Order;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseFragment;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.faxian.CartActivity;
import com.hclz.client.faxian.bean.ProductBean;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.order.adapter.Order2Adapter;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;

import static com.hclz.client.R.string.order;


public class OrderFragment extends BaseFragment {

    private View viWholeView;
    private TextView tvCommHeadTitle;
    private SwipeRefreshLayout mSwipe;
    private RecyclerView rvOrders;
    private LinearLayoutManager mManager;
    private Order2Adapter mAdapter;
    private LinearLayout llNullContent;
    private TextView tvLogin, tvOrderRecord;

    Snackbar mSnackbar;
    Order anotherOrder;
    private ArrayList<Order> orderList;
    private List<ProductBean.ProductsBean> plist = new ArrayList<>();
    private int mPage = 0;
    private int has_more = 1;
    Dialog redordDialog;

    public static void startMe(Context from) {
        Intent intent = new Intent(from, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment", "OrderFragment");
        from.startActivity(intent);
    }

    public static void startMeFromSelfActivity(Context from) {
        ((MainActivity) from).rdoOrder.setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viWholeView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order, container, false);
        return viWholeView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {// 当前fragment显示时
//			getOrder();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("OrderFragment");
        if (((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_ORDER) {
            showLoadingDialog();
        }
        getOrder(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("OrderFragment");
    }

    @Override
    protected void initView() {
        viWholeView.findViewById(R.id.iv_comm_head_left).setVisibility(View.GONE);
        tvCommHeadTitle = (TextView) viWholeView.findViewById(R.id.tv_comm_head_title);
        mSwipe = (SwipeRefreshLayout) viWholeView.findViewById(R.id.swipe);
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        rvOrders = (RecyclerView) viWholeView.findViewById(R.id.rv_orders);
        llNullContent = (LinearLayout) viWholeView.findViewById(R.id.ll_null_content);
        tvLogin = (TextView) viWholeView.findViewById(R.id.tv_login);
        tvOrderRecord = (TextView) viWholeView.findViewById(R.id.tv_order_record);
    }

    @Override
    protected void initInstance() {
        mManager = new LinearLayoutManager(mContext);
        mAdapter = new Order2Adapter(mContext, new Order2Adapter.OrderItemClickListener() {
            @Override
            public void onOp(final Order order, final int position, final String op, final String show) {
                if ("删除订单".equals(show)) {
                    new AlertDialog.Builder(mContext).setTitle("您确定要删除订单吗?")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    op(order, position, op);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DO NOTHING
                        }
                    }).show();
                } else if ("取消订单".equals(show)) {
                    new AlertDialog.Builder(mContext).setTitle("您确定要取消订单吗?")
                            .setPositiveButton("取消订单", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    op(order, position, op);
                                }
                            }).setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DO NOTHING
                        }
                    }).show();
                } else if ("去支付".equals(show)) {
                    SelectPayMethodActivity.startMe(mContext, order.orderid, order.payment_amount, order.mid);
                } else if ("再来一单".equals(show)) {
                    anotherOrder(order, position, op);
                } else {
                    op(order, position, op);
                }
            }

            @Override
            public void onItemSelected(Order order, int position) {
                OrderDetailActivity.startMe(mContext, order);
            }
        });
        mSnackbar = Snackbar.make(rvOrders, "加载中...", Snackbar.LENGTH_LONG);
        rvOrders.setLayoutManager(mManager);
        rvOrders.setAdapter(mAdapter);
        rvOrders.setItemAnimator(new OvershootInRightAnimator());
    }

    @Override
    protected void initData() {

        configMap = HclzApplication.getData();
    }

    @Override
    protected void setViewData() {
        tvCommHeadTitle.setText(getActivity().getResources().getString(order));

    }

    @Override
    protected void setListener() {
        tvLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startMe(mContext);
            }
        });

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SanmiConfig.isPulling = true;
                getOrder(true);
            }
        });
        rvOrders.addOnScrollListener(new EndlessRecyclerOnScrollListener(mManager) {
            @Override
            public void onLoadMore() {
                simulateLoadMoreData();
            }
        });
        tvOrderRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderRecordActivity.startMe(mContext, "历史订单", "历史订单", "");
            }
        });
    }

    private void simulateLoadMoreData() {
        loadMoreData();
    }

    private void loadMoreData() {
        getOrder(false);
    }

    private void dismissPullScrollView() {
//        mHandler.sendEmptyMessage(2222);
        if (mSwipe.isRefreshing()) {
            SanmiConfig.isPulling = false;
            mSwipe.setRefreshing(false);
        }
    }

    private void dissmissDialog() {
        handler.sendEmptyMessageDelayed(8888, 0);
    }

    private void showLoadingDialog() {
        WaitingDialogControll.showLoadingDialog(mContext);
        handler.sendEmptyMessageDelayed(8888, 20000);
    }

    private void anotherOrder(Order order, final int position, String op) {
        JSONArray jsonArray = new JSONArray();
        anotherOrder = order;
        for (int i = 0; i < order.products.size(); i++) {
            jsonArray.put(order.products.get(i).pid);
        }


        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            content = PostHttpUtil.prepareContents(configMap, mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("pids", jsonArray);
            content.put("addressid", "51303096-147650285251");
            PostHttpUtil.prepareParams(requestParams, content.toString());

            sanmiAsyncTask.setIsShowDialog(false);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ORDER_ANOTHER.getOrderMethod(),
                    requestParams,
                    new ResultHandler() {
                        @Override
                        public void callBackForServerFailed(String result) {
                            super.callBackForServerFailed(result);
                            dismissPullScrollView();
                        }

                        @Override
                        protected void callBackForGetDataFailed(
                                String result) {
                            super.callBackForGetDataFailed(result);
                            dismissPullScrollView();
                        }

                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            //staffes = JsonUtility.fromJson(obj.get("staff"), new TypeToken<List<NetStaff>>() {
                            plist = JsonUtility.fromJson(obj.get("products"), new TypeToken<List<ProductBean.ProductsBean>>() {
                            });

                            add(plist);

                        }

                        @Override
                        public boolean callBackSessionError() {
                            return false;
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void add(List<ProductBean.ProductsBean> olist) {
        int count = 0;

        for (int i = 0; i < olist.size(); i++) {
            Integer num = 0;
            if (DiandiCart.getInstance().contains(olist.get(i).pid)) {//如果购物车中有此类商品，num等于购物车数量
                DiandiCartItem cartItem = DiandiCart.getInstance().get(olist.get(i).pid);
                num = cartItem.num;
            }
            if (olist.get(i).inventory > 0) {
                if (olist.get(i).inventory > anotherOrder.products.get(i).deal_count) {
                    num += anotherOrder.products.get(i).deal_count;
                }
            }
//            Log.d("测试+",olist.get(i).name+num);
            DiandiCartItem newItem = new DiandiCartItem(olist.get(i), num);
            count += num;
            DiandiCart.getInstance().put(newItem, mContext);
        }
        if (count > 0) {
            CartActivity.startMe(mContext);
        } else {
            ToastUtil.showToast(mContext, getResources().getString(R.string.product_num_empty));
        }


    }

    private void getOrder(final boolean isRefresh) {
        if (!isRefresh) {
            mSnackbar.show();
        }
        if (!TextUtils.isEmpty(SharedPreferencesUtil
                .get(mContext, ProjectConstant.APP_USER_MID))) {
            requestParams = new HashMap<String, String>();
            JSONObject content = null;
            try {
                content = PostHttpUtil.prepareContents(configMap, mContext);
                content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                        .get(mContext, ProjectConstant.APP_USER_MID));
                content.put(ProjectConstant.APP_USER_SESSIONID,
                        SharedPreferencesUtil.get(mContext,
                                ProjectConstant.APP_USER_SESSIONID));
                if (!isRefresh && orderList != null && orderList.size() > 0) {
                    content.put("head_orderid", orderList.get(0).orderid);
                    content.put("last_page_num", mPage);
                }
                PostHttpUtil.prepareParams(requestParams, content.toString());

                sanmiAsyncTask.setIsShowDialog(false);
                sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ORDER_LIST.getOrderMethod(),
                        requestParams,
                        new ResultHandler() {
                            @Override
                            public void callBackForServerFailed(String result) {
                                super.callBackForServerFailed(result);
                                dismissPullScrollView();
                            }

                            @Override
                            protected void callBackForGetDataFailed(
                                    String result) {
                                super.callBackForGetDataFailed(result);
                                dismissPullScrollView();
                            }

                            @Override
                            public void callBackForServerSuccess(String result) {
                                JsonObject obj = JsonUtility.parse(result);
                                ArrayList<Order> olist = JsonUtility.fromJson(
                                        obj.get("orders"),
                                        new TypeToken<ArrayList<Order>>() {
                                        });
                                if (orderList == null || isRefresh) {
                                    orderList = olist;
                                } else {
                                    orderList.addAll(olist);
                                }
                                mPage = JsonUtility.fromJson(obj.get("page_num"), Integer.class);
                                has_more = JsonUtility.fromJson(obj.get("has_more"), Integer.class);
                                showContent();
                            }

                            @Override
                            public boolean callBackSessionError() {
                                getOrder(isRefresh);
                                return false;
                            }
                        });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            showNullContent();
        }
    }

    private void showNullContent() {
        dissmissDialog();
        llNullContent.setVisibility(View.VISIBLE);
        rvOrders.setVisibility(View.GONE);
        tvOrderRecord.setVisibility(View.GONE);
    }

    private void showContent() {
        showDialog();
        mAdapter.setData(orderList);
        mAdapter.notifyDataSetChanged();
        dissmissDialog();
        if (mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
        dismissPullScrollView();
        tvOrderRecord.setVisibility(View.VISIBLE);
        llNullContent.setVisibility(View.GONE);
        rvOrders.setVisibility(View.VISIBLE);
    }

    private void op(Order order, final int position, String op) {
        requestParams = new HashMap<String, String>();
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            contentObj.put("orderid", order.orderid);
            contentObj.put("op", op);
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.setIsShowDialog(true);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.ORDER_STATUS_OP.getOrderMethod(),
                    requestParams, new ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, "操作成功!");
                            getOrder(true);
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract class EndlessRecyclerOnScrollListener extends
            RecyclerView.OnScrollListener {

        int firstVisibleItem, visibleItemCount, totalItemCount;

        private LinearLayoutManager mLinearLayoutManager;

        public EndlessRecyclerOnScrollListener(
                LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (has_more == 0) {
            } else if (has_more == 1
                    && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
                onLoadMore();
            }
        }

        public abstract void onLoadMore();
    }

    private void showDialog() {
        int intAppStartUpTimes;
        String strAppStartUpTimes = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_ORDER_RECORD_TIMES);
        if (CommonUtil.isNull(strAppStartUpTimes)) {
            intAppStartUpTimes = 0;
        } else {
            intAppStartUpTimes = Integer.valueOf(strAppStartUpTimes);
        }
        // 判定项目启动次数
        if (intAppStartUpTimes <= 0) {
            SharedPreferencesUtil.save(mContext, ProjectConstant.APP_ORDER_RECORD_TIMES, "1");
            // 跳转到引导页
            showImage();
        }
    }

    private void showImage() {

        redordDialog = new Dialog(mContext, R.style.dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt, null);
        redordDialog.setContentView(view);
        LinearLayout llOrderRecord = (LinearLayout) redordDialog.findViewById(R.id.ll_order_record);
        llOrderRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                redordDialog.dismiss();
            }
        });
        redordDialog.show();
        /**
         * 设置dialog高宽
         */
        WindowManager wm = mContext.getWindowManager();

        int width = (int) (wm.getDefaultDisplay().getWidth());
        int height = (int) (wm.getDefaultDisplay().getHeight());
        WindowManager.LayoutParams params =
                redordDialog.getWindow().getAttributes();
        params.width = width;
        params.height = height;
        redordDialog.getWindow().setAttributes(params);
    }

}

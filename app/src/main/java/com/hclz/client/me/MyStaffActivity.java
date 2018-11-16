package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.me.adapter.AddressAdapter;
import com.hclz.client.me.adapter.MenuAdapter;
import com.hclz.client.me.listener.OnItemClickListener;
import com.hclz.client.me.view.ListViewDecoration;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;
import com.hclz.client.order.confirmorder.bean.staff.NetStaff;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的员工列表
 */
public class MyStaffActivity extends BaseActivity implements OnClickListener {
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private LinearLayout llAdd;
    private AddressAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String fromActivity;
    private List<NetAddress> addresses;
    private List<NetStaff> staffes = new ArrayList<>();
    MenuAdapter mMenuAdapter;
    private List<String> mDataList;

    /**
     * 页面跳转
     *
     * @param from
     * @param fromActivity 来自那个activity
     */
    public static void startMe(Context from, String fromActivity) {
        Intent intent = new Intent(from, MyStaffActivity.class);
        intent.putExtra("from", fromActivity);
        from.startActivity(intent);
    }

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from) {
        Intent intent = new Intent(from, MyStaffActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_my_staff);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllStaff();
    }

    @Override
    protected void initView() {
        llAdd = (LinearLayout) findViewById(R.id.ll_add_staff);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
//         swipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
//        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.my_staff);
        getAllStaff();
//        showContent();
        if (mIntent != null) {
            fromActivity = mIntent.getStringExtra("from");
        }
    }

    @Override
    protected void initListener() {
        llAdd.setOnClickListener(this);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        // 添加滚动监听。
        mSwipeMenuRecyclerView.addOnScrollListener(mOnScrollListener);

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_staff:
                StaffActivity.startMe(mContext, null);
                break;
            default:
                break;
        }
    }


    /**
     * 刷新监听。
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeMenuRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);

        }
    };

    /**
     * 加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。

//                Toast.makeText(RefreshLoadMoreActivity.this, "滑到最底部了，去加载更多吧！", Toast.LENGTH_SHORT).show();
//                size += 50;
//                for (int i = size - 50; i < size; i++) {
//                    mDataList.add("我是第" + i + "个。");
//                }
//                mMenuAdapter.notifyDataSetChanged();
            }
        }
    };
    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
         *                        #RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
                if (menuPosition == 0) {
                    deleteStaff(staffes.get(adapterPosition).getStaff_id());
                    staffes.remove(adapterPosition);
                    mMenuAdapter.notifyDataSetChanged();
                } else {
                    StaffActivity.startMe(mContext, staffes.get(adapterPosition));
                }
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
//                mDataList.remove(adapterPosition);
                mMenuAdapter.notifyDataSetChanged();
            }
        }
    };
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            StaffDetailActivity.startMe(mContext, staffes.get(position));

        }
    };

    private void deleteStaff(String staffId){
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);

            contentObj.put(ProjectConstant.APP_USER_MID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            JSONObject staffObj = new JSONObject();
            staffObj.put("staff_id", staffId);
            contentObj.put("staff_info", staffObj);
            String content = contentObj.toString();
            PostHttpUtil.prepareParams(requestParams, content);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_DELETE_STAFF.getUserMethod(),
                    requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, getString(R.string.success));
                        }

                        @Override
                        public void callBackStaffError() {
                            ToastUtil.showToast(mContext, "删除失败");
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getAllStaff() {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
            content = PostHttpUtil.prepareContents(configMap, mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.USER_STAFF_LIST.getUserMethod(),
                    requestParams, new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            if (obj != null) {
                                //获取地址
                                staffes = JsonUtility.fromJson(obj.get("staff"), new TypeToken<List<NetStaff>>() {
                                });
                            }
                            showContent();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void showContent() {
//        for (int q = 0; q < 20; q++) {
//            NetStaff a = new NetStaff();
//            a.setStaffName(q+"a");
//            a.setStaffPhone(q+"b");
//            a.setStaffId(q+"c");
//            a.setStaffJob(q+"d");
//            staffes.add(q, a);
//        }
        for(int i=0;i<staffes.size();i++){
            Log.d("**",staffes.size()+staffes.get(i).getStaff_name());
        }
        mMenuAdapter = new MenuAdapter((ArrayList<NetStaff>) staffes);
        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mSwipeMenuRecyclerView.setAdapter(mMenuAdapter);
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。.
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.actionbar_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {

            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.color.red)
//                        .setImage(R.mipmap.ic_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.color.btn_green_noraml)
                        .setText("修改")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。

            }
        }
    };

}

package com.merben.wangluodianhua.fragment;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.merben.wangluodianhua.R;
import com.merben.wangluodianhua.activitys.CallActivity;
import com.merben.wangluodianhua.adapters.ContactAdapter;
import com.merben.wangluodianhua.listeners.ContactClickListener;
import com.merben.wangluodianhua.slideabc.CharacterParser;
import com.merben.wangluodianhua.slideabc.PinyinComparator;
import com.merben.wangluodianhua.slideabc.SideBar;
import com.merben.wangluodianhua.util.ContactBean;
import com.merben.wangluodianhua.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment implements View.OnClickListener {
    private RecyclerView rv_contact;
    private TextView haoma, dialog;
    ;
    private ImageView call;
    private StaggeredGridLayoutManager contactLinearLayoutManager;
    private ContactAdapter contactAdapter;
    private List<ContactBean> contactBeanList;
    private ArrayList<String> xingList;
    private Map<String, ArrayList<ContactBean>> contactXingmap;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    private Map<Integer, ContactBean> contactIdMap = null;
    private ContactBean mContactBean;

    private SideBar sideBar;
    private PinyinComparator pinyinComparator;
    private CharacterParser characterParser;
    private EditText edtSearch;
    private final static int READ_CALL_CODE = 97;

    public ContactFragment() {
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        sideBar = (SideBar) rootView.findViewById(R.id.sidrbar);
        dialog = (TextView) rootView.findViewById(R.id.dialog);
        edtSearch = (EditText) rootView.findViewById(R.id.edt_search);

        rv_contact = (RecyclerView) rootView.findViewById(R.id.rv_contact);
        haoma = (TextView) rootView.findViewById(R.id.haoma);
        call = (ImageView) rootView.findViewById(R.id.call);
        contactLinearLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        contactAdapter = new ContactAdapter(getActivity(), new ContactClickListener() {
            @Override
            public void onContactClick(ContactBean contactBean) {
                mContactBean = contactBean;
                haoma.setText(contactBean.getPhoneNum());
                edtSearch.setText("");
            }
        });
        rv_contact.setLayoutManager(contactLinearLayoutManager);

        rv_contact.setAdapter(contactAdapter);
        call.setOnClickListener(this);

        // 实例化
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = contactAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
//                    rv_contact.setSelection(position);
                    rv_contact.scrollToPosition(position);
                }

            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<ContactBean> list = new ArrayList<ContactBean>();
                if (!contactBeanList.isEmpty()) {
                    for (int i = 0; i < contactBeanList.size(); i++) {
                        ContactBean contactBean = contactBeanList.get(i);
                        if (contactBean.getDesplayName().contains(s.toString())) {
                            list.add(contactBean);
                            continue;
                        }
                        if (contactBean.getPinyin().contains(s.toString())) {
                            list.add(contactBean);
                            continue;
                        }
                        if (contactBean.getPhoneNum().contains(s.toString())) {
                            list.add(contactBean);
                            continue;
                        }
                    }
                }
                contactAdapter.setData(list);
                contactAdapter.notifyDataSetChanged();
            }
        });

        asyncQueryHandler = new ConatactAsyncQueryHandler(getActivity().getContentResolver());
//        initContacts();
        Android_6_Permission(Manifest.permission.READ_CONTACTS, READ_CALL_CODE);
        return rootView;
    }

    public void Android_6_Permission(String permission, int code) {
        //首先判断版本号是否大于等于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !(getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{permission}, code);
        } else {
            initContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("hjm", "获取联系人开始");
        initContacts();
    }

    /**
     * 初始化数据库查询参数
     */
    private void initContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = {ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY};
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call) {
            if (mContactBean == null || mContactBean.getPhoneNum() == null || mContactBean.getPhoneNum().length() < 11 || mContactBean.getPhoneNum().length() > 14) {
                ToastUtil.showToast(getActivity(), "请输入正确的号码,座机请加区号");
            } else {
                Intent intent = new Intent(getActivity(), CallActivity.class);
                intent.putExtra("contact", mContactBean);
                startActivity(intent);
            }
        }
    }

    /**
     * @author Administrator
     */
    private class ConatactAsyncQueryHandler extends AsyncQueryHandler {

        public ConatactAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = new HashMap<Integer, ContactBean>();
                contactBeanList = new ArrayList<ContactBean>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    String sortKey = cursor.getString(3);
                    int contactId = cursor.getInt(4);
                    Long photoId = cursor.getLong(5);
                    String lookUpKey = cursor.getString(6);

                    if (contactIdMap.containsKey(contactId)) {
                        // 无操作
                    } else {
                        // 创建联系人对象
                        ContactBean contact = new ContactBean();
                        contact.setDesplayName(name);
                        contact.setPhoneNum(number);
                        contact.setSortKey(sortKey);
                        contact.setPhotoId(photoId);
                        contact.setLookUpKey(lookUpKey);
                        contactBeanList.add(contact);
                        contactIdMap.put(contactId, contact);
                    }
                }
                if (contactBeanList.size() > 0) {

                    xingList = new ArrayList<>();
//                    contactXingmap = new HashMap<>();
                    contactXingmap = new HashMap<>();
                    for (ContactBean contactBean : contactBeanList) {
                        if (contactBean.getDesplayName() == null || contactBean.getDesplayName().length() < 1) {
                            break;
                        } else {
                            String cha1 = contactBean.getDesplayName().charAt(0) + "";
                            if (!contactXingmap.containsKey(cha1)) {
                                xingList.add(cha1);
                                StringBuilder sb = new StringBuilder();
                                for (String s : xingList) {
                                    sb.append(s);
                                }
                                ArrayList<ContactBean> tmp = new ArrayList<>();
                                tmp.add(contactBean);
                                contactXingmap.put(cha1, tmp);
                            } else {
                                ArrayList<ContactBean> tmp = contactXingmap.get(cha1);
                                tmp.add(contactBean);
                                contactXingmap.put(cha1, tmp);
                            }
                        }
                    }
                    filledData(contactBeanList);
                    Collections.sort(contactBeanList, pinyinComparator);
                    contactAdapter.setData(contactBeanList);
                    contactAdapter.notifyDataSetChanged();
                }
            }

            super.onQueryComplete(token, cookie, cursor);
        }

    }

    private void filledData(List<ContactBean> data) {
        for (int i = 0; i < data.size(); i++) {
            ContactBean contactBean = data.get(i);
            String pinyin = characterParser.getSelling(contactBean.getDesplayName());
            contactBean.setPinyin(pinyin);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                contactBean.setSortLetters(sortString.toUpperCase());
            } else {
                contactBean.setSortLetters("#");
            }
        }
    }

}
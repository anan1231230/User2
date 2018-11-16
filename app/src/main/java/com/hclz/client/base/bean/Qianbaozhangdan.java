package com.hclz.client.base.bean;

/**
 * Created by handsome on 16/4/26.
 */
public class Qianbaozhangdan {
    /**
     * //账单信息：购买卡片
     * 'billtype': 'buycard',
     * 'billid': '',
     * 'status': 10/20,  //状态，10：提交，20：支付成功
     * <p/>
     * //卡面信息
     * 'cardtype': 'balancecard',
     * 'cardid': '',
     * 'amount': 123,
     * 'expire':1212341,
     * 'price': 123,
     * <p/>
     * //消费信息
     * 'zhifutype': 'prepardcard',
     * 'transactionid': 'xxxxx', //交易ID，比如购买产品的订单orderid
     * 'balance_before': 120,
     * 'balance_after': 100,
     * 'zhifu_amount': 20,
     * <p/>
     * 'ct': '2015-12-12 12:32:23',
     * 'ut': '2015-12-12 12:32:23'
     */

    //账单信息
    private String billtype;
    private String billid;
    private int status;

    //卡面信息
    private String cardtype;
    private String cardid;
    private int amount;
    private long expire;
    private int price;

    //消费信息
    private String zhifutype;
    private String transactionid;
    private int balance_before;
    private int balance_after;
    private int zhifu_amount;

    //时间
    private String ct;
    private String ut;


    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getZhifutype() {
        return zhifutype;
    }

    public void setZhifutype(String zhifutype) {
        this.zhifutype = zhifutype;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public int getBalance_before() {
        return balance_before;
    }

    public void setBalance_before(int balance_before) {
        this.balance_before = balance_before;
    }

    public int getBalance_after() {
        return balance_after;
    }

    public void setBalance_after(int balance_after) {
        this.balance_after = balance_after;
    }

    public int getZhifu_amount() {
        return zhifu_amount;
    }

    public void setZhifu_amount(int zhifu_amount) {
        this.zhifu_amount = zhifu_amount;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }


}

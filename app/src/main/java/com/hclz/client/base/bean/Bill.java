package com.hclz.client.base.bean;

/**
 * Created by handsome on 16/4/26.
 */
public class Bill {

    /***
     * //账单信息
     * 'billtype': 'buycard',
     * 'billid': '',
     * 'status': 10/20, //状态，10：提交，20：支付成功
     * <p/>
     * //卡面信息
     * 'cardtype': 'balancecard',
     * 'cardid': 'balancecard100',
     * 'amount': 100   ,
     * 'price': 99
     **/

    private String billtype;
    private String billid;
    private String cardtype;
    private String cardid;
    private int status;
    private int amount;
    private int price;


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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}

package com.hclz.client.me.bean;

public class BasicEntity {
    /**
     * amount : 100
     * expire_utcms : null
     * mid : 15
     * name : points
     * unlimited : false
     */

    private PointsEntity points;
    private BalanceEntity balance;

    public BalanceEntity getBalance() {
        return balance;
    }

    public void setBalance(BalanceEntity balance) {
        this.balance = balance;
    }

    public PointsEntity getPoints() {
        return points;
    }

    public void setPoints(PointsEntity points) {
        this.points = points;
    }

    public static class PointsEntity {
        private long amount;
        private Object expire_utcms;
        private String mid;
        private String name;
        private boolean unlimited;

        public long getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public Object getExpire_utcms() {
            return expire_utcms;
        }

        public void setExpire_utcms(Object expire_utcms) {
            this.expire_utcms = expire_utcms;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isUnlimited() {
            return unlimited;
        }

        public void setUnlimited(boolean unlimited) {
            this.unlimited = unlimited;
        }
    }

    public static class BalanceEntity {
        private long amount;

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }
    }
}
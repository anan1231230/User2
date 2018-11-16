package com.hclz.client.mall.bean;

import com.hclz.client.base.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/6/24.
 */
public class MallType {

    /**
     * name : laidian
     * type2 : [{"name":"护肤","type3":[{"name":"面膜"},{"name":"洗面奶"},{"name":"BB霜"}]},{"name":"彩妆","type3":[{"name":"眉笔"},{"name":"BB霜"}]}]
     */

    private List<Type1Entity> type1;

    public List<Type1Entity> getType1() {
        return type1;
    }

    public void setType1(List<Type1Entity> type1) {
        this.type1 = type1;
    }

    public static class Type1Entity {
        private String name;
        /**
         * name : 护肤
         * type3 : [{"name":"面膜"},{"name":"洗面奶"},{"name":"BB霜"}]
         */

        private List<Type2Entity> type2;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Type2Entity> getType2() {
            return type2;
        }

        public void setType2(List<Type2Entity> type2) {
            this.type2 = type2;
        }

        public static class Type2Entity {
            private String name;
            /**
             * name : 面膜
             */

            private List<Type3Entity> type3;

            private List<Product> products;
            //购物使用
            private int count;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public List<Product> getProducts() {
                return products;
            }

            public void setProducts(ArrayList<Product> products) {
                this.products = products;
            }

            /**
             * 获取Item内容
             *
             * @param pPosition
             * @return
             */
            public Object getItem(int pPosition) {
                // Category排在第一位
                if (pPosition == 0) {
                    return name;
                } else {
                    return products.get(pPosition - 1);
                }
            }

            /**
             * 当前类别Item总数。Type也需要占用一个Item
             *
             * @return
             */
            public int getItemCount() {
                return products.size() + 1;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Type3Entity> getType3() {
                return type3;
            }

            public void setType3(List<Type3Entity> type3) {
                this.type3 = type3;
            }

            public static class Type3Entity {
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}

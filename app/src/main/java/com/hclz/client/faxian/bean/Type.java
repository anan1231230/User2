package com.hclz.client.faxian.bean;

import java.util.List;

/**
 * Created by handsome on 16/2/16.
 */
public class Type {

    /**
     * icon : null
     * id : laidian
     * name : 来点
     * type2 : [{"id":"lingshi","name":"零食","type3":[{"id":"gaodian","name":"糕点"},{"id":"tangguo","name":"糖果"},{"id":"jianguo","name":"坚果"},{"id":"naizhipin","name":"奶制品"}]},{"id":"yinliao","name":"饮料","type3":[{"id":"tansuan","name":"碳酸"}]},{"id":"jinkoulingshi","name":"进口零食","type3":[{"id":"jianguo","name":"坚果"}]},{"id":"youxibanlv","name":"游戏伴侣","type3":[{"id":"tansuan","name":"碳酸"},{"id":"gaodian","name":"糕点"}]},{"id":"dianyingbanlv","name":"电影伴侣","type3":[{"id":"jianguo","name":"坚果"},{"id":"gaodian","name":"糕点"}]},{"id":"shuiqianlaidian","name":"睡前来点","type3":[{"id":"gaodian","name":"糕点"},{"id":"naipin","name":"奶制品"}]},{"id":"jianguo","name":"坚果","type3":[{"id":"guowaijianguo","name":"国外坚果"},{"id":"xinjiangjianguo","name":"新疆坚果"}]}]
     */

    private List<Type1Entity> type1;
    private List<CountryEntity> countries;
    private List<PromotionEntity> promotions;

    public List<PromotionEntity> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionEntity> promotions) {
        this.promotions = promotions;
    }

    public List<CountryEntity> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryEntity> countries) {
        this.countries = countries;
    }

    public List<Type1Entity> getType1() {
        return type1;
    }

    public void setType1(List<Type1Entity> type1) {
        this.type1 = type1;
    }

    public static class CountryEntity {
        private String icon;
        private String name;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class PromotionEntity {
        private String poster;
        private List<String> tags;
        private String title;

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class Type1Entity {
        private Object icon;
        private String id;
        private String name;
        /**
         * id : lingshi
         * name : 零食
         * type3 : [{"id":"gaodian","name":"糕点"},{"id":"tangguo","name":"糖果"},{"id":"jianguo","name":"坚果"},{"id":"naizhipin","name":"奶制品"}]
         */

        private List<Type2Entity> type2;

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
            //            private String id;

            private String icon;
            private String name;
            /**
             * id : gaodian
             * name : 糕点
             */

            private List<Type3Entity> type3;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }

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
                private String icon;
                private String name;

                public String getId() {
                    return icon;
                }

                public void setId(String id) {
                    this.icon = id;
                }

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

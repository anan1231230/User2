package com.hclz.client.shouye.bean;

import java.util.List;

/**
 * Created by hjm on 16/9/22.
 */

public class ShouyeBean {

    /**
     * block_name :
     * elements : [{"content":"肉类禽类","id":4,"image":"http://img.hclz.me/data%2Fbf9647187a2a447e575825f4eb7bac8b.png","name":"aaa","order":1,"type":"tag"},{"content":"蔬菜凉菜","id":5,"image":"http://img.hclz.me/data%2Fb83c42725d5ddd74fc5112af37f1ca63.png","name":"222","order":2,"type":"tag"},{"content":"爆款","id":6,"image":"http://img.hclz.me/data%2F21ca931194eb153a5aac082db385e9ba.png","name":"aaa","order":3,"type":"tag"}]
     */
    private String block_name;
    /**
     * content : 肉类禽类
     * id : 4
     * image : http://img.hclz.me/data%2Fbf9647187a2a447e575825f4eb7bac8b.png
     * name : aaa
     * order : 1
     * type : tag
     */

    private List<ElementsBean> elements;

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public List<ElementsBean> getElements() {
        return elements;
    }

    public void setElements(List<ElementsBean> elements) {
        this.elements = elements;
    }

    public static class ElementsBean {
        private String content;
        private int id;
        private String image;
        private String name;
        private int order;
        private String type;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

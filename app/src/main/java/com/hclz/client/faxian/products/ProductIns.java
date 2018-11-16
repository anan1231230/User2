package com.hclz.client.faxian.products;

import android.text.TextUtils;

import com.hclz.client.faxian.bean.Product;
import com.hclz.client.faxian.bean.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 16/6/12.
 */
public class ProductIns {

    private static ProductIns mProductIns;
    private Product mProduct;
    private Type mType;

    public ProductIns() {
    }

    public static ProductIns getInstance() {
        if (mProductIns == null) {
            mProductIns = new ProductIns();
        }
        return mProductIns;
    }

    //设置Product
    public void setProducts(Product product) {
        mProduct = product;
    }

    //设置type
    public void setType(Type type) {
        mType = type;
    }

    //清空数据
    public void clear() {
        mType = null;
        mProduct = null;
    }

    //获取所有的type2
    public List<Type.Type1Entity.Type2Entity> getAllType2(String type1Name) {
        if (type1Name == null) return new ArrayList<>();
        if (mType == null) {
            return new ArrayList<>();
        } else if (mType.getType1() == null) {
            return new ArrayList<>();
        } else {
            for (Type.Type1Entity tmpType1 : mType.getType1()) {
                if (type1Name.equals(tmpType1.getName())) {
                    if (tmpType1.getType2() == null) {
                        return new ArrayList<>();
                    } else {
                        return tmpType1.getType2();
                    }
                }
            }
            return new ArrayList<>();
        }
    }

    //获取某个type2的type3
    public List<Type.Type1Entity.Type2Entity.Type3Entity> getType3(String type2Name) {
        if (type2Name == null) return new ArrayList<>();
        if (mType == null) {
            return new ArrayList<>();
        } else if (mType.getType1() == null) {
            return new ArrayList<>();
        } else {
            for (Type.Type1Entity tmpType1 : mType.getType1()) {
                if (tmpType1.getType2() == null) {
                    continue;
                } else {
                    for (Type.Type1Entity.Type2Entity tmpType2 : tmpType1.getType2()) {
                        if (type2Name.equals(tmpType2.getName())) {
                            if (tmpType2.getType3() == null) {
                                return new ArrayList<>();
                            } else {
                                return tmpType2.getType3();
                            }
                        }
                    }
                }
            }
            return new ArrayList<>();
        }
    }

    //TODO 获取promotion列表
    public List<Type.PromotionEntity> getPromotions() {
        if (mType == null || mType.getPromotions() == null) {
            return new ArrayList<>();
        } else {
            return mType.getPromotions();
        }
    }

    //TODO 获取国家
    public List<Type.CountryEntity> getCountries() {
        if (mType == null || mType.getCountries() == null) {
            return new ArrayList<>();
        } else {
            return mType.getCountries();
        }
    }

    //根据一个或者多个tag,获取产品列表
    public List<Product.Product2sEntity> getProductUsingTags(ArrayList<String> tags) {

        List<Product.Product2sEntity> returnProducts = new ArrayList<>();

        if (mProduct == null || mProduct.getProduct2s() == null || tags == null || tags.size() <= 0) {
            return new ArrayList<>();
        } else {
//            for (Product.Product2sEntity product : mProduct.getProduct2s()) {
//                boolean isPipei = true;
//                if (tags != null) {
//                    for (String tag : tags) {
//                        if (!product.getTags().contains(tag)) {
//                            isPipei = false;
//                            break;
//                        }
//                    }
//                } else {
//                    isPipei = false;
//                }
//                if (isPipei) {
//                    returnProducts.add(product);
//                }
//            }
            List<Product.Product2sEntity> finalProducts = new ArrayList<>();
            if (tags != null) {
                for (String tag : tags) {
                    returnProducts.addAll(getProductUsingTagFromProducts(tag));
                }
                for (Product.Product2sEntity product2sEntity : returnProducts){
                    boolean isContains = false;
                    for (Product.Product2sEntity product : finalProducts){
                        if (product2sEntity.getPid().equals(product.getPid())){
                            isContains = true;
                            break;
                        }
                    }
                    if (!isContains) {
                        finalProducts.add(product2sEntity);
                    }
                }
            }
            return finalProducts;
        }
    }

    public List<Product.Product2sEntity> getProductUsingTagFromProducts(String key){
        List<Product.Product2sEntity> returnProducts = new ArrayList<>();
        if (mProduct == null || mProduct.getProduct2s() == null || key == null) {
            return new ArrayList<>();
        } else {
            for (Product.Product2sEntity product : mProduct.getProduct2s()) {
                for (String tag : product.getTags()){
                    if (tag.contains(key)) {
                        returnProducts.add(product);
                        break;
                    }
                }
            }
            return returnProducts;
        }
    }

    //关键词包含搜索---type+product获得最终产品列表
    public List<Product.Product2sEntity> getProductUsingKey(String key) {
        List<Product.Product2sEntity> returnProducts = new ArrayList<>();
        if (TextUtils.isEmpty(key)) {
            return new ArrayList<>();
        } else {
            List<String> tags = getTagsUsingKeyFromTags(key);
            for (String tag : tags) {
                ArrayList<String> tmp = new ArrayList<>();
                tmp.add(tag);
                returnProducts.addAll(getProductUsingTags(tmp));
            }
            returnProducts.addAll(getProductUsingTagFromProducts(key));
            returnProducts.addAll(getProductsUsingKeyFromProducts(key));
        }
        List<Product.Product2sEntity> finalProducts = new ArrayList<>();
        for (Product.Product2sEntity product2sEntity : returnProducts){
            boolean isContains = false;
            for (Product.Product2sEntity product : finalProducts){
                if (product2sEntity.getPid().equals(product.getPid())){
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                finalProducts.add(product2sEntity);
            }
        }
        return finalProducts;
    }

    //关键词获取tags
    private List<String> getTagsUsingKeyFromTags(String key) {
        List<String> resultTags = new ArrayList<>();
        if (TextUtils.isEmpty(key) || mType == null || mType.getType1() == null) {
            return new ArrayList<>();
        } else {
            for (Type.Type1Entity tmpType1 : mType.getType1()) {
                if (tmpType1.getName().contains(key)) {
                    resultTags.add(tmpType1.getName());
                }
                if (tmpType1.getType2() == null) {
                    break;
                } else {
                    for (Type.Type1Entity.Type2Entity tmpType2 : tmpType1.getType2()) {
                        if (tmpType2.getName().contains(key)) {
                            resultTags.add(tmpType2.getName());
                        }
                        if (tmpType2.getType3() == null) {
                            break;
                        } else {
                            for (Type.Type1Entity.Type2Entity.Type3Entity tmpType3 : tmpType2.getType3()) {
                                if (tmpType3.getName().contains(key)) {
                                    resultTags.add(tmpType3.getName());
                                }
                            }
                        }
                    }
                }
            }
            if (mType.getCountries() != null && mType.getCountries().size() > 0) {
                for (Type.CountryEntity country : mType.getCountries()) {
                    if (country.getName().contains(key)) {
                        resultTags.add(country.getName());
                    }
                }
            }
            return resultTags;
        }
    }

    //关键词获取products
    private List<Product.Product2sEntity> getProductsUsingKeyFromProducts(String key) {
        List<Product.Product2sEntity> returnProducts = new ArrayList<>();
        if (TextUtils.isEmpty(key)) {
            return new ArrayList<>();
        } else {
            if (mProduct == null || mProduct.getProduct2s() == null) {
                return new ArrayList<>();
            } else {
                for (Product.Product2sEntity product2sEntity : mProduct.getProduct2s()) {
                    if (product2sEntity.getName().contains(key)) {
                        returnProducts.add(product2sEntity);
                    }
                }
                return returnProducts;
            }
        }

    }

    public Product getmProduct() {
        return mProduct;
    }

    public Type getmType() {
        return mType;
    }

}

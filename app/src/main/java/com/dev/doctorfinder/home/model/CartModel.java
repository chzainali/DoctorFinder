package com.dev.doctorfinder.home.model;

public class CartModel {
    String id,productName,productId,userId,price,imageUri;
    int quantity;

    public CartModel() {
    }

    public CartModel(String id, String productName, String productId, String userId, String price, int quantity, String imageUri) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
        this.imageUri=imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


    public int addQuantity(){
        quantity+=1;
        setQuantity(quantity);
        return getQuantity();
    }

    public  int addPrice(){
        return Integer.parseInt(price) *quantity;
    }

    public int minusQuantity(){
        if(quantity>0){
            quantity-=1;
            setQuantity(quantity);
            return getQuantity();
        }else {
            return quantity;
        }
    }


}

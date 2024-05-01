package org.example.dao.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.annotations.Table;

import java.util.Objects;

@Table(name = "products")
public class Product {

    private Long id;
    private Long userId;
    private String account;
    private Long balance;
    private ProductType productType;

    @JsonCreator
    public Product(
            @JsonProperty("id") Long id,
            @JsonProperty("userId") Long userId,
            @JsonProperty("account") String account,
            @JsonProperty("balance") Long balance,
            @JsonProperty("productType") ProductType productType
    ) {
        this.id = id;
        this.userId = userId;
        this.account = account;
        this.balance = balance;
        this.productType = productType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(userId, product.userId) && Objects.equals(account, product.account) && Objects.equals(balance, product.balance) && productType == product.productType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(account);
        result = 31 * result + Objects.hashCode(balance);
        result = 31 * result + Objects.hashCode(productType);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
               "id=" + id +
               ", userId=" + userId +
               ", account='" + account + '\'' +
               ", balance=" + balance +
               ", productType=" + productType +
               '}';
    }
}

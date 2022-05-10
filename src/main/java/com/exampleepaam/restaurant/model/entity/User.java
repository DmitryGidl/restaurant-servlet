package com.exampleepaam.restaurant.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Describes User entity
 */
public class User extends AbstractBaseEntity {

    private String name;
    private String email;
    private String password;
    private BigDecimal balanceUAH;

    private Role role;

    private List<Order> orders;
    private boolean enabled;


    public User(long id, String name, String email, String password, BigDecimal balanceUAH,
                Role role, List<Order> orders, boolean enabled) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.balanceUAH = balanceUAH;
        this.role = role;
        this.orders = orders;
        this.enabled = enabled;
    }

    public User(String name, String email, String password ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balanceUAH = BigDecimal.ZERO;
        this.orders = new ArrayList<>();
        this.enabled = true;
        this.role = Role.USER;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balanceUAH=" + balanceUAH +
                ", role=" + role +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        if (id != 0) {
            return Objects.hash(id);
        } else {
            return super.hashCode();
        }
    }
public void addOrder(Order order) {
        orders.add(order);
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalanceUAH() {
        return balanceUAH;
    }

    public void setBalanceUAH(BigDecimal balanceUAH) {
        this.balanceUAH = balanceUAH;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}

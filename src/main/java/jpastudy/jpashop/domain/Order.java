package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    //회원과 *:1 관계.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //Delivery와 1:1 관계
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //OrderItem과의 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //주문 날짜
    private LocalDateTime orderDate;

    //주문 상태
    @Enumerated(EnumType.STRING)
    private  OrderStatus status;

    //Order와 Member의 관계
    public void setMember(Member member) {
        this.member=member;
        member.getOrders().add(this);
    //Member의 Orders를 가져와서 Order에 추가해준 후 set을 이용해 해당 Member의 Orders를 찾는다
    }

    //Order와 Delivery
    public void setDelivery(Delivery delivery) {
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    //Order와 OrderItem
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

}

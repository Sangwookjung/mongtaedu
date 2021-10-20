package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    //Order와의 관계 *:1 ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //Item과의 관계 *:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    //주문 가격
    private int orderPrice;

    //주문 수량
    private int count;

    //==비즈니스 로직 : 주문 상품 생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        //OrderItem과 Item 연결
        orderItem.setItem(item);
        //주문 상품 가격
        orderItem.setOrderPrice(orderPrice);
        //수량
        orderItem.setCount(count);
        //주문한 만큼 재고 감소
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직 : 주문 취소 -> 재고량 복구 ==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==비즈니스 로직 : 주문상품 전체 가격 조회 ==//
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }




}

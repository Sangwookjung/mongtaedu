package jpastudy.jpashop.repository.order.query;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
//of 를 안넣어주면 다른 변수도 전부 같은지 비교한다
//지금은 orderId 변수만 비교하여 처리한다
public class OrderQueryDto {
    private Long orderId;                           //주문번호
    private String name;                            //회원이름
    private LocalDateTime orderDate;                //주문날짜
    private OrderStatus orderStatus;                //주문상태
    private Address address;                        //배송주소
    private List<OrderItemQueryDto> orderItems;     //의존관계인 OrderItem을 저장한 OrderItemQueryDto ->
                                                    //query를 수행한 결과를 담는 용도로 사용

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate,
                         OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}

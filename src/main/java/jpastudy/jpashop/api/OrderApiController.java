package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

//OneToMany 관계 성능 최적화
//Order -> OrderItem -> Item
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    //V1 : Entity를 API에 직접 노출
    //N+1 발생함
    //Order 1번, Member ,Delivery N번 (Order Row 수 만큼)
    //OrderItem (Order Row 수 만큼)
    //Item N번 (OrderItem Row 수 만큼)
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName()); //Lazy 강제초기화
//          =orderItems.stream().forEach(o -> o.getItem().getName()); //Lazy 강제초기화
        }
        return all;
    }

    //V2 : Entity를 DTO로 변환해서 노출하는 방식
    //N + 1 문제 발생함
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream().map(order -> new OrderDto(order)).collect(toList());
    }

    //V3 : Entity를 DTO로 변환해서 노출하는 방식, Fetch Join 으로 성능 최적화
    //   : xxToMany 의존 관계 객체들의 Paging 처리가 안된다
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(order -> new OrderDto(order))
                .collect(toList());
        return result;
    }



    //응답과 요청에 사용할 DTO Inner Class 선언
    @Data
    static class OrderItemDto {
        private String itemName; //상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    } //static class OrderItemDto

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    } //static class OrderDto


}

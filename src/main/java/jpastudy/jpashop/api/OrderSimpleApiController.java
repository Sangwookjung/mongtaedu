package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderSearch;
import jpastudy.jpashop.domain.OrderStatus;
import jpastudy.jpashop.repository.OrderRepository;
import jpastudy.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpastudy.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order, Order -> Member , Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        all.forEach(order -> {
            order.getMember().getName();        //Member를 Lazy Loading 강제 초기화
            order.getDelivery().getAddress();   //Delivery를 Lazy Loading 강제 초기화
        });
        return all;
    }

    //V2: Entity를 DTO로 변화
    //  : Lazy loading으로 쿼리 반복 호출 (=N + 1 문제)
    //Order 2건 Member 2번, Delivery 2번 -> order 1, member 2번, delivery 2번
    //=> Fetch Join으로 해결
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream().map(order -> new SimpleOrderDto(order)).collect(toList());
    }

    // V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용함)
    // fetch join으로 쿼리 1번 호출
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    //V4 Query 결과를 Entity 대신 DTO 저장하여 조회
    //_ Query 1번 호출
    //_ select 절에 원하는 data만 선택해서 조회
    //_ DTO가 너무 많아지는 단점이 있다
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    //응답과 요청에 사용할 DTO Inner Class 선언
    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

            public SimpleOrderDto(Order order) {
                orderId = order.getId();
                name = order.getMember().getName(); //Lazy 강제 초기화
                orderDate = order.getOrderDate();
                orderStatus = order.getStatus();
                address = order.getDelivery().getAddress(); //Lazy 강제 초기화
            }
    }

}

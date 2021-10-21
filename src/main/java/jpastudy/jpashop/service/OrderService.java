package jpastudy.jpashop.service;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.domain.item.Item;
import jpastudy.jpashop.repository.ItemRepository;
import jpastudy.jpashop.repository.MemberRepository;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    //주문생성 처리
    @Transactional
    public Long order(Long memberId, Long itemdId, int count) {
        //member조회
        Member member = memberRepository.findOne(memberId);
        //Item 조회
        Item item = itemRepository.findOne(itemdId);
        //Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);
        //OrderItem 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //Order 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        //Order 저장
        orderRepository.save(order);

        return order.getId();
    }

    //주문취소 처리
    @Transactional
    public void cancelOrder(long orderId) {
        //주문조회
        Order order = orderRepository.findOne(orderId);
        //주문취소
        order.cancel();
    }

    /**
     * 주문검색
     * @param orderSearch 회원번호, 주문상태
     * @return 주문 목록
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
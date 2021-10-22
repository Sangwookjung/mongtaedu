package jpastudy.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    // 1:N 관계(컬렉션)를 제외한 Order, Member, Delivery를 한번에 조회
    // ToOne 관계인 Member, Delivery만 조회
    // OrderItem은 조회하지 않음
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpastudy.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" + " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    } //findOrders

    //Order, OrderItem, Item 조회
    //ToMany 관계인 OrderItem & Item을 조회해서 DTO 저장
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpastudy.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name,oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    } //findOrderItems

    //OrderQueryDto가 참조하는 OrderItemQueryDto를 채워넣기
    public List<OrderQueryDto> findOrdersQueryDtos() {
        //toOne 관계인 객체로 한번에 조회
        List<OrderQueryDto> orderQueryDtos = findOrders();
        //loop를 돌면서 toMany 관계인 객체를 조회해서 저장하기
        orderQueryDtos.forEach(order -> {
            List<OrderItemQueryDto> orderItemQueryDtos = findOrderItems(order.getOrderId());

            order.setOrderItems(orderItemQueryDtos);
        });
        return orderQueryDtos;
    }


}

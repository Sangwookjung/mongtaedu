package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class EntityTest {
    @Autowired
    EntityManager em;

    @Test //Test는 Rollback(value=true)가 기본 값
    @Rollback(value = true)
    public void entity() throws Exception {
        Member member = new Member();
        member.setName("Mongta");
        Address address = new Address("서울","동작구","마립빌딩");

        member.setAddress(address);

        em.persist(member);

        //Order 생성
        Order order = new Order();
        //Order와 Member 연결
        order.setMember(member);

        //Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //em.persist(delivery); No need
        //Order와 Delivery 연결
        order.setDelivery(delivery);

        //Item - Book 생성

        Book book = new Book();
        book.setName("Clean Code");
        book.setPrice(10000);
        book.setStockQuantity(10);
        book.setAuthor("불도깨비");
        book.setIsbn("999beedulgiya");
        //영속성 context에 저장
        em.persist(book);

        //OrderItem 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(2);
        orderItem.setOrderPrice(20000);
        orderItem.setItem(book);

        //Order와 OrderItem 연결
        order.addOrderItem(orderItem);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);

        //영속성 context에 저장
        em.persist(order);


    }
}
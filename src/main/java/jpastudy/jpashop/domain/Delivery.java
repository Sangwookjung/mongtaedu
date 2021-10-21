package jpastudy.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded //:address의 객체를 참조한다는 의미
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    //DeliveryStatus에 있는 READY와  COMP가 String 문자열로 저장되도록 설정
}

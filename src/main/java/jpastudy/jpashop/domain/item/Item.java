package jpastudy.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtyped")
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    
    //상품 이름
    private String name;
    
    //상품 가격
    private int price;
    
    //상품 재고
    private int stockQuantity;

}

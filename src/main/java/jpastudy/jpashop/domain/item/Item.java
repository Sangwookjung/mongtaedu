package jpastudy.jpashop.domain.item;

import jpastudy.jpashop.exception.NotEnoughStockException;
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

    //==비즈니스 로직==//
    //주문이 취소되어 재고량 복구
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    //주문 후 재고량 감소
     public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}

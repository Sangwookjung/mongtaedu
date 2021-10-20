package jpastudy.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable //Embedded가 참조할 수 있게 하는 Annotation
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;
    private String street;
    private String zipcode;

//    protected Address() {
//    } == @NoArgsConstructor(access = AccessLevel.PROTECTED)

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

package jpastudy.jpashop;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {
    @Test
    public void stream() {
        List<User> users = List.of(new User("길동", 10),
                                   new User("mongta", 1),
                                   new User("SangWook", 30));
        //User의 Name 추출해서 List<String>으로 변환해서 출력하세요
        //.collect(Collectors.toList())에서 return 값이 List로 변환된다 그 전엔 Stream 타입
//        List<String> nameList = users.stream().map(user -> user.getName()).collect(Collectors.toList());
        List<String> nameList = users.stream().map(User :: getName).collect(Collectors.toList());

        nameList.forEach(name -> System.out.println(name)); //Lamda
        nameList.forEach(System.out::println); //Method Reference

        System.out.println("-------------------------------------------------");
        //20살 이상인 User의 Name 추출해서 List<String>으로 변환해서 출력하세요
        users.stream().filter(user -> user.getAge() >= 20)
                      .forEach(user -> System.out.println(user.getName()));

        List<String> names = users.stream()                             //Stream<User>
                                    .filter(user -> user.getAge() >= 20)//Stream<User>
                                    .map(user -> user.getName())        //Stream<String>
                                    .collect(Collectors.toList());      //List<String>

        names.forEach(System.out::println);

        //User들의 나이 합계
        int sum = users.stream() //Stream<User>
                        .mapToInt(user -> user.getAge()) //IntStream
                        .sum();
        System.out.println("합계: " + sum);
    }


    @Data
    @AllArgsConstructor
    static class User {
        private String name;
        private int age;

    }

}

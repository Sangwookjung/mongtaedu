package jpastudy.jpashop;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class GroupByTest {
    @Test
    public void groupby() {
        List<Dish> dishList = Arrays.asList(new Dish("pork", 700, Type.MEAT),
                new Dish("spaghetti", 580, Type.NOODLE),
                new Dish("salmon", 350, Type.FISH),
                new Dish("onion", 200, Type.VEGE),
                new Dish("pumpkin", 150, Type.VEGE));
        //Dish 이름을 List 출력하기
        List<String> nameList = dishList.stream().map(Dish::getName).collect(toList());

        nameList.forEach(dishName -> System.out.println(dishName));

        //Dish 이름을 구분자를 포함한 문자열로 출력
        String nameStrs = dishList.stream().map(dish -> dish.getName()).collect(Collectors.joining(","));
        System.out.println(nameStrs);

        //Dish cal sum, avg
        Integer totalcal = dishList.stream().collect(Collectors.summingInt(dish -> dish.getCal()));
        IntSummaryStatistics totalcal2 = dishList.stream().collect(Collectors.summarizingInt(Dish::getCal));
        System.out.println(totalcal);
        System.out.println(totalcal2);

        //Dish Type별

        Map<Type, List<Dish>> dishesByType = dishList.stream().collect(Collectors.groupingBy(dish -> dish.getType()));

        System.out.println(dishesByType);
    }

    @Data
    static class Dish {
        String name;
        int cal;
        Type type;

        public Dish(String name, int cal, Type type) {
            this.name = name;
            this.cal = cal;
            this.type = type;
        }

        @Override
        public String toString() {
            return "Dish{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    enum Type {
        MEAT, FISH, NOODLE, VEGE
    }

}

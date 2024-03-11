package example.java.functionalprogramming.function;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class _Predicate {

    private static final Predicate<String> emailfilterFunction = str -> str.contains("@");

    private static final Predicate<String> isBlank = String::isEmpty;

    public static void main(String[] args) {

        //Filter All Emails Within List
        Stream.of(
                "user1@domain.com",
                "userId1",
                "user2@domain.com",
                "userId2"
        )
        .filter(emailfilterFunction)
        .forEach(System.out::println);
    }
}

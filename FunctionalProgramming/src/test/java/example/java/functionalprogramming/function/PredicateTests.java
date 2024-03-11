package example.java.functionalprogramming.function;

import example.java.functionalprogramming.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateTests {

    public List<String> filterEmail(List<String> list){
        Predicate<String> filterEmailPredicate = str -> str.contains("@");

        return list
                    .stream()
                    .filter(filterEmailPredicate)
                    .collect(Collectors.toList());
    }

    public List<User> filterUser(List<User> list){
        Predicate<User> filterOutBlankEmail = user -> !(user.getEmail() == null || user.getEmail().isEmpty());

        return list
                .stream()
                .filter(filterOutBlankEmail)
                .collect(Collectors.toList());
    }


    @Test
    public void givenUserData_whenFilterEmailIsApplied_thenListOfValidEmailIsReturned(){
        //Setup
        List<String> userData = List.of(
                "john.doe@example.com",
                "user1",
                "user2",
                "alice.smith@example.com");

        //Execute And Test
        Assertions.assertIterableEquals(
                List.of("john.doe@example.com","alice.smith@example.com"),
                filterEmail(userData),"List should contains only emails");
    }

    @Test
    public void FilterUser_WhenListContainsUserWithBlankEmail_ThenExcludeUserWithBlankEmail(){
        //Setup
        User john = new User(1,"John",null);
        User alice = new User(2,"Alice","alice.smith@example.com");

        //Execute And Test
        Assertions.assertIterableEquals(
                List.of(alice),
                filterUser(List.of(john,alice)),
                "List should contain users with non empty email");
    }


}

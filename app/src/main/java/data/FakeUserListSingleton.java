package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.User;

public class FakeUserListSingleton {
    private static FakeUserListSingleton instance;
    private List<User> fakeUserList = new ArrayList<>();

    private FakeUserListSingleton() {
        // Ajouter les utilisateurs à la liste
        fakeUserList.add(new User("001", "Alice", "alice@example.com", "https://www.example.com/alice.jpg", Arrays.asList("1", "2"), null));
        fakeUserList.add(new User("002", "Bob", "bob@example.com", "https://www.example.com/bob.jpg", Arrays.asList("3", "4"), "3"));
        fakeUserList.add(new User("003", "Caroline", "caroline@example.com", "https://www.example.com/caroline.jpg", Arrays.asList("5", "6"), "6"));
        fakeUserList.add(new User("004", "David", "david@example.com", "https://www.example.com/david.jpg", Arrays.asList("1", "4"), "4"));
        fakeUserList.add(new User("005", "Emma", "emma@example.com", "https://www.example.com/emma.jpg", Arrays.asList("2", "3"), null));
        fakeUserList.add(new User("006", "Frank", "frank@example.com", "https://www.example.com/frank.jpg", Arrays.asList("5", "7"), "7"));
        fakeUserList.add(new User("007", "Grace", "grace@example.com", "https://www.example.com/grace.jpg", Arrays.asList("1", "2"), null));
        fakeUserList.add(new User("008", "Henry", "henry@example.com", "https://www.example.com/henry.jpg", Arrays.asList("4", "3"), "5"));
        fakeUserList.add(new User("009", "Isabelle", "isabelle@example.com", "https://www.example.com/isabelle.jpg", Arrays.asList("2", "6"), null));
        fakeUserList.add(new User("010", "John", "john@example.com", "https://www.example.com/john.jpg", Arrays.asList("3", "7"), "7"));
    }

    public static FakeUserListSingleton getInstance() {
        if (instance == null) {
            instance = new FakeUserListSingleton();
        }
        return instance;
    }

    public List<User> getFakeUserList() {
        return fakeUserList;
    }

    public void setFakeUserList(List<User> fakeUserList) {
        this.fakeUserList = fakeUserList;
    }
}


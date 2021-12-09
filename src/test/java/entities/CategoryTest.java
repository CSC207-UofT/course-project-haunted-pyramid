package entities;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


/**
 * @author Malik Lahlou
 */


public class CategoryTest {
    User user = new User(UUID.randomUUID(), "m", "l", "p");
    User user1 = new User(UUID.randomUUID(), "f", "q", "p");
    User user2 = new User(UUID.randomUUID(), "e", "w", "p");
    Category category = new Category(UUID.randomUUID(), "c1", user);


    @Before
    public void setUp() {
        category.addRegularUser(user1);
        category.addRegularUser(user2);
    }

    @Test
    public void removeUserTest() {
        category.removeUser(user, user1);
        assertEquals(category.getRegularUsers().size(), 1);
        assertEquals(category.getRegularUsers().get(0), user2);
        assertEquals(category.getAdminUsers().size(), 1);
        category.removeUser(user1, null);
        assertEquals(category.getRegularUsers().size(), 0);
        assertEquals(category.getAdminUsers().get(0), user2);
    }
}
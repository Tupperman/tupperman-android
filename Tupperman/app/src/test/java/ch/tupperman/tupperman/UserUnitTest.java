package ch.tupperman.tupperman;

import org.json.JSONObject;
import org.junit.Test;

import ch.tupperman.tupperman.models.User;

import static org.junit.Assert.assertEquals;

public class UserUnitTest {

    @Test
    public void createUserTest() throws Exception {
        String email = "email";
        String password = "password";
        User user = new User(email, password);
        JSONObject jsonUser = user.toJSON();

        assertEquals(jsonUser.get(email), email);
        assertEquals(jsonUser.get(password), password);
    }
}

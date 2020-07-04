package application.service;

import application.dao.UserDao;
import application.model.DaoUser;
import application.model.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class JwtUserDetailsServiceTest {

    Fixture fixture;

    @Before
    public void initialize() {
        fixture = new Fixture();
        fixture.init();
    }

    @Test
    public void test_loadByUsernameExist() {
        fixture.given_UsernameIsInTheLogger();
        fixture.when_UserIsLookUp("user");
        fixture.then_VerifyUserExist();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void test_loadByUsernameNotExist() {
        fixture.given_UsernameIsInNotTheLogger();
        fixture.when_UserIsLookUp("user");
    }

    @Test
    public void test_NewUserUsesTheService() {
        fixture.given_UserRegistersOnApi();
        fixture.when_UserIsSaved();
        fixture.then_VerifyUserSaved();
    }

    private class Fixture {
        private final Integer NEW_CASES_NUMBER = 123;
        private final String USER_NAME = "username";
        private final String PASSWORD = "password";
        @Mock
        private SessionRegistry sessionRegistry;
        @Mock
        private UserDao userDao;
        @Mock
        private PasswordEncoder bcryptEncoder;
        @InjectMocks
        private JwtUserDetailsService jwtUserDetailsService;
        private UserDetails results;
        private DaoUser savedUser;
        private UserDTO userDTO;

        public void init() {
            MockitoAnnotations.initMocks(this);
        }

        public void given_UsernameIsInTheLogger() {
            DaoUser daoUser = new DaoUser();
            daoUser.setUsername(USER_NAME);
            daoUser.setPassword(PASSWORD);
            when(userDao.findByUsername(anyString())).thenReturn(daoUser);
        }

        public void given_UsernameIsInNotTheLogger() {
            when(userDao.findByUsername(anyString())).thenReturn(null);
        }

        public void given_UserRegistersOnApi() {
            DaoUser daoUser = new DaoUser();
            daoUser.setUsername(USER_NAME);
            daoUser.setPassword(PASSWORD);
            userDTO = new UserDTO();
            userDTO.setUsername(USER_NAME);
            userDTO.setPassword(PASSWORD);
            when(bcryptEncoder.encode(anyString())).thenReturn(PASSWORD);
            when(userDao.save(any())).thenReturn(daoUser);
        }

        public void when_UserIsLookUp(String user) {
            results = jwtUserDetailsService.loadUserByUsername(user);
        }

        public void when_UserIsSaved() {
            savedUser = jwtUserDetailsService.save(userDTO);
        }

        public void then_VerifyUserExist() {
            assertEquals(USER_NAME, results.getUsername());
        }

        public void then_VerifyUserSaved() {
            assertEquals(USER_NAME, savedUser.getUsername());
        }

    }
}

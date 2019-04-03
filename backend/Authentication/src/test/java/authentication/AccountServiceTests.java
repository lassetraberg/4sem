package authentication;


import commonAuthentication.domain.model.Account;
import commonAuthentication.domain.repository.IAccountRepository;
import authentication.domain.service.AccountService;
import authentication.domain.service.IAccountService;
import authentication.util.IHasher;
import authentication.util.JwtProvider;
import common.web.exceptions.ValidationException;
import io.javalin.UnauthorizedResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;

public class AccountServiceTests {
    @Mock
    private IAccountRepository repository;
    @Mock
    private IHasher hasher;

    private IAccountService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        JwtProvider jwtProvider = new JwtProvider();
        service = new AccountService(repository, jwtProvider, hasher);
    }

    @Test
    public void createAccount_UsernameAlreadyRegistered_ShouldThrowException() {
        // Arrange
        Account fakeAccount = new Account(null, "bob", "password", null, null, -1, null);
        Mockito.when(repository.findByUsername(anyString())).thenReturn(fakeAccount);
        ValidationException ex = null;

        // Act
        try {
            service.create(fakeAccount);
        } catch (ValidationException vex) {
            ex = vex;
        }

        // Assert
        Assert.assertNotNull(ex);
        Assert.assertEquals(ex.getStatus(), HttpStatus.BAD_REQUEST_400);
        Assert.assertEquals(ex.getMessage(), "Username already registered");
    }

    @Test
    public void createAccount_BadPassword_ShouldThrowException() {
        // Arrange
        Account badAccount = new Account(null, "bob", "12345", null, null, -1, null);
        Mockito.when(repository.findByUsername(anyString())).thenReturn(null);
        ValidationException ex = null;

        // Act
        try {
            service.create(badAccount);
        } catch (ValidationException vex) {
            ex = vex;
        }

        // Assert
        Assert.assertNotNull(ex);
        Assert.assertEquals(ex.getStatus(), HttpStatus.BAD_REQUEST_400);
        Assert.assertEquals(ex.getMessage(), "Password must be at least 6 characters");
    }

    @Test
    public void createAccount_BadUsername_ShouldThrowException() {
        // Arrange
        Account badAccount = new Account(null, "b", "123456", null, null, -1, null);
        Mockito.when(repository.findByUsername(anyString())).thenReturn(null);
        ValidationException ex = null;

        // Act
        try {
            service.create(badAccount);
        } catch (ValidationException vex) {
            ex = vex;
        }

        // Assert
        Assert.assertNotNull(ex);
        Assert.assertEquals(ex.getStatus(), HttpStatus.BAD_REQUEST_400);
        Assert.assertEquals(ex.getMessage(), "Username must be at least 2 characters");
    }

    @Test
    public void authenticate_GoodAccount_ShouldHaveToken() {
        // Arrange
        Account goodAccount = new Account(null, "bob", "123456", null, null, -1, null);
        Mockito.when(repository.findByUsername(anyString())).thenReturn(goodAccount);
        Mockito.when(hasher.isPasswordCorrect(anyString(), anyString())).thenReturn(true);

        // Act
        Account authenticatedAccount = service.authenticate(goodAccount);

        // Assert
        Assert.assertNotNull(authenticatedAccount.getToken());
    }

    @Test
    public void authenticate_BadPassword_ShouldThrowException() {
        // Arrange
        Account goodAccount = new Account(null, "bob", "123456", null, null, -1, null);
        Mockito.when(repository.findByUsername(anyString())).thenReturn(goodAccount);
        Mockito.when(hasher.isPasswordCorrect(anyString(), anyString())).thenReturn(false);
        UnauthorizedResponse unauthorizedResponse = null;

        // Act
        try {
            Account authenticatedAccount = service.authenticate(goodAccount);
        } catch (UnauthorizedResponse response) {
            unauthorizedResponse = response;
        }

        // Assert
        Assert.assertNotNull(unauthorizedResponse);
        Assert.assertEquals(unauthorizedResponse.getMessage(), "Invalid username or password!");
    }

    @Test
    public void authenticate_NonExistentAccount_ShouldThrowException() {
        // Arrange
        Account goodAccount = new Account(null, "bob", "123456", null, null, -1, null);
        Mockito.when(repository.findByUsername(anyString())).thenReturn(null);
        UnauthorizedResponse unauthorizedResponse = null;

        // Act
        try {
            Account authenticatedAccount = service.authenticate(goodAccount);
        } catch (UnauthorizedResponse response) {
            unauthorizedResponse = response;
        }

        // Assert
        Assert.assertNotNull(unauthorizedResponse);
        Assert.assertEquals(unauthorizedResponse.getMessage(), "Invalid username or password!");
    }
}

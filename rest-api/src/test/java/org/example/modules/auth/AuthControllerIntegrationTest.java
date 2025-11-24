package org.example.modules.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dtos.AuthRequest;
import org.example.enums.UserRole;
import org.example.models.User;
import org.example.modules.users.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("AuthController Integration Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private AuthRequest authRequest;


    @BeforeAll
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("password123");
        testUser.setRole(UserRole.MEMBER);
        testUser.setActive(true);

        authRequest = new AuthRequest();
        authRequest.setEmail("john.doe@example.com");
        authRequest.setPassword("password123");
    }

    @Nested
    @DisplayName("Register Endpoint Tests")
    class RegisterTests {

        @Test
        void givenValidUser_whenRegister_thenReturnsCreatedAndUserDetails() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testUser)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Doe"))
                    .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                    .andExpect(jsonPath("$.role").value("MEMBER"))
                    .andExpect(jsonPath("$.active").value(true));
        }


        @Test
        void givenInvalidUser_whenRegister_thenReturnsBadRequest() throws Exception {
            User invalidUser = new User();
            invalidUser.setEmail("invalid@example.com");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidUser)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void givenAdminUser_whenRegister_thenCreatesAdminAccount() throws Exception {
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword("adminPass123");
            adminUser.setRole(UserRole.ADMIN);
            adminUser.setActive(true);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(adminUser)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.role").value("ADMIN"));
        }
    }

    @Nested
    @DisplayName("Login Endpoint Tests")
    class LoginTests {

        @BeforeEach
        void registerTestUser() throws Exception {
            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser)));
        }

        @Test
        void givenValidCredentials_whenLogin_thenReturnsOkAndToken() throws Exception {
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
        }

        @Test
        void givenInvalidPassword_whenLogin_thenReturnsUnauthorized() throws Exception {
            authRequest.setPassword("wrongPassword");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Invalid email or password"));
        }

        @Test
        void givenInvalidEmail_whenLogin_thenReturnsUnauthorized() throws Exception {
            authRequest.setEmail("nonexistent@example.com");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Invalid email or password"));
        }

        @Test
        void givenEmptyCredentials_whenLogin_thenReturnsUnauthorized() throws Exception {
            AuthRequest emptyRequest = new AuthRequest();
            emptyRequest.setEmail("");
            emptyRequest.setPassword("");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(emptyRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void givenNullCredentials_whenLogin_thenReturnsBadRequest() throws Exception {
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Authentication Flow Tests")
    class AuthenticationFlowTests {

        @Test
        void givenNewUser_whenRegisterAndLogin_thenAuthenticationSucceeds() throws Exception {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testUser)))
                    .andExpect(status().isCreated());

            String token = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(token);
            assertFalse(token.isEmpty());
        }

        @Test
        void givenMultipleUsers_whenRegisterAndLogin_thenTokensAreUnique() throws Exception {
            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser)));

            String token1 = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            User secondUser = new User();
            secondUser.setFirstName("Jane");
            secondUser.setLastName("Smith");
            secondUser.setEmail("jane.smith@example.com");
            secondUser.setPassword("password456");
            secondUser.setRole(UserRole.MEMBER);
            secondUser.setActive(true);

            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(secondUser)));

            AuthRequest secondAuthRequest = new AuthRequest();
            secondAuthRequest.setEmail("jane.smith@example.com");
            secondAuthRequest.setPassword("password456");

            String token2 = mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(secondAuthRequest)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            assertNotEquals(token1, token2, "Tokens should be unique for different users");
        }
    }
}

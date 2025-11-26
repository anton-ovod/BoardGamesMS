package org.example.modules.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.AuthRequestDto;
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
    private AuthRequestDto authRequestDto;


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

        authRequestDto = new AuthRequestDto();
        authRequestDto.setEmail("john.doe@example.com");
        authRequestDto.setPassword("password123");
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
                            .content(objectMapper.writeValueAsString(authRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
        }

        @Test
        void givenInvalidPassword_whenLogin_thenReturnsUnauthorized() throws Exception {
            authRequestDto.setPassword("wrongPassword");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequestDto)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Invalid email or password"));
        }

        @Test
        void givenInvalidEmail_whenLogin_thenReturnsUnauthorized() throws Exception {
            authRequestDto.setEmail("nonexistent@example.com");

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequestDto)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Invalid email or password"));
        }

        @Test
        void givenEmptyCredentials_whenLogin_thenReturnsUnauthorized() throws Exception {
            AuthRequestDto emptyRequest = new AuthRequestDto();
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
}

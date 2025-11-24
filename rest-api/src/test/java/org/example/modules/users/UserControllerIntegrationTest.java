package org.example.modules.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.enums.UserRole;
import org.example.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenValidUser_whenCreateAndRetrieve_thenUserIsCreatedAnd200Ok() throws Exception {
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Piotr");
        user.setEmail("jan.piotr@example.com");
        user.setPassword("password123");
        user.setRole(UserRole.MEMBER);
        user.setActive(true);

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Piotr"))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.email").value("jan.piotr@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenExistingUser_whenUpdate_thenUserIsUpdatedAnd200Ok() throws Exception {
        User user = new User();
        user.setFirstName("Anna");
        user.setLastName("Kowalska");
        user.setEmail("anna.kowalska@example.com");
        user.setPassword("password123");
        user.setRole(UserRole.MEMBER);
        user.setActive(true);

        String createResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        User updatedUser = new User();
        updatedUser.setFirstName("Anna");
        updatedUser.setLastName("Kowalska");
        updatedUser.setEmail("anna.kowalska@example.com");
        updatedUser.setPassword("newPassword123");
        updatedUser.setRole(UserRole.ADMIN);
        updatedUser.setActive(false);

        mockMvc.perform(put("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Kowalska"))
                .andExpect(jsonPath("$.email").value("anna.kowalska@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.active").value(false));
    }

}

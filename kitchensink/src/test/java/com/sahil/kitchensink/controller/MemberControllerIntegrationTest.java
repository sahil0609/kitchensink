package com.sahil.kitchensink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahil.kitchensink.BaseIntegrationTest;
import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.model.MemberDTO;
import com.sahil.kitchensink.repository.MemberRepository;
import com.sahil.kitchensink.repository.UserRespository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
public class MemberControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRespository userRespository;


    private static MemberDTO createMember(String id, String name, String phone, String email) {
        MemberDTO memberDTO = new MemberDTO();
        if(id != null) memberDTO.setId(id);
        memberDTO.setName(name);
        memberDTO.setPhoneNumber(phone);
        memberDTO.setEmail(email);
        return memberDTO;
    }

    @Nested
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    public class AdminTests {

        public static Member member1 = new Member("1", "John Doe", "john@example.com", "1234567890", "user2@gmail.com");
        public static Member member2 = new Member("2", "Jane Doe", "jane@example.com", "0987654321", "user1@gmail.com");

        @BeforeEach
        void setUp() {
            memberRepository.saveAll(List.of(member1, member2));
        }
        @AfterEach
        void tearDown() {
            memberRepository.deleteAll();
        }

        @Test
        void getAllMembers_ReturnsAllMembers_WhenUserIsAdmin() throws Exception {
            mockMvc.perform(get("/members/v1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        void deleteAllMembers_DeletesAllMembers_WhenUserIsAdmin() throws Exception {

            Assertions.assertEquals(2, memberRepository.findAll().size());
            mockMvc.perform(delete("/members/v1"))
                    .andExpect(status().isOk());

            Assertions.assertTrue(memberRepository.findAll().isEmpty());
        }

        @Test
        void DeleteMemberById_WhenUserIsAdmin() throws Exception {
            mockMvc.perform(delete("/members/v1/1"))
                    .andExpect(status().isOk());

            Assertions.assertEquals(1, memberRepository.findAll().size());
        }

        @Test
        void getMemberById_ReturnsMember_WhenUserIsAdmin() throws Exception {
            mockMvc.perform(get("/members/v1/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("John Doe"));
        }

        @Test
        void createMember_CreatesAndReturnsMember() throws Exception {
            ///create MemberDTO
            MemberDTO member = createMember("1","John Doe", "1234562290", "john@gmail.com");

            mockMvc.perform(post("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isCreated());
            Assertions.assertEquals(3, memberRepository.findAll().size());

        }

        @Test
        void UpdateMember_CreatesAndReturnsMember() throws Exception {
            ///create MemberDTO
            MemberDTO member = createMember("1", "John Doe", "1234532890", "john@gmail.com");

            mockMvc.perform(put("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isOk());
            Assertions.assertEquals(2, memberRepository.findAll().size());
            Member updtedMember = memberRepository.findById("1").get();
            Assertions.assertEquals("John Doe", updtedMember.getName());
            Assertions.assertEquals("1234532890", updtedMember.getPhoneNumber());

        }


        @Test
        void DeleteNonExistingMember_ThrowsException() throws Exception {
            mockMvc.perform(delete("/members/v1/3"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void GetNonExistingMember_ThrowsException() throws Exception {
            mockMvc.perform(get("/members/v1/3"))
                    .andExpect(status().isNotFound());
        }


    }

    @Nested
    @WithMockUser(username = "user1@gmail.com", roles = "USER")
    public class UserTests {

        public static Member member1 = new Member("1", "John Doe", "john@example.com", "1234567890", "user2@gmail.com");
        public static Member member2 = new Member("2", "Jane Doe", "jane@example.com", "0987654321", "user1@gmail.com");

        @BeforeEach
        void setUp() {
            memberRepository.saveAll(List.of(member1, member2));
        }

        @AfterEach
        void tearDown() {
            memberRepository.deleteAll();
        }


        @Test
        void getAllMembers_ReturnsUserMembers_WhenUserIsNotAdmin() throws Exception {
            memberRepository.saveAll(List.of(member1, member2));

            mockMvc.perform(get("/members/v1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        void getMemberById_ReturnsMember_WhenUserIsAuthorized() throws Exception {
            mockMvc.perform(get("/members/v1/2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Jane Doe"));
        }

        @Test
        void getMemberById_ThrowsException_WhenUserIsNotTheActualUser() throws Exception {
            mockMvc.perform(get("/members/v1/1"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void DeleteALl_ThrowsUnAuthorized_WhenUserIsNotAdmin() throws Exception {
            mockMvc.perform(delete("/members/v1"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void DeleteMemberById_ThrowsUnAuthorized_WhenUserIsNotAdminAndOwner() throws Exception {
            mockMvc.perform(delete("/members/v1/1"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void DeleteMemberById_WhenUserIsOwner() throws Exception {
            mockMvc.perform(delete("/members/v1/2"))
                    .andExpect(status().isOk());
            Assertions.assertEquals(1, memberRepository.findAll().size());
        }

        @Test
        void createMember_CreatesAndReturnsMember() throws Exception {
            ///create MemberDTO
            MemberDTO member = createMember(null,"sahil", "1234582710", "sahil@example.com");

            mockMvc.perform(post("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/members/v1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));


        }

        @Test
        void UpdateMember_CreatesAndReturnsMember() throws Exception {
            ///create MemberDTO
            MemberDTO member = createMember("2","Jaona Doe", "1234532121", "updated@example.com");

            mockMvc.perform(put("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isOk());

            Member updatedMember = memberRepository.findById("2").get();
            Assertions.assertEquals("Jaona Doe", updatedMember.getName());
            Assertions.assertEquals("1234532121", updatedMember.getPhoneNumber());
            Assertions.assertEquals("updated@example.com", updatedMember.getEmail());

        }

        @Test
        void UpdateMember_ThrowsException_WhenUserIsNotOwner() throws Exception {
            ///create MemberDTO
            MemberDTO member = createMember("1", "Jaona Doe", "1234532121", "updated@example.com");
            mockMvc.perform(put("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    class ValidationTests {

        @AfterEach
        void setUp() {
            memberRepository.deleteAll();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "s123"})
        void createMember_ReturnsBadRequest_WhenNameIsInvalid(String name) throws Exception {
            MemberDTO member = createMember(null, name, "1234567890", "abcd@gmail.com");

            mockMvc.perform(post("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"abcd", "", "   "})
        void createMember_ReturnsBadRequest_WhenEmailIsInvalid(String email) throws Exception {
            MemberDTO member = createMember(null, "sahil", "1234567890", email);

            mockMvc.perform(post("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("invalidPhoneNumberDataSource")
        @NullSource
        void createMember_ReturnsBadRequest_WhenPhoneNumberIsInvalid(String phonenumber) throws Exception {
            MemberDTO member = createMember(null, "sahil", phonenumber, "abcd@gmail.com");
            mockMvc.perform(post("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> invalidPhoneNumberDataSource() {
            return Stream.of(
                    Arguments.of("123456789212312"),
                    Arguments.of("abcd"),
                    Arguments.of("1234567890.0121"),
                    Arguments.of("1234"),
                    Arguments.of("   ")
            );
        }


        @Test
        void CreateMember_ReturnsBadRequest_WhenEmailIsAlreadyRegistered() throws Exception {
            MemberDTO member = createMember(null, "sahil", "1234567890", "abcd@gmail.com");
            memberRepository.save(new Member("1", "sahil", "abcd@gmail.com", "1234567890", "user1@gmail.com"));

            mockMvc.perform(post("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isBadRequest());
        }


        @Test
        void UpdateMember_ReturnsOk_WhenEmailIsNotUpdated() throws Exception {

            memberRepository.save(new Member("1", "sahil", "abcd@gmail.com", "1234567890", "user1@gmail.com"));
            MemberDTO member = createMember("1", "sahil", "1234567891", "abcd@gmail.com");

            mockMvc.perform(put("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isOk());

            Assertions.assertEquals(1, memberRepository.findAll().size());
            Member updatedMember = memberRepository.findById("1").get();
            Assertions.assertEquals("1234567891", updatedMember.getPhoneNumber());

        }

        @Test
        void UpdateMember_ReturnsBadRequest_WhenEmailIsUpdated() throws Exception {

            memberRepository.save(new Member("1", "sahil", "abcd@gmail.com", "1234567890", "user1@gmail.com"));
            memberRepository.save(new Member("2", "sahil", "abcd1@gmail.com", "1234567891", "user1@gmail.com"));

            MemberDTO member = createMember("1", "sahil", "1234567891", "abcd1@gmail.com");

            mockMvc.perform(put("/members/v1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isBadRequest());
        }
    }

}
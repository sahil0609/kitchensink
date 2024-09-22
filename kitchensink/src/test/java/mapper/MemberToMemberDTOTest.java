package mapper;

import com.sahil.kitchensink.model.Member;
import com.sahil.kitchensink.model.MemberDTO;
import com.sahil.kitchensink.mapper.MemberToMemberDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberToMemberDTOTest {

    @Test
    void map_ReturnsMemberDTO_WhenMemberIsValid() {
        Member member = new Member();
        member.setId("1");
        member.setName("John Doe");
        member.setEmail("john@example.com");
        member.setPhoneNumber("1234567890");

        MemberDTO result = MemberToMemberDTO.map(member);

        assertEquals("1", result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
    }

    @Test
    void map_ReturnsMemberDTOWithNullFields_WhenMemberFieldsAreNull() {
        Member member = new Member();

        MemberDTO result = MemberToMemberDTO.map(member);

        assertNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getEmail());
        assertNull(result.getPhoneNumber());
    }

    @Test
    void map_ReturnsMemberDTOWithEmptyFields_WhenMemberFieldsAreEmpty() {
        Member member = new Member();
        member.setId("");
        member.setName("");
        member.setEmail("");
        member.setPhoneNumber("");

        MemberDTO result = MemberToMemberDTO.map(member);

        assertEquals("", result.getId());
        assertEquals("", result.getName());
        assertEquals("", result.getEmail());
        assertEquals("", result.getPhoneNumber());
    }
}
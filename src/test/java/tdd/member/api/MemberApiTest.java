package tdd.member.api;

import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tdd.member.app.ConfirmMemberService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberApi.class)
public class MemberApiTest {
  @Autowired
  private MockMvc mvc;
  @MockBean
  private ConfirmMemberService confirmMemberService;


  @Test
  public void shouldCallService() throws Exception {
    // when
    mvc.perform(post("/members/{id}/confirm", "id"))
      .andExpect(status().isOk());

    // then
    BDDMockito.then(confirmMemberService)
      .should()
      .confirm("id");
  }
}

package tdd.member.api;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("classpath:init.sql")
public class MemberApiIntegrationTest {
//  @Autowired
//  private MemberRepository memberRepository;
//  @Autowired
//  private MockMvc mvc;
//
//  @Test
//  public void confirm() throws Exception {
//    // arrange(given)
//    memberRepository.save(new Member("id", MemberStatus.WAITING));
//
//    // act(when)
//    mvc.perform(post("/members/{id}/confirm", "id"))
//      .andExpect(status().isOk());
//
//    //assert(then)
//    Member m = memberRepository.findById("id");
//    assertThat(m.getStatus()).isEqualTo(MemberStatus.ACTIVE);
//  }
}

package tdd;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordMeterTest {

  @Test
  public void nullInput() {
    String password = null;
    PasswordStrength expected = PasswordStrength.INVALID;
    assertPasswordStrength(password, expected);
  }

  private void assertPasswordStrength(String password, PasswordStrength expected) {
    PasswordMeter meter = new PasswordMeter();
    PasswordStrength result = meter.meter(password);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void emptyInput() {
    assertPasswordStrength("", PasswordStrength.INVALID);
  }

  @Test
  public void meetAllRules() {
//    assertPasswordStrength("abcdABCD1234", PasswordStrength.STRONG);
//    assertPasswordStrength("123abcdABCD", PasswordStrength.STRONG);
//    assertPasswordStrength("ABCD1234abc", PasswordStrength.STRONG);
  }

  @Test
  public void meet2RulesExceptForLengthRule() {
    assertPasswordStrength("abc12AB", PasswordStrength.NORMAL);
    assertPasswordStrength("12ABabc", PasswordStrength.NORMAL);
  }

  @Test
  public void meet2RulesExceptForDigitRule() {
    assertPasswordStrength("abcdABabc", PasswordStrength.NORMAL);
  }

  @Test
  public void meet2RulesExceptForUppercaseRule() {
    assertPasswordStrength("abcde1234", PasswordStrength.NORMAL);
  }

  @Test
  public void meetOnlyLengthRule() {
    assertPasswordStrength("abcdefgjwof", PasswordStrength.WEEK);
  }
}

package tdd.pwdMeter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordMeter {

  public PasswordStrength meter(String password) {
    if (password == null) {
      return PasswordStrength.INVALID;
    }
    if (password.isEmpty()) {
      return PasswordStrength.INVALID;
    }

    int metCount = 0;

    if (containsDigit(password)) metCount++;
    if (containsUpperCase(password)) metCount++;
    if (password.length() >= 8) metCount++;

    if(metCount <= 1){
      return PasswordStrength.WEEK;
    }

    if(metCount == 2){
      return PasswordStrength.NORMAL;
    }

    return PasswordStrength.STRONG;
  }

  private boolean containsDigit(String password) {
    Pattern pattern = Pattern.compile("[0-9]");
    Matcher matcher = pattern.matcher(password);
    return matcher.find();
  }

  private boolean containsUpperCase(String password) {
    Pattern pattern = Pattern.compile("[A-Z]");
    Matcher matcher = pattern.matcher(password);
    return matcher.find();
  }
}

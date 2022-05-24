package tdd;

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

    boolean containsDigit = containsDigit(password);
    boolean containsUppercase = containsUpperCase(password);
    boolean meetLength = password.length() >= 8;
    if(meetLength && !containsDigit && !containsUppercase) {
      return PasswordStrength.WEEK;
    }

    if (!meetLength){
      return PasswordStrength.NORMAL;
    }
    if(!containsDigit) {
      return PasswordStrength.NORMAL;
    }
    if(!containsUppercase) {
      return PasswordStrength.NORMAL;
    }
    return PasswordStrength.STRONG;
  }

  private boolean containsDigit(String password) {
    Pattern pattern = Pattern.compile("[0-9]]");
    Matcher matcher = pattern.matcher(password);
    return matcher.find();
  }

  private boolean containsUpperCase(String password) {
    Pattern pattern = Pattern.compile("[A-Z]]");
    Matcher matcher = pattern.matcher(password);
    return matcher.find();
  }
}

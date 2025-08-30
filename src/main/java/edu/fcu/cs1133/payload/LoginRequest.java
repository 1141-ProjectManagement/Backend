package edu.fcu.cs1133.payload;

public class LoginRequest {
  private String officialId;
  private String password;
  // Getters and Setters
  public String getOfficialId() {
    return officialId;
  }
  public void setOfficialId(String officialId) {
    this.officialId = officialId;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
}
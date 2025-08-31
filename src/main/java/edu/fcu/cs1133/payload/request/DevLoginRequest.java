package edu.fcu.cs1133.payload.request;

// 這個 DTO 只用於開發者模式的快速登入
public class DevLoginRequest {
    private String officialId;

    public String getOfficialId() {
        return officialId;
    }

    public void setOfficialId(String officialId) {
        this.officialId = officialId;
    }
}

package edu.fcu.cs1133.model;

import lombok.Data;

@Data
public class LoginAsIdRequest {
    private int id;
    private String role;
}

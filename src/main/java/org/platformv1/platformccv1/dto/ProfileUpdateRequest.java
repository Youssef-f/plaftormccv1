package org.platformv1.platformccv1.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String displayName;
    private String bio;
    private String avatarUrl;
    private String skills;
    private String location;
}

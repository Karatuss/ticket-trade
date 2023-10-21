package com.ticket.Ticketing.dto;


import com.ticket.Ticketing.domain.repository.Gender;
import com.ticket.Ticketing.domain.repository.Role;
import lombok.*;

// Data transfer object

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserDto {
    private String id;
    private String password;
    private String name;
    private Integer age;
    private String email;
    private Gender gender;
    private String phoneNumber;
    private Role role;
//    private Byte[] faceImg;
}

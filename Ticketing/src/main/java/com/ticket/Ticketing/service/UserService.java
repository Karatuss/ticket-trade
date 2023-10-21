package com.ticket.Ticketing.service;


import com.ticket.Ticketing.domain.document.UserDocument;
import com.ticket.Ticketing.domain.repository.UserRepository;
import com.ticket.Ticketing.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


// To revise DAO
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // CREATE
    private UserDocument createUserDocument(UserDto userDto){
        return UserDocument.builder()
                .id(userDto.getId())
                .age(userDto.getAge())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .gender(userDto.getGender())
                .role(userDto.getRole())
//                .faceImg(couchDto.getFaceImg())
                .build();
    }

    // READ
    public List<UserDto> getList(String id, String password) {
        List<UserDto> userDtos = new ArrayList<>();
        List<UserDocument> userDocuments = (List<UserDocument>) userRepository.findAll();
        for(UserDocument userDocument : userDocuments){
            userDtos.add(this.convertDocumentToDto(userDocument));
        }
        return userDtos;
    }

    // UPDATE & CREATE
    public void setList(UserDto userDto){
        userRepository.save(this.createUserDocument(userDto));
    }

    // DELETE
    public void deleteList(UserDto userDto){
        userRepository.delete(this.createUserDocument(userDto));
    }


    // convert document to dto
    private UserDto convertDocumentToDto(UserDocument userDocument) {
        return UserDto.builder()
                .id(userDocument.getId())
                .age(userDocument.getAge())
                .name(userDocument.getName())
                .email(userDocument.getEmail())
                .password(userDocument.getPassword())
                .phoneNumber(userDocument.getPhoneNumber())
                .gender(userDocument.getGender())
                .role(userDocument.getRole())
//                .faceImg(userDocument.getFaceImg())
                .build();

    }

}

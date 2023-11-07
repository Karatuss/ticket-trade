package com.ticket.Ticketing.service;

import com.ticket.Ticketing.domain.document.UserDocument;
import com.ticket.Ticketing.domain.repository.UserRepository;
import com.ticket.Ticketing.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private UserDocument createUserDocument(UserDto userDto){
        return UserDocument.builder()
                .id(userDto.getId())
                .age(userDto.getAge())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .gender(userDto.getGender())
                .seat(userDto.getSeat())
                .role(userDto.getRole())
//                .faceImg(couchDto.getFaceImg())
                .build();
    }

    public void setList(UserDto userDto){
        userRepository.save(this.createUserDocument(userDto));
    }


/*
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
                .seat(userDocument.getSeat())
                .role(userDocument.getRole())
                .loginAttempts(userDocument.getLoginAttempts())
//                .faceImg(userDocument.getFaceImg())
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

    public List<String> getBookedSeat(String userId){
        Bucket userBucket = cluster.bucket(UserConfig.getStaticBucketName());
        Collection userCollection = userBucket.defaultCollection();

        GetResult userResult = userCollection.get(userId);
        JsonObject content = userResult.contentAsObject();

        List<String> userSeatList = (List<String>) content.get("seat");
        //TODO 나중에 받은 데이터 seat인지도 확인해야함

        return userSeatList;
    }

    */

}

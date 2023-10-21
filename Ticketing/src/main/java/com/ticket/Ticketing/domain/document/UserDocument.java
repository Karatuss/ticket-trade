package com.ticket.Ticketing.domain.document;


import com.ticket.Ticketing.domain.repository.Gender;
import com.ticket.Ticketing.domain.repository.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

// To remember data format

@Builder
@Data
@AllArgsConstructor
@Document
public class UserDocument {
    @Id
    @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES)
    String id;

    @Field
    String password;

    @Field
    String name;

    @Field
    Integer age;

    @Field
    String email;

    @Field
    Gender gender;

    @Field
    String phoneNumber;

    @Field
    Role role;



    // Face Image is replaced Id.jpg and Image File is assumed to be stored in another server
//    @Field
//    Byte[] faceImg;

}

//package com.example.CapstoneTestCouch.service;
//
//import com.couchbase.client.core.deps.com.google.gson.Gson;
//import com.couchbase.client.core.deps.com.google.gson.JsonElement;
//import com.couchbase.client.core.deps.com.google.gson.JsonParser;
//import com.example.CapstoneTestCouch.domain.document.UserDocument;
//import com.example.CapstoneTestCouch.domain.repository.Role;
//import com.example.CapstoneTestCouch.domain.repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class UserSecurityService {
//
//    private final UserRepository userRepository;
//
//
//    public User loadUserByUserID(HashMap<String, Object> userinfo) {
//        Optional<UserDocument> _siteUser = this.userRepository.findByIdAndPassword(userinfo.get("user_ID"), userinfo.get("user_PW"));
//        //해당 이름의 사용자 db에서 못찾은 경우
//        if (_siteUser.isEmpty()) {
//            return new User(null, null, null);
//        }
//        //해당 이름의 사용자 db에서 찾은 경우
//        UserDocument siteUser = _siteUser.get();//해당 유저 엔티티를 Optional 객체에서 꺼냄
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        if ("admin".equals(userinfo.get("role"))) {//사용자명이 admin 인 경우 ADMIN 권한 부여
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getDescription()));
//
//        } else {//그 외엔 USER 권한 부여
//            authorities.add(new SimpleGrantedAuthority(Role.ROLE_MEMBER.getDescription()));
//        }
//        return new User(siteUser.getId(), siteUser.getPassword(), authorities);//SiteUser 객체 아님!!
//    }
//
//}
package com.ticket.Ticketing.controller;


import com.ticket.Ticketing.domain.document.SeatDocument;
import com.ticket.Ticketing.dto.SeatDto;
import com.ticket.Ticketing.service.SeatService;
import com.ticket.Ticketing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

import static com.ticket.Ticketing.controller.PostController.loginUserID;


@Controller
@AllArgsConstructor
public class GetController {
    private final UserService userService;
    private final SeatService seatService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/register")
    public String setRegister(){
        return "register";
    }

    @GetMapping(value = "/seat")
    public String setSeat() { return "seat";}

    @GetMapping(value = "/manage")
    public String manage(){ return "manage"; }

    @GetMapping("/buy-setcolor.js")
    public ResponseEntity<Object> getSeatData() {
        HashMap<String, Object> getData = null;
        try {
            // data.UserSeat
            List<String> userSeatList = userService.getBookedSeat(loginUserID);

            // check whether user booked seat
            if(userSeatList == null){   // user didn't book seat yet
                getData.put("UserSeat", null);
                throw new NullPointerException();
            }

            getData.put("UserSeat", userSeatList);

            // data.BookedSeat
            List<SeatDto> bookedSeatDtoList = seatService.getSeatList();

            // convert dto to document
            List<SeatDocument> totalSeat = bookedSeatDtoList.stream()
                    .map(this::convertSeatDtoToSeatDocument)
                    .toList();

            // get booked seat list
            List<SeatDocument> bookedSeat = null;
            for(int i = 1; i <= seatService.getTotalSeats(); i++){
                if(totalSeat.get(i-1).getSold()){ // if this seat is sold
//                        assert false;
                    bookedSeat.add(totalSeat.get(i-1));
                }
            }

            // check booked seat exists
            if(bookedSeat == null){
                getData.put("BookedSeat", null);
                throw new NullPointerException();
            }

            getData.put("BookedSeat", bookedSeat);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(getData);

        } catch (NullPointerException e){ // userSeatList or bookedSeat make null exception
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(getData);
        } catch (Exception e) { // network server connect error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getData);

        }

    }
    private SeatDocument convertSeatDtoToSeatDocument(SeatDto seatDto) {
        return SeatDocument.builder()
                .id(seatDto.getId())
                .sold(seatDto.getSold())
                .build();
    }


    // Check member information
    //! 수정해서 사용하거나 지워질 수도 있음.
//        @GetMapping(value = "/register/{id}")
//        public List<Map<String, Object>> getList(@PathVariable String id, String password){
//                List<UserDto> userDtoList = userService.getList(id, password);
//
//                List<Map<String, Object>> userLoginList = new ArrayList<>();
//                for(UserDto userDto : userDtoList){
//                        Map<String,Object> result = new HashMap<>();
//                        result.put("id", userDto.getId());
//                        result.put("age", userDto.getAge());
//                        result.put("name", userDto.getName());
//                        result.put("email", userDto.getEmail());
//                        result.put("password", userDto.getPassword());
//                        result.put("phonenumber", userDto.getPhoneNumber());
//                        result.put("gender", userDto.getGender());
//                        result.put("role", userDto.getRole());
////                        result.put("faceimg", userDto.getFaceImg());
//                        userLoginList.add(result);
//                }
//                return userLoginList;
//        }





}

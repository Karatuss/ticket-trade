package com.ticket.Ticketing.controller;


import com.ticket.Ticketing.domain.repository.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@AllArgsConstructor
@SessionAttributes("user")
public class GetController {
//    private final UserService userService;
//    private final SeatService seatService;

    @GetMapping("/login")
    public String login(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser){

        if(loginUser != null) {
            return "index";
        }

        return "login";
    }

    @GetMapping(value = "/register")
    public String setRegister(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser){

        if(loginUser != null) {
            return "index";
        }

        return "register";
    }


    @GetMapping(value = "/seat")
    public String setSeat(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser,
                          HttpServletRequest request) {
        // check login
        if(loginUser == null) {
            return "index";
        }

        // if normally checked login get session
        HttpSession session = request.getSession(false);

//        session.invalidate(); // for logout because of not making logout button

        // check session is maintained
        if(session == null){
            return "index";
        }
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginUserID);

        return "seat";
    }

    //TODO 로그인 세션 확인 + 매니저만 접근 가능  기능 추가
    @GetMapping(value = "/manage")
    public String manage(){ return "manage"; }

    //TODO 로그아웃 수행시 세션 삭제

/*
    @GetMapping("/.js")
    public ResponseEntity<Object> getSeatData(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) String loginUser,
                                              HttpServletRequest request) {
        HashMap<String, Object> getData = null;

        try {
            HttpSession session = request.getSession(false);

            // check session is maintained
            if(session == null){
                throw new IllegalStateException();
            }
//            session.setAttribute(SessionConst.LOGIN_MEMBER, loginUserID);

            // check login user exists
            if(loginUser == null) {
                getData.put("UserSeat", null);
                session.invalidate();
                throw new LoginException();
            }

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

        } catch(IllegalStateException e){ // session error
          return ResponseEntity.status(HttpStatus.FOUND)
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(getData);
        } catch (NullPointerException | LoginException e) { // userSeatList or bookedSeat make null exception
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
    */


/*
    //TODO 수정해서 사용하거나 지워질 수도 있음.

     Check member information
        @GetMapping(value = "/register/{id}")
        public List<Map<String, Object>> getList(@PathVariable String id, String password){
                List<UserDto> userDtoList = userService.getList(id, password);

                List<Map<String, Object>> userLoginList = new ArrayList<>();
                for(UserDto userDto : userDtoList){
                        Map<String,Object> result = new HashMap<>();
                        result.put("id", userDto.getId());
                        result.put("age", userDto.getAge());
                        result.put("name", userDto.getName());
                        result.put("email", userDto.getEmail());
                        result.put("password", userDto.getPassword());
                        result.put("phonenumber", userDto.getPhoneNumber());
                        result.put("gender", userDto.getGender());
                        result.put("role", userDto.getRole());
//                        result.put("faceimg", userDto.getFaceImg());
                        userLoginList.add(result);
                }
                return userLoginList;
        }

*/



}

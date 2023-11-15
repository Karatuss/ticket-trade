const Event_id_Area = document.querySelector('a[User-Event]');//event-list 로 받아 온 영역 선택
const All_Seat_List_Area = document.querySelector('a[All-Seat]');//user-event-list 로 받아 온 영역 선택
const User_Seat_List_Area = document.querySelector('a[User-Seat]');//event-list 로 받아 온 영역 선택
const Event_Seat_Num_Area = document.querySelector('a[Event-Seat]');//event-list 로 받아 온 영역 선택

let event_id = parseInt(Event_id_Area.getAttribute('User-Event'));//받아온 영역으로 부터 event-list 의 이름으로 저장 된 정보를 부여
let All_Seat_List = All_Seat_List_Area.getAttribute('All-Seat');//받아온 영역으로 부터 user-event-list 의 이름으로 저장 된 정보를 부여
let User_Seat_List = User_Seat_List_Area.getAttribute('User-Seat');//받아온 영역으로 부터 event-list 의 이름으로 저장 된 정보를 부여
let Event_Seat_Num = parseInt(Event_Seat_Num_Area.getAttribute('Event-Seat'));//받아온 영역으로 부터 event-list 의 이름으로 저장 된 정보를 부여


let seats = [];
let rows = 5;
let selectedSeats = [];
let seatsPerRow = parseInt(Event_Seat_Num / rows);

document.addEventListener("DOMContentLoaded", function () {


    if (Event_Seat_Num - rows * seatsPerRow != null)
        rows += 1;
    for (let i = 1; i <= rows; i++) {
        for (let j = 1; j <= seatsPerRow; j++) {
            let seat_id = (i - 1) * seatsPerRow + j;
            seats.push({id: seat_id.toString().padStart(3, '0'), reservedBy: ''});
        }
    }


    /*
        const CleanedLoginUserSeatArray = User_Seat_List.filter(element => element !== null); //loginUserSeatArray_Before 중 null값이 들어 있을 수 있기 때문에 이를 제거 [1, null] or [null, 1] -> [1]
    */

    const seatingPlanElement = document.getElementById('seatingPlan');
    for (let i = 0; i < seats.length; i += seatsPerRow) {
        const seatingRow = document.createElement('div');
        seatingRow.classList.add('seating-row');
        seats.slice(i, i + seatsPerRow).forEach(seat => {
            const seatElement = createSeatElement(seat); // seatElement는 seat 좌석의 div을 가리킨다.
            seatingRow.appendChild(seatElement);

            if (All_Seat_List.includes(seat.id)) { //AllUserSeatArray에 있는 숫자가 내가 생성하고 있는 seat의 id와 일치하다면 실행

                if (User_Seat_List.includes(seat.id)) {
                    seat.reservedBy = 'self';
                    selectedSeats.push(seat);
                } else seat.reservedBy = 'other';

                seatElement.classList.add('reserved');
            }
        });
        seatingPlanElement.appendChild(seatingRow);
    }
    updateReservedSeatsText();

})

function createSeatElement(seat) {
    const seatElement = document.createElement('div');
    seatElement.classList.add('seat');
    seatElement.dataset.seatId = seat.id;
    seatElement.textContent = seat.id;
    seatElement.addEventListener('click', () => toggleSeat(seat));
    return seatElement;
}

function toggleSeat(seat) {
    {
        const seatElement = document.querySelector(`[data-seat-id="${seat.id}"]`);

        if (seatElement.classList.contains('reserved')) return; // 다른 사람이 예약한 좌석이거나 이미 예약된 좌석은 선택 불가능
        /*
        if (seat.reservedBy === 'other' || seat.reservedBy === 'self') return;
        */
        if (seatElement.classList.contains('selected')) {
            seatElement.classList.remove('selected');
            selectedSeats = selectedSeats.filter(selectedSeat => selectedSeat.id !== seat.id);
        } else if (selectedSeats.filter(Boolean).length >= 2) {
            alert('이미 2개 이상의 좌석을 선택하셨습니다.');
        } else {
            seatElement.classList.toggle('selected');
            selectedSeats.push(seat);
        }
    }
}

function bookSelectedSeats() {


    if (selectedSeats.length > 0) {
        const selectedSeatIds = selectedSeats.map(seat => seat.id);

        const data_book = {
            selectedSeatIds: selectedSeatIds,
            eventId: event_id
        };

        // 서버로 선택한 좌석 번호를 전송 (fetch를 사용하여 서버와 통신)
        fetch('http://localhost:8080/user-event-seat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },

            body: JSON.stringify(data_book)
        })
            .then(response => response.json())
            .then(() => {
                // if (data.success) {
                // 성공적으로 예약되었을 때 처리
                selectedSeats.forEach(seat => {
                    seat.reservedBy = 'self'; // 예약자를 'self'로 설정
                    const seatElement = document.querySelector(`[data-seat-id="${seat.id}"]`);
                    seatElement.classList.remove('selected');
                    seatElement.classList.add('reserved');
                });

                // } else {
                //     alert('다시 시도해 주세요.');
                // }

                updateReservedSeatsText();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    } else {
        alert('선택한 좌석이 없습니다.');
    }
}

function updateReservedSeatsText() {
    const reservedSeatsElement = document.querySelector('#reservedSeatsText ul');
    reservedSeatsElement.innerHTML = '';

    seats.forEach(seat => {
        if (seat.reservedBy === 'self') {
            const listItem = document.createElement('li');
            listItem.textContent = `좌석 번호: ${seat.id}`;
            reservedSeatsElement.appendChild(listItem);
        }
    });
}

document.getElementById('bookSeats').addEventListener('click', bookSelectedSeats);

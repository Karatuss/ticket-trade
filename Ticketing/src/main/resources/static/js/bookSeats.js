const seats = [];
const rows = 4;
const seatsPerRow = 5;
let selectedSeats = [];

// seats 배열 초기화
for (let i = 1; i <= rows; i++) {
    for (let j = 1; j <= seatsPerRow; j++) {
        seats.push({id: (i - 1) * seatsPerRow + j, reservedBy: ''});
    }
}

// 좌석을 나타내는 div을 생성하는 함수
function createSeatElement(seat) {
    const seatElement = document.createElement('div');
    seatElement.classList.add('seat');
    seatElement.dataset.seatId = seat.id;
    seatElement.textContent = seat.id;
    seatElement.addEventListener('click', () => toggleSeat(seat));
    return seatElement;
}

// 좌석을 선택하거나 해제하는 함수
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

// 예약하기 버튼을 눌렀을 때 선택한 좌석 번호를 서버로 전송
function bookSelectedSeats() {
    if (selectedSeats.length > 0) {
        const selectedSeatIds = selectedSeats.map(seat => seat.id);

        // 서버로 선택한 좌석 번호를 전송 (fetch를 사용하여 서버와 통신)
        fetch('http://localhost:8080/seat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({selectedSeatIds})
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

// 페이지 로드시 예약된 좌석 목록 초기화
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

document.addEventListener('DOMContentLoaded', function () {
    const User_reserved_seats = document.querySelector('a[Login-User-Seat]');//Login-User-Seat 로 받아 온 영역 선택
    const All_reserved_seats = document.querySelector('a[Seat-Reserved]');//Seat-Reserved 로 받아 온 영역 선택

    const LoginUserSeatString = User_reserved_seats.getAttribute('Login-User-Seat');//받아온 영역으로 부터 Login-User-Seat 의 이름으로 저장 된 정보를 부여
    const AllUserSeatString = All_reserved_seats.getAttribute('Seat-Reserved');//받아온 영역으로 부터 Seat-Reserved 의 이름으로 저장 된 정보를 부여

    const LoginUserSeatArray_Before = JSON.parse(LoginUserSeatString); //현재 string으로 된 정보(배열 상태)를 사용하기 위해 JSON 형식으로 파싱
    const AllUserSeatArray = JSON.parse(AllUserSeatString); //현재 string으로 된 정보(배열 상태)를 사용하기 위해 JSON 형식으로 파싱

    const CleanedLoginUserSeatArray = LoginUserSeatArray_Before.filter(element => element !== null); //loginUserSeatArray_Before 중 null값이 들어 있을 수 있기 때문에 이를 제거 [1, null] or [null, 1] -> [1]

    // 좌석을 화면에 렌더링
    const seatingPlanElement = document.getElementById('seatingPlan');
    for (let i = 0; i < seats.length; i += seatsPerRow) {
        const seatingRow = document.createElement('div');
        seatingRow.classList.add('seating-row');
        seats.slice(i, i + seatsPerRow).forEach(seat => {
            const seatElement = createSeatElement(seat); // seatElement는 seat 좌석의 div을 가리킨다.
            seatingRow.appendChild(seatElement);

            if (AllUserSeatArray.includes(seat.id)) { //AllUserSeatArray에 있는 숫자가 내가 생성하고 있는 seat의 id와 일치하다면 실행

                if (CleanedLoginUserSeatArray.includes(seat.id)) {
                    seat.reservedBy = 'self';
                    selectedSeats.push(seat);
                } else seat.reservedBy = 'other';

                seatElement.classList.add('reserved');
            }
        });
        seatingPlanElement.appendChild(seatingRow);
    }
    updateReservedSeatsText();
});


document.getElementById('bookSeats').addEventListener('click', bookSelectedSeats);


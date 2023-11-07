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
        if (seat.reservedBy === 'other' || seat.reservedBy === 'self') return; // 다른 사람이 예약한 좌석이거나 이미 예약된 좌석은 선택 불가능

        const seatElement = document.querySelector(`[data-seat-id="${seat.id}"]`);

        if (seatElement.classList.contains('selected')) {
            seatElement.classList.remove('selected');
            selectedSeats = selectedSeats.filter(selectedSeat => selectedSeat.id !== seat.id);
        } else {
            if (selectedSeats.length < 2) {
                seatElement.classList.toggle('selected');
                selectedSeats.push(seat);
            } else return;
        }
    }
}


// 좌석을 화면에 렌더링
const seatingPlanElement = document.getElementById('seatingPlan');
for (let i = 0; i < seats.length; i += seatsPerRow) {
    const seatingRow = document.createElement('div');
    seatingRow.classList.add('seating-row');
    seats.slice(i, i + seatsPerRow).forEach(seat => {
        const seatElement = createSeatElement(seat);
        seatingRow.appendChild(seatElement);
    });
    seatingPlanElement.appendChild(seatingRow);
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
            .then(data => {
                if (data.success) {
                    // 성공적으로 예약되었을 때 처리
                    selectedSeats.forEach(seat => {
                        seat.reservedBy = 'self'; // 예약자를 'self'로 설정
                        const seatElement = document.querySelector(`[data-seat-id="${seat.id}"]`);
                        seatElement.classList.remove('selected');
                        seatElement.classList.add('reserved');
                    });

                    selectedSeats = [];
                } else {
                    alert('다시 시도해 주세요.');
                }

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

// 예매된 좌석 정보를 서버로부터 받아와서 업데이트
function fetchBookedSeats() {
    fetch('http://localhost:8080/seat', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const bookedSeatIds = data.bookedSeatIds;

                seats.forEach(seat => {
                    if (bookedSeatIds.includes(seat.id)) {
                        seat.reservedBy = 'other'; // 다른 예약자로 설정
                        const seatElement = document.querySelector(`[data-seat-id="${seat.id}"]`);
                        seatElement.classList.add('reserved');
                    }
                });

                updateReservedSeatsText();
            } else {
                console.error('Error fetching booked seats:', data.error);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

updateReservedSeatsText();

document.getElementById('bookSeats').addEventListener('click', bookSelectedSeats);

// 페이지 로드시 예약된 좌석 정보 초기화
fetchBookedSeats();

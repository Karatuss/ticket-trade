document.addEventListener('DOMContentLoaded', function(){ //html이 로드되고 파싱된 후 실행
    var seatContainer = document.getElementById('seat-container');

    fetch(url)
    .then(response => {
        if(!response.ok){
            throw new Error('네트워크 오류');
        }
        return response.json();
    })
    .then(data => { // 내가 이미 예약한 좌석과 다른 사람이 예약한 좌석의 정보를 받아옴
        MySeat.push(data.UserSeat);
        for(let i = 0; i < MySeat.length; i++)
        {
            seats[MySeat[i]].booked = true;
        }

        for(let i = 0; i < data.BookedSeat.length; i++)
        {
            seats[data.BookedSeat[i]].booked = true;
        } 

    })
    .catch(error => console.error('Error', error))

    // 좌석 당 구역을 나누어 css를 설정
    for (let i = 0; i < seats.length; i++) { 
        seat.classList.add('seat');
        seat.id = seats[i].id; // seat는 'div'형태, 즉 구역의 형태임으로 각 구역 별 seats에 들어있는 id 번호를 부여

        if (seats[i].booked) { 
            seat.classList.add('reserved');
        }

        seatContainer.appendChild(seat); // seat라는 div을 seatContainer라는 div의 자식요소로 추가(seatContainer 구역 안에 seat 구역이 존재)
    }

    // seat로 지정 된 모든 div 항목들을 가져와 seatsDOM에 저장해준다.
    var seatsDOM = document.querySelectorAll('.seat'); 

    // 위에서 가져온 seatsDOM에 대햐여 각각 기능을 실행
    seatsDOM.forEach(function(seatDOM) { 
        seatDOM.addEventListener('click', function() {

            // 만약 해당 구역을 선택했을 때 이미 reserved 구역이라면 (이미 예약된 좌석이라면) 함수를 빠져나온다.
            if (this.classList.contains('reserved')) 
            { 
                return;
            }

            else
            {
                this.classList.toggle('selected'); //클릭했을 시 해당 div을 selected라는 항목의 css가 적용되도록 설정
            }
            
        });
    });

});

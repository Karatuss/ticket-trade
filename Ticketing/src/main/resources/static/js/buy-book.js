//좌석 예약 버튼 부분
document.getElementById("bookSeats").addEventListener("click", function(){
    var selectedSeats = document.querySelectorAll('.selected'); // 위 div 중 selected 로 선택 된 div 항목들만 가져옴
    var selectedSeatNumbers = [];

    selectedSeats.forEach(function() {
        selectedSeatNumbers.push(this.id);
    });

    var MS = JSON.stringify({ seat: MySeat.concat(selectedSeatNumbers) }); // MySeat에 내가 선택한 영역의 번호 추가

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json")

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: MS,
        redirect: 'follow'
    };

    fetch('http://localhost:8080/seat', requestOptions)

    .then(response => {
        if(!response.ok) {
            throw new Error('네트워크 오류');
        }
        else{
            alert('좌석 예약이 완료되었습니다!');
        }
        return response.json();
    })
    .catch(error => console.error('Error', error));

});

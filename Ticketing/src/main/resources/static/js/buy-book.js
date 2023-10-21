//좌석 예약 버튼 부분
document.getElementById("bookSeats").addEventListener("click", function(){
    var selectedSeats = document.querySelectorAll('.selected'); // 위 div 중 selected 로 선택 된 div 항목들만 가져옴
    var selectedSeatNumbers = [];

    selectedSeats.forEach(function() {
        selectedSeatNumbers.push(this.id);
    });

    MySeat = MySeat.concat(selectedSeatNumbers); // MySeat에 내가 선택한 영역의 번호 추가

    fetch('url', {
        // POST 요청을 보냄
        method: 'POST',
        // 파일이 JSON형식임을 명시
        headers: {
            'Content-Type' : 'application/json'
        },
        // data의 정보를 JSON 형식으로 만들어 서버로 전송
        body: JSON.stringify(MySeat)
    })
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

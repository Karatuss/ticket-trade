document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("generate").addEventListener("click", function () {
        let eventName = document.getElementById("event_name").value;
        let event_col = document.getElementById("event_seatCol").value;
        let event_row = document.getElementById("event_seatRow").value;
        let event_start = document.getElementById("event_startTime").value;
        let event_end = document.getElementById("event_endTime").value;

        let data = {
            'eventName': eventName,
            'row': event_row,
            'col': event_col,
            'eventStart': event_start,
            'eventEnd': event_end
        };

        FetchEventInfo(data);
    });
});

function FetchEventInfo(data) {

    const data_event = {
        eventName: data.eventName,
        row: data.row,
        col: data.col,
        eventStart: data.eventStart,
        eventEnd: data.eventEnd
    };

    // 파일이 JSON형식임을 명시
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const requestOptions = {
        // POST 요청을 보냄
        method: 'POST',
        headers: myHeaders,
        // data의 정보를 JSON 형식으로 만들어 서버로 전송
        body: JSON.stringify(data_event),
        redirect: 'follow'
    };


    fetch('http://localhost:8080/manager-event-generate', requestOptions) // 서버에 eventid를 보내고 user-event-seat.html로 이동
        // 서버로부터 받은 응답을 response에 json 형식으로 파싱 (response는 서버에서 받은 전체 응답을 의미)
        .then(response => response.json()) // 응답을 JSON 형식으로 파싱
        .then(() => {
            window.location.href = 'manager-event';
        })
        .catch(error => console.error('Error', error));
}

document.addEventListener('DOMContentLoaded', function () {
    const Event_list = document.querySelector('a[Event-List]');//event-list 로 받아 온 영역 선택

    let EventlistString = Event_list.getAttribute('Event-List');//받아온 영역으로 부터 event-list 의 이름으로 저장 된 정보를 부여

    for (let i = 1; i < 20; i++) {
        let find = i + "=";
        let replace = '"' + i + '":';
        EventlistString = EventlistString.replace(new RegExp(find, 'g'), replace);
    }

    const EventlistArray = JSON.parse(EventlistString); //현재 string으로 된 정보(배열 상태)를 사용하기 위해 JSON 형식으로 파싱

    for (const key in EventlistArray) { // json 객채의 key 개수만큼 실행
        AddEventDiv(EventlistArray, key);
    }


    document.getElementById("event_remove_button").addEventListener("click", function () {
        let divElement = document.getElementById('event_remove_div');
        divElement.style.display = 'block';

        document.getElementById("remove").addEventListener("click", function () {
            let Event_name = document.getElementById('event_remove').value;
            for (const key in EventlistArray)
            {
                if (Event_name === EventlistArray[key].eventName) {
                    FetchRemoveEvent(EventlistArray[key].id);
                    Event_name = "";
                    divElement.style.display = 'none';
                }
            }
        });
    });
});


function AddEventDiv(EventList, key) { //서버로 부터 받아 온 정보를 토대로 이벤트명 + div 만드는 함수
    const EventListDiv = document.getElementById('manage_event'); // 'event'라는 id를 가진 요소를 가져옴
    const EventNameElement_manage = document.createElement('div'); // 새로운 div 요소를 생성

    EventNameElement_manage.id = key; // EventNameElement의 div id를 서버로부터 받아 온 eventlist의 id로 설정
    EventNameElement_manage.classList.add('event');

    // EventNameElement에 서버로 부터 받아온 이벤트 이름을 삽입
    EventNameElement_manage.innerHTML = ` 
      <h2>${EventList[key].eventName}</h2>
    `;


    EventNameElement_manage.addEventListener('click', function () {
        // 이미 존재하는 자식 div 확인
        const existingChildDiv = document.getElementById("childDiv");

        // 자식 div가 이미 존재한다면 제거
        if (existingChildDiv) {
            existingChildDiv.remove();
        } else {
            FetchEventId_manage(EventNameElement_manage)
        }
    });

    EventListDiv.appendChild(EventNameElement_manage); // EventNameElement를 EventlistDiv의 자식 div으로 설정

}

function FetchEventId_manage(ClickDiv) { //특정 이벤트명을 클릭 시 서버로 해당 div 의 eventid 전송 및 user-event-seat로 이동

    const data_manager = {
        eventId: ClickDiv.id,
        remove: "false"
    };

    // 파일이 JSON형식임을 명시
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const requestOptions = {
        // POST 요청을 보냄
        method: 'POST',
        headers: myHeaders,
        // data의 정보를 JSON 형식으로 만들어 서버로 전송
        body: JSON.stringify(data_manager),
        redirect: 'follow'
    };

    fetch('http://localhost:8080/manager-event', requestOptions) // 서버에 eventid를 보내고 user-event-seat.html로 이동
        // 서버로부터 받은 응답을 response에 json 형식으로 파싱 (response는 서버에서 받은 전체 응답을 의미)
        .then(response => response.json()) // 응답을 JSON 형식으로 파싱
        .then((data) => {

            if (!data.eventParticipantsList[0]) {
                alert('이벤트를 예약한 유저가 없습니다.');
            } else {

                const childDiv = document.createElement("div");
                childDiv.id = "childDiv";
                for (let j = 0; j < data.eventParticipantsList.length; j++) {
                    // Initialize the array if not already defined
                    let list = JSON.parse(data.eventParticipantsList[j]);

                    let list_seatId = "";
                    for (let i = 0; i < list.seat.length; i++) {
                        if (list.seat[i])
                            if (list_seatId)
                                list_seatId += ', ' + parseInt(list.seat[i].match(/\d{3}$/), 10);
                            else
                                list_seatId = parseInt(list.seat[i].match(/\d{3}$/), 10);
                    }

                    childDiv.innerHTML += `Seat Number: ${list_seatId} <br>ID: ${list.id}, Phone Number: ${list.phoneNumber} <br>`;
                }

                ClickDiv.appendChild(childDiv);
            }

        })
        .catch(error => console.error('Error', error));
}

function FetchRemoveEvent(Event_Id){

    const data_manager = {
        remove: true,
        eventId: Event_Id
    };

    // 파일이 JSON형식임을 명시
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const requestOptions = {
        // POST 요청을 보냄
        method: 'POST',
        headers: myHeaders,
        // data의 정보를 JSON 형식으로 만들어 서버로 전송
        body: JSON.stringify(data_manager),
        redirect: 'follow'
    };

    fetch('http://localhost:8080/manager-event', requestOptions) // 서버에 eventid를 보내고 user-event-seat.html로 이동
        // 서버로부터 받은 응답을 response에 json 형식으로 파싱 (response는 서버에서 받은 전체 응답을 의미)
        .then(response => response.json()) // 응답을 JSON 형식으로 파싱
}
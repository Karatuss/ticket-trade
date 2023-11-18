document.addEventListener('DOMContentLoaded', function () {
    const Event_list = document.querySelector('a[event-list]');//event-list 로 받아 온 영역 선택
    const User_event_list = document.querySelector('a[user-event-list]');//user-event-list 로 받아 온 영역 선택

    let EventlistString = Event_list.getAttribute('event-list');//받아온 영역으로 부터 event-list 의 이름으로 저장 된 정보를 부여
    let UsereventlistString = User_event_list.getAttribute('user-event-list');//받아온 영역으로 부터 user-event-list 의 이름으로 저장 된 정보를 부여

    console.log(EventlistString);
    console.log(UsereventlistString);

    EventlistString = EventlistString.replace(/GetResult/g, '');

// 문자열을 파싱 가능한 형식으로 변환
    for (let i = 1; i < 20; i++) {
        let find = i + "=";
        let replace = '"' + i + '":';
        EventlistString = EventlistString.replace(new RegExp(find, 'g'), replace);
        UsereventlistString = UsereventlistString.replace(new RegExp(find, 'g'), replace);
    }

    const EventlistArray = JSON.parse(EventlistString); //현재 string으로 된 정보(배열 상태)를 사용하기 위해 JSON 형식으로 파싱
    const UsereventlistArray = JSON.parse(UsereventlistString); //현재 string으로 된 정보(배열 상태)를 사용하기 위해 JSON 형식으로 파싱

    console.log(EventlistArray);

    for (const key in EventlistArray) { // json 객채의 key 개수만큼 실행
        AddEventDiv(EventlistArray, key, UsereventlistArray);
    }

});

function AddEventDiv(EventList, key, UserEventList) { //서버로 부터 받아 온 정보를 토대로 이벤트명 + div 만드는 함수
    const EventListDiv = document.getElementById('event'); // 'event'라는 id를 가진 요소를 가져옴
    const EventNameElement = document.createElement('div'); // 새로운 div 요소를 생성
    const EventNameElement_time = document.createElement('div');

    EventNameElement.id = key; // EventNameElement의 div id를 서버로부터 받아 온 eventlist의 id로 설정
    EventNameElement.classList.add('event');

    // EventNameElement에 서버로 부터 받아온 이벤트 이름을 삽입
    EventNameElement.innerHTML = ` 
      <h2>${EventList[key].eventName}</h2>
    `;
    EventNameElement_time.innerHTML = `
      <h4>예매기간 : ${EventList[key].eventStart} ~ ${EventList[key].eventEnd}</h4>
    `;

    EventNameElement.appendChild(EventNameElement_time);

    //UserEventList 객체가 
    if (UserEventList.hasOwnProperty(key)) {
        const attendingElement = document.createElement('h4');
        attendingElement.textContent = '참여중인 이벤트';
        EventNameElement.appendChild(attendingElement);
    }

    EventNameElement.addEventListener('click', function () {
        FetchEventId(EventNameElement)
    });

    EventListDiv.appendChild(EventNameElement); // EventNameElement를 EventlistDiv의 자식 div으로 설정

}


function FetchEventId(ClickDiv) { //특정 이벤트명을 클릭 시 서버로 해당 div 의 eventid 전송 및 user-event-seat로 이동

    const data = {
        eventId: ClickDiv.id
    };

    // 파일이 JSON형식임을 명시
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const requestOptions = {
        // POST 요청을 보냄
        method: 'POST',
        headers: myHeaders,
        // data의 정보를 JSON 형식으로 만들어 서버로 전송
        body: JSON.stringify(data),
        redirect: 'follow'
    };

    fetch('http://localhost:8080/user-event', requestOptions) // 서버에 eventid를 보내고 user-event-seat.html로 이동
        // 서버로부터 받은 응답을 response에 json 형식으로 파싱 (response는 서버에서 받은 전체 응답을 의미)
        .then(response => response.json()) // 응답을 JSON 형식으로 파싱
        .then(() => {
            window.location.href = 'user-event-seat';
        })
        .catch(error => console.error('Error', error));
}



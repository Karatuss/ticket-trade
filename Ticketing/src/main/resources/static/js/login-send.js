function login(){
    var id = document.getElementById("user_ID").value;
    var password = document.getElementById("user_PW").value;

    //서버에 보낼 id정보와 password 정보를 data라는 형식의 파일에 저장
    var data = {
        user_ID: id,
        user_PW: password
    };

    // 파일이 JSON형식임을 명시
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    //서버에 보내줄 파일의 양식
    var requestOptions = {
        // POST 요청을 보냄
        method: 'POST',
        headers: myHeaders,
        // data의 정보를 JSON 형식으로 만들어 서버로 전송
        body: JSON.stringify(data),
        redirect: 'follow'
    };

    //서버로 POST요청 보내기
    fetch('http://localhost:8080/login', requestOptions)
        // 서버로부터 받은 응답을 response에 json 형식으로 파싱 (response는 서버에서 받은 전체 응답을 의미)
        .then(response => {
            if(!(response.ok)) {
                throw new Error('네트워크 오류');
            }
            else if(response.service_unavailable){
                alert('로그인 시도 횟수를 초과하였습니다.\n 다음에 다시 시도해 주세요')
            }
            return response.json();
        })

        // 서버로 부터 받은 본문의 내용을 data에 저장 후 응답 처리
        .then(data => {
            if(data.loginSuccess){
                alert('로그인 되었습니다!');
                //
                if(data.identify === "MEMBER"){
                    window.location.href = 'seat';
                }
                else if(data.identify === "MANAGER"){
                    window.location.href = 'manager';
                }
                else{
                    alert('identify 오류');
                }

            }
            else{
                alert('로그인 실패하였습니다. 다시 시도해주세요.');
                document.getElementById("user_ID").value = "";
                document.getElementById("user_PW").value = "";

            }
        })
        .catch(error => console.error('Error', error));
}

document.addEventListener("keyup", function(event){
    if(event.code === 'Enter'){
        login();
    }
})

document.addEventListener('DOMContentLoaded', function() {
    var inputElement = document.getElementById('user_ID');
    inputElement.focus(); // 자동으로 포커스 주기
});

document.getElementById("login").addEventListener("click", login);

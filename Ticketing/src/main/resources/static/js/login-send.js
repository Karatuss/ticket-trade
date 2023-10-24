document.getElementById("login").addEventListener("click", function(){
    var id = document.getElementById("user_ID").value;
    var password = document.getElementById("user_PW").value;

    //서버에 보낼 id 정보와 password 정보를 data 라는 형식의 파일에 저장
    var data = JSON.stringify({
        user_ID: id,
        user_PW: password
    });

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: data,
        redirect: 'follow'
    };

    //서버로 POST 요청 보내기
    fetch('http://localhost:8080/login', requestOptions)

        // 서버로부터 받은 응답을 response에 json 형식으로 파싱 (response는 서버에서 받은 전체 응답을 의미)
        .then(response => {
            if(!response.ok) {
                throw new Error('네트워크 오류');
            }
            return response.json();
        })

        // 서버로 부터 받은 본문의 내용을 data에 저장 후 응답 처리
        .then(data => {
            if(data.loginSuccess){
                alert('로그인 되었습니다!');
                if(data.identify === "MEMBER"){
                    window.location.href = 'seat';
                } else if(data.identify === "ADMIN"){
                    window.location.href = 'manage';
                } else{
                    alert('Role error');
                }

            } else{
                alert('로그인 실패하였습니다. 다시 시도해주세요.');
                document.getElementById("user_ID").value = "";
                document.getElementById("user_PW").value = "";

            }
        })
    .catch(error => console.error('Error', error));
});
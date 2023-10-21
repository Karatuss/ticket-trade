document.getElementById("logout").addEventListener("click",function(){

    var logout = {logout : true};

    fetch('url', {
        // POST 요청을 보냄
        method: 'POST',
        // 파일이 JSON형식임을 명시
        headers: {
            'Content-Type' : 'application/json'
        },
        // data의 정보를 JSON 형식으로 만들어 서버로 전송
        body: JSON.stringify(logout)
    })
    .then(response => {
        if(!response.ok) {
            throw new Error('네트워크 오류');
        }
        else{
            alert('로그아웃 되었습니다.');
            window.location.href = 'login.html';
        }
        return response.json();
    })
    .catch(error => console.error('Error', error));

});


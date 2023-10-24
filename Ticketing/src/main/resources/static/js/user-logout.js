document.getElementById("logout").addEventListener("click",function(){

    var logout = JSON.stringify({logout : true});

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json")

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: logout,
        redirect: 'follow'
    };

    fetch('http://localhost:8080/seat', requestOptions)
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


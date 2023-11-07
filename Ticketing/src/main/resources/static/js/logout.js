document.getElementById("logout").addEventListener("click", function () {

    fetch('http://localhost:8080/logout', {
        // GET 요청을 보냄
        method: 'GET'
    })

    window.location.href = '/index';
    location.reload();

});

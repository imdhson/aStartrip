
function jsonTest(articleNum) {
    console.log("jsonTest()")
    // 원격 서버에서 JSON 데이터를 가져오기
    fetch('/api/article/' + articleNum) // 데이터가 있는 URL
        .then(response => {
            // HTTP 응답을 JSON으로 변환
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // JSON 데이터 처리
            console.log(data);
        })
        .catch(error => {
            // 에러 처리
            console.error('There was a problem with your fetch operation:', error);
        });
}
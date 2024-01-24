//WebSocket으로 통신하기
//마지막 keypress 부터 5초간 keypress 없으면 websocket으로 발신
//keypress 없는 상태의 경우 event 발생마다 websocket으로 수정된 카드 json 수신해서 화면 그리기

const r01 = document.querySelector(".r01").cloneNode(true)
const r02 = document.querySelector(".r02").cloneNode(true)
const w01 = document.querySelector(".w01").cloneNode(true)
const w02 = document.querySelector(".w02").cloneNode(true)
const v01 = document.querySelector(".v01").cloneNode(true)
const v02 = document.querySelector(".v02").cloneNode(true)
const title = document.querySelector("#articleTitle")

const main = document.querySelector("main")
const addArticle = document.querySelector(".grid-add")

function jsonR(articleNum) {
    const uri = '/api/article/' + articleNum

    fetch(uri) // URI
        .then(response => {
            // HTTP 응답을 JSON으로 변환
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(jsonData => {
            // 게시글 json 으로 처리 시작
            articleDetailView(jsonData)

            console.log(jsonData);
        })
        .catch(error => {
            // 에러 처리
            console.error('There was a problem with your fetch operation:', error);
        });
}

// ____ articleDTO JSON ____
//CardList
// articleNum: 5
// cardType: "V01"
// id: 10
// llmStatus: "COMPLETED"
// llmresponse0: "LLMResponse0"
// llmresponse1: "LLMResponse1"
// llmresponse2: "LLMResponse2"
// userInput0: "USERINPUT0"

// content: null
// hit: 0
// modDate: "2024-01-23T11:07:46.007675"
// num: 2
// regDate: "2024-01-23T11:07:46.007675"
// title: null
// visibleBoard: false
// writer: "1name"

function articleDetailView(jsonData) {

    title.value = jsonData.title
    titleWS(jsonData.num, title)

    let cards = jsonData.cardDTOList
    cards.forEach(card => {
        switch (card.cardType) {
            case "R01":
                child = r01.cloneNode(true)

                child.querySelector("#llmresponse0").value = card.llmresponse0

                child.style.display = "flex"
                main.appendChild(child)
                break;
            case "R02":
                child = r02.cloneNode(true)

                child.querySelector("#userInput0").value = card.userInput0
                child.querySelector("#llmresponse0").text = card.llmresponse0
                child.querySelector("#llmresponse1").text = card.llmresponse1
                child.querySelector("#llmresponse2").text = card.llmresponse2

                child.style.display = "flex"
                main.appendChild(child)
                break;
            case "W01":
                child = w01.cloneNode(true)

                child.querySelector("#userInput0").value = card.userInput0
                child.querySelector("#llmresponse0").value = card.llmresponse0

                child.style.display = "flex"
                main.appendChild(child)
                break;
            case "W02":
                child = w02.cloneNode(true)

                child.querySelector("#userInput0").value = card.userInput0
                child.querySelector("#llmresponse0").value = card.llmresponse0

                child.style.display = "flex"
                main.appendChild(child)
                break;
            case "V01":
                child = v01.cloneNode(true)

                child.querySelector("#userInput0").value = card.userInput0
                child.querySelector("#llmresponse0").value = card.llmresponse0

                child.style.display = "flex"
                main.appendChild(child)
                break;
            case "V02":
                child = v02.cloneNode(true)

                child.querySelector("#userInput0").value = card.userInput0
                child.querySelector("#llmresponse0").value = card.llmresponse0
                child.querySelector("#llmresponse1").value = card.llmresponse1
                child.querySelector("#llmresponse2").value = card.llmresponse2

                child.style.display = "flex"
                main.appendChild(child)
                break;
        }//Switch 문 종료
        cardWS(card, child)

    });

    addArticle.style.display = "grid"
    main.appendChild(addArticle)
}

function titleWS(articleNum, dom) {
    let webSocket = new WebSocket('ws://localhost:8080/article-ws')
    webSocket.onopen = function (event) {
        console.log("커넥션 열림")
    }
    webSocket.onmessage = function (event) {
        console.log('Message from server: ', event.data);
    };
    webSocket.onclose = function (event) {
        console.log("커넥션 닫힘 ");
    }

    dom.addEventListener('keypress', sendArticle)
    dom.addEventListener('blur', sendArticle)

    function sendArticle(event) {
        var jsonMessage = JSON.stringify({ num: articleNum, title: dom.value })
        webSocket.send(jsonMessage)
    }

}

function cardWS(card, dom) {
    let webSocket = new WebSocket('ws://localhost:8080/card-ws')
    webSocket.onopen = function (event) {
        console.log("커넥션 열림")
    }
    webSocket.onmessage = function (event) {
        console.log('Message from server: ', event.data);
    };
    webSocket.onclose = function (event) {
        console.log("커넥션 닫힘 ");
    }

    dom.addEventListener('keypress', sendCard)
    dom.addEventListener('blur', sendCard)

    function sendCard(event) {
        let jsonObj = {
            id: card.id,
            cardType: card.cardType,
            llmStatus: card.llmStatus
        }

        switch (card.cardType) {
            case "R01":
                jsonObj.llmresponse0 = dom.querySelector("#llmresponse0").value
                break
            case "R02":
                jsonObj.userInput0 = dom.querySelector("#userInput0").value
                jsonObj.llmresponse0 = dom.querySelector("#llmresponse0").value
                jsonObj.llmresponse1 = dom.querySelector("#llmresponse1").value
                jsonObj.llmresponse2 = dom.querySelector("#llmresponse2").value
                break
            case "W01":
                jsonObj.userInput0 = dom.querySelector("#userInput0").value
                jsonObj.llmresponse0 = dom.querySelector("#llmresponse0").value
                break
            case "W02":
                jsonObj.userInput0 = dom.querySelector("#userInput0").value
                jsonObj.llmresponse0 = dom.querySelector("#llmresponse0").value
                break
            case "V01":
                jsonObj.userInput0 = dom.querySelector("#userInput0").value
                jsonObj.llmresponse0 = dom.querySelector("#llmresponse0").value
                break
            case "V02":
                jsonObj.userInput0 = dom.querySelector("#userInput0").value
                jsonObj.llmresponse0 = dom.querySelector("#llmresponse0").value
                jsonObj.llmresponse1 = dom.querySelector("#llmresponse1").value
                jsonObj.llmresponse2 = dom.querySelector("#llmresponse2").value
                break
        }
        jsonMessage = JSON.stringify(jsonObj)
        webSocket.send(jsonMessage)
    }
}
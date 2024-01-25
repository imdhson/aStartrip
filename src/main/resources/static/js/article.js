// articleDTO 에 변화(제목, card add) 가 있어서 메시지 수신시
// main 삭제 이후  articledetailview 재호출 구현
const server_address = 'localhost:8080' //http, ws 등 제외해야함

const r01 = document.querySelector(".r01").cloneNode(true)
const r02 = document.querySelector(".r02").cloneNode(true)
const w01 = document.querySelector(".w01").cloneNode(true)
const w02 = document.querySelector(".w02").cloneNode(true)
const v01 = document.querySelector(".v01").cloneNode(true)
const v02 = document.querySelector(".v02").cloneNode(true)
const title = document.querySelector("#articleTitle")

const main = document.querySelector("main")
const addArticle = document.querySelector(".grid-add")

let last_interaction = 0;

function start(articleNum) {
    const uri = 'http://' + server_address + '/api/article/' + articleNum
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
    titleWS(jsonData.num, title) //title 관리 웹 소켓

    let cards = jsonData.cardDTOList
    cards.forEach(card => {
        cardBuild(card); //card 받아와서 그리기
        cardWS(card, child) //card 관리 웹 소켓

    });

    addArticle.style.display = "grid"
    main.appendChild(addArticle)
}

function titleWS(articleNum, dom) { // card 마지막 id가 js에 저장된 것과 일치하지 않으면 card 추가
    let webSocket = new WebSocket('ws://' + server_address + '/article-ws')
    webSocket.onopen = function (event) {
        console.log("커넥션 열림")
    }
    webSocket.onmessage = function (event) {
        jsonData = JSON.parse(event.data) //여기서 메시지 받아서 마지막 이벤트 이후 3초 지났을 때에만 대입하도록 수정
        const current_time = new Date().getTime();
        if (current_time - last_interaction >= 3000) {
            dom.value = jsonData.title;
            console.log("titleWS 대입됨: ", jsonData)
        }
    };
    webSocket.onclose = function (event) {
        console.log("커넥션 닫힘 ");
    }

    dom.addEventListener('keyup', sendTitle)
    dom.addEventListener('blur', sendTitle)


    function sendTitle(event) {
        last_interaction = new Date().getTime()
        var jsonMessage = JSON.stringify({ num: articleNum, title: dom.value })
        webSocket.send(jsonMessage)
    }

}

function cardWS(card, dom) {
    let webSocket = new WebSocket('ws://' + server_address + '/card-ws')
    webSocket.onopen = function (event) {
        console.log("커넥션 열림")
    }
    webSocket.onmessage = function (event) {
        jsonData = JSON.parse(event.data) //여기서 메시지 받아서 마지막 이벤트 이후 3초 지났을 때에만 대입하도록 수정
        console.log(jsonData)
        const current_time = new Date().getTime();
        if (current_time - last_interaction >= 3000) {
            dom.remove()
            cardBuild(card)
            console.log("card 재생성됨:: ", jsonData)
        }
    };
    webSocket.onclose = function (event) {
        console.log("커넥션 닫힘 ");
    }

    dom.addEventListener('keyup', sendCard)
    dom.addEventListener('blur', sendCard)

    function sendCard(event) {
        last_interaction = new Date().getTime()


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
function addCard(articleNum, cardType1) {
    console.log(articleNum, cardType1)
    const cardDTO = {
        cardType: cardType1
    }
    fetch('http://' + server_address + '/api/article/' + articleNum + '/add-card', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDTO)
    })
        .then(response => response.json())
        .then(data => {
            console.log("성공", data)
        })
        .catch((error => {
            console.error("에러: ", error)
        }));
}

function cardBuild(card) {
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
}
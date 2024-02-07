import { cube_three } from "/js/three.js"
import { uploadFile, loadFile } from "/js/file.js"
import { showModal, closeModal } from "/js/modal.js"

const server_address = 'localhost:8080' //http, ws , / 등 제외해야함

const r01 = document.querySelector(".r01").cloneNode(true)
const r02 = document.querySelector(".r02").cloneNode(true)
const w01 = document.querySelector(".w01").cloneNode(true)
const w02 = document.querySelector(".w02").cloneNode(true)
const v01 = document.querySelector(".v01").cloneNode(true)
const v02 = document.querySelector(".v02").cloneNode(true)
const p01 = document.querySelector(".p01").cloneNode(true)
const p02 = document.querySelector(".p02").cloneNode(true)
const p03 = document.querySelector(".p03").cloneNode(true)

const cardsDOM = document.querySelector(".cards")
const addArticle = document.querySelector(".grid-add")

let cardWS_webSocket_arr = []
let articleWS_webSocket;
let last_interaction = 0;
let keycount = 0;
let weakmap = new WeakMap();
let qrToggle_bool = false;

start(articleNum)
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
            const title = document.querySelector("#articleTitle")
            title.value = jsonData.title
            titleWS(jsonData.num, title) //article 관리 웹 소켓
            articleDetailView(jsonData)
        })
        .catch(error => {
            // 에러 처리
            console.error('There was a problem with your fetch operation:', error);
        });


    editPermission ? true : showModal("편집 권한이 없어서 열람만 가능해요")
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
    articleWS(jsonData.num, cardsDOM) //article 수정 시 수신해서 새로 그리는 websocket

    let cards = jsonData.cardDTOList
    cards.forEach(card => {
        cardBuild(card, cardsDOM, false); //card 받아와서 카드 하나씩 그리기
    });

    if (editPermission) {
        addArticle.style.display = "grid"
        cardsDOM.appendChild(addArticle)
    }
}

function titleWS(articleNum, dom) {
    let titleWS_webSocket = new WebSocket('ws://' + server_address + '/title-ws')
    titleWS_webSocket.onopen = function (event) {

        let jsonMessage = JSON.stringify({
            num: articleNum
        })
        titleWS_webSocket.send(jsonMessage)
    }
    titleWS_webSocket.onmessage = function (event) {
        let newTitle = event.data //여기서 메시지 받아서 마지막 이벤트 이후 1초 지났을 때에만 대입하도록 수정
        const current_time = new Date().getTime();
        if (current_time - last_interaction >= 1000) {
            dom.value = newTitle;
            console.log("titleWS 대입됨: ", newTitle)
        }
    };
    titleWS_webSocket.onclose = function (event) {
        console.log("커넥션 닫힘 titleWS");
    }

    dom.addEventListener('keyup', function (event) {
        last_interaction = new Date().getTime()
        keycount += 1
        if (keycount % 10 === 0) {
            if (editPermission) {
                sendTitle(event, dom)
            } else {
                showModal("편집 권한이 없어요. 제목 변경 실패")
            }
        }
    })
    dom.addEventListener('blur', function (event) {
        last_interaction = new Date().getTime()
        sendTitle(event, dom)
    })


    function sendTitle(event, dom) {
        let jsonMessage = JSON.stringify({ num: articleNum, title: dom.value })
        titleWS_webSocket.send(jsonMessage)
    }
}

function articleWS(articleNum, dom) {
    //card add 등을 통해 article 전체가 변경된 경우에 사용됨
    articleWS_webSocket = new WebSocket('ws://' + server_address + '/article-ws')
    articleWS_webSocket.onopen = function (event) {
        console.log("커넥션 열림 articleWS")
        let jsonMessage = JSON.stringify({
            num: articleNum
        })
        articleWS_webSocket.send(jsonMessage)
    }
    articleWS_webSocket.onmessage = function (event) {
        let newJsonData = JSON.parse(event.data)
        const current_time = new Date().getTime()
        if (current_time - last_interaction >= 1000) {
            while (dom.firstChild) { //dom아래의 요소들을 제거 
                dom.removeChild(dom.firstChild);
            }
            articleWS_webSocket.close()
            cardWS_webSocket_arr.forEach(e => {
                // article-ws에서 글 갱신시 close하기 위해서 담음
                e.close()
            })
            return articleDetailView(newJsonData)
        }
    };
    articleWS_webSocket.onclose = function (event) {
        console.log("커넥션 닫힘 articleWS")
    }
}


function cardWS(card, child) {
    let cardWS_webSocket = new WebSocket('ws://' + server_address + '/card-ws')

    // cardWS_webSocket_arr.push(cardWS_webSocket) // article-ws에서 글 갱신시 close하기 위해서 담음 !

    cardWS_webSocket.onopen = function (event) {
        console.log("커넥션 열림 cardWS")
        let jsonMessage = JSON.stringify({
            id: card.id
        })
        if (cardWS_webSocket.readyState === WebSocket.OPEN) {
            cardWS_webSocket.send(jsonMessage)
        }

    }
    cardWS_webSocket.onmessage = function (event) {
        //여기서 변경된 카드 받아서 마지막 이벤트 이후 1초 지났을 때에만 대입하도록 수정
        let newCard = JSON.parse(event.data)
        const current_time = new Date().getTime();
        if (current_time - last_interaction >= 1000) {
            cardBuild(newCard, child, true)
            console.log("card 재생성됨:: ", newCard)
        } else {
            console.log("card 재생성 대기중, last_interaction 이유")
        }
    };
    cardWS_webSocket.onclose = function (event) {
        console.log("커넥션 닫힘 cardWS");
    }

    return cardWS_webSocket
}


function cardBuild(card, dom, refresh) { //refresh는 onmessage 수신시 카드 정보만 변경하기 위함. 새 소켓을 생성하지 않음.
    let child
    switch (card.cardType) {
        case "R01":
            child = r01.cloneNode(true)

            child.querySelector("#llmresponse0").value = card.llmresponse0

            child.style.display = "flex"
            break;
        case "R02":
            child = r02.cloneNode(true)

            child.querySelector("#userInput0").value = card.userInput0
            child.querySelector("#llmresponse0").value = card.llmresponse0
            child.querySelector("#llmresponse1").value = card.llmresponse1
            child.querySelector("#llmresponse2").value = card.llmresponse2

            child.style.display = "flex"
            break;
        case "W01":
            child = w01.cloneNode(true)

            child.querySelector("#userInput0").value = card.userInput0
            child.querySelector("#llmresponse0").value = card.llmresponse0

            child.style.display = "flex"
            break;
        case "W02":
            child = w02.cloneNode(true)

            child.querySelector("#userInput0").value = card.userInput0
            child.querySelector("#llmresponse0").value = card.llmresponse0

            child.style.display = "flex"
            break;
        case "V01":
            child = v01.cloneNode(true)

            child.querySelector("#userInput0").value = card.userInput0
            child.querySelector("#llmresponse0").value = card.llmresponse0

            child.style.display = "flex"
            break;
        case "V02":
            child = v02.cloneNode(true)

            child.querySelector("#userInput0").value = card.userInput0
            child.querySelector("#llmresponse0").value = card.llmresponse0
            child.querySelector("#llmresponse1").value = card.llmresponse1
            child.querySelector("#llmresponse2").value = card.llmresponse2

            child.style.display = "flex"
            break;
        case "P01":
            child = p01.cloneNode(true)

            child.querySelector("#userInput0").value = card.userInput0
            child.querySelector("#llmresponse0").value = card.llmresponse0

            child.style.display = "flex"
            break;
        case "P02":
            child = p02.cloneNode(true)

            child.querySelector("#userInput0").value = card.userInput0

            child.style.display = "flex"
            break;
        case "P03":
            child = p03.cloneNode(true)
            loadFile(child, card.id)
            child.style.display = "flex"
            break;
    }//Switch 문 종료

    if (refresh) {// 리프레시 시에 prev child삭제 먼저
        let prev_dom_ws = weakmap.get(dom)
        while (dom.firstChild) {
            dom.removeChild(dom.firstChild)
        }
        //refresh는 onmessage 수신시 카드 정보만 변경하기 위함. 새 소켓을 생성하지 않음.
        // console.log("prev_dom_ws: ", prev_dom_ws)
        weakmap.set(child, prev_dom_ws)
    } else {
        let cardWS_webSocket = cardWS(card, child)
        weakmap.set(child, cardWS_webSocket)
    }
    if (editPermission){
        //편집 권한이 있는 경우에 card 삭제 표기
        child.querySelector('.cardDelButton').style.display = "block"
        child.querySelector('.cardDelButton').addEventListener('click' , function() {
            delCard(card.id)
        })
    }
    dom.appendChild(child)

    child.querySelector('#regButton').addEventListener('click', function (event) {
        console.log("card regButton clicked!", card)
        if (editPermission) {
            card.llmStatus = 'GENERATING'
            last_interaction = 0; //무조건 바로 갱신되도록
            sendCard(event, child, card)
        } else {
            showModal("편집 권한이 없어요. 생성형 AI에게 요청 실패했어요")
        }

    });

    if (card.cardType == 'P03') { //파일의 경우에만 처리해야하는 것 
        child.querySelector("#userInput0").onchange = function (event) {
            console.log("file change 감지됨")
            if (editPermission) {
                const three_container = child.querySelector('.threejs-container')
                three_container.style.display = "block"
                three_container.classList.add("blur-effect")
                setTimeout(function () {
                    child.querySelector('.cardContent .reg').classList.add("blur-effect")
                    child.querySelector('.cardContent .response').classList.add("blur-effect")
                    three_container.classList.remove("blur-effect")
                }, 500)

                cube_three(child); // Three.js 초기화 및 렌더링

                uploadFile(child.querySelector("#userInput0").files, card.id)
                setTimeout(function () {
                    last_interaction = 0
                    sendCard(event, child, card)
                }, 2000)
            } else {
                showModal("편집 권한이 없어요. 파일 업로드 실패")
            }
        }
    }

    child.querySelector('.cardContent').addEventListener('keyup', function (event) {
        last_interaction = new Date().getTime() //상호작용 시간 갱신
        keycount += 1
        // 입력된 텍스트 길이가 20의 배수일 때만 sendCard 함수 호출
        if (keycount % 10 == 0) {
            sendCard(event, child, card)
        }
        if (event.key == "Enter" || event.key == "." || event.key == "," || event.key == "?" || event.key == "!") {
            sendCard(event, child, card)
        }
    })

    const llmStatusDOM = child.querySelector("#llmStatus") //h2 태그에 상태 표시 기능
    switch (card.llmStatus) {
        case "NEW":
            llmStatusDOM.textContent = "입력을 기다리고 있어요"
            break;
        case "GENERATING":
            llmStatusDOM.textContent = "답변을 생성 중. . ."
            break;
        case "CANCELED":
            llmStatusDOM.textContent = "! 생성 중 오류 발생 !"
            break;
        case "COMPLETED":
            llmStatusDOM.textContent = "생성 완료. 한번 확인해보세요"
            break;
    }


    if (card.llmStatus == "GENERATING") {
        const three_container = child.querySelector('.threejs-container')
        three_container.style.display = "block"
        three_container.classList.add("blur-effect")
        setTimeout(function () {
            child.querySelector('.cardContent .reg').classList.add("blur-effect")
            child.querySelector('.cardContent .response').classList.add("blur-effect")
            three_container.classList.remove("blur-effect")
        }, 500)

        cube_three(child); // Three.js 초기화 및 렌더링
    }
}

function sendCard(event, child, card) {
    //카드에 변경이 있을 경우에 card-ws로 보내는 역할 
    let jsonObj = {
        id: card.id,
        cardType: card.cardType,
        llmStatus: card.llmStatus
    }

    switch (card.cardType) {
        case "R01":
            jsonObj.llmresponse0 = child.querySelector("#llmresponse0").value
            break
        case "R02":
            jsonObj.userInput0 = child.querySelector("#userInput0").value
            jsonObj.llmresponse0 = child.querySelector("#llmresponse0").value
            jsonObj.llmresponse1 = child.querySelector("#llmresponse1").value
            jsonObj.llmresponse2 = child.querySelector("#llmresponse2").value
            break
        case "W01":
            jsonObj.userInput0 = child.querySelector("#userInput0").value
            jsonObj.llmresponse0 = child.querySelector("#llmresponse0").value
            break
        case "W02":
            jsonObj.userInput0 = child.querySelector("#userInput0").value
            jsonObj.llmresponse0 = child.querySelector("#llmresponse0").value
            break
        case "V01":
            jsonObj.userInput0 = child.querySelector("#userInput0").value
            jsonObj.llmresponse0 = child.querySelector("#llmresponse0").value
            break
        case "V02":
            jsonObj.userInput0 = child.querySelector("#userInput0").value
            jsonObj.llmresponse0 = child.querySelector("#llmresponse0").value
            jsonObj.llmresponse1 = child.querySelector("#llmresponse1").value
            jsonObj.llmresponse2 = child.querySelector("#llmresponse2").value
            break
        case "P01":
            jsonObj.userInput0 = child.querySelector("#userInput0").value
            jsonObj.llmresponse0 = child.querySelector("#llmresponse0").value
            break
        case "P02":
            jsonObj.userInput0 = child.querySelector("#userInput0").value
            break
    }
    let jsonMessage = JSON.stringify(jsonObj)
    console.log("sendCard(): ", jsonObj)
    let cardWS_webSocket = weakmap.get(child)
    cardWS_webSocket.send(jsonMessage)
}

function addCard(articleNum, cardType1) {
    last_interaction = 0;
    let articleDTO = {
        num: articleNum,
        cardDTOList: [{ cardType: cardType1 }]
    }
    let jsonMessage = JSON.stringify(articleDTO)
    if (articleWS_webSocket.readyState === WebSocket.OPEN) {
        // console.log(jsonMessage)
        articleWS_webSocket.send(jsonMessage)
    }
}
window.addCard = addCard

function articlePermission(articleNum) {
    last_interaction = 0;
    let articlePermission = document.querySelector("#articlePermission").checked ? 'OPEN' : 'ONLYME'
    if (editPermission) {
        let articleDTO = {
            num: articleNum,
            articlePermission: articlePermission
        }
        let jsonMessage = JSON.stringify(articleDTO)
        if (articleWS_webSocket.readyState === WebSocket.OPEN) {
            articleWS_webSocket.send(jsonMessage)
        }
    } else {
        showModal("편집 권한이 없어요. 따라서 게시판 공개 여부를 수정할 수 없어요")
        document.querySelector("#articlePermission").checked = !document.querySelector("#articlePermission").checked
        //다시 반대로
    }

}
window.articlePermission = articlePermission

function delArticle(articleNum) {
    fetch('/api/article/' + articleNum, {
        method: 'DELETE', // HTTP DELETE 요청 메소드 지정
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/'
            } else {
                showModal("삭제를 실패했어요")
                throw new Error("삭제 실패")
            }
        }
        ).catch((error) =>
            console.log(error)
        )
}
window.delArticle = delArticle

function delCard(cardId) {
    fetch('/api/card/' + cardId, {
        method: 'DELETE', // HTTP DELETE 요청 메소드 지정
    })
        .then(response => {
            if (response.ok) {
                let articleDTO = {
                    num: articleNum,
                    modDate: new Date().toISOString() //modDate를 보낸다는 것은 articleWS에게 브로드캐스팅을 요청하는 행위임.
                }
                let jsonMessage = JSON.stringify(articleDTO)
                console.log("삭제 jsonmessage", jsonMessage)
                last_interaction = 0;
                articleWS_webSocket.send(jsonMessage)
            } else {
                showModal("삭제를 실패했어요")
                throw new Error("삭제 실패")
            }
        }
        ).catch((error) =>
            console.log(error)
        )
}

function qrcodeToggle(){
    if (!qrToggle_bool) {
        document.querySelector(".articleTitle  #qrcode img").style.width = "500px";
    } else{
        document.querySelector(".articleTitle  #qrcode img").style.width = "50px";
    }
    qrToggle_bool = !qrToggle_bool
}
window.qrcodeToggle = qrcodeToggle
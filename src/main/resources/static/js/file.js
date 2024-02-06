export function uploadFile(files, cardId) {
    const file = files[0]
    if (file) {
        const formData = new FormData();
        formData.append('file', file)
        formData.append('cardId', cardId)

        fetch('/api/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                }
                throw new Error("파일 전송 중 오류")
            })
            .then(data => {
                console.log("파일 업로드 성공", data)
                return true
            })
            .catch(error => {
                console.error("업로드 중 오류 발생", error)
                return false
            })
    }
}

export function loadFile(dom, cardId) {
    const formData = new FormData();
    formData.append('cardId', cardId)
    fetch('/api/load', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response
            }
            throw new Error("파일 수신 중 오류")
        })
        .then(res => {
            console.log("파일 수신 성공", res)
            let isImage = res.headers.get("CardFile-isImage")
            if (isImage == "true") {
                const blob = res.blob().then(blob => { //blob 호출 후 프로미스 종료 시 실행
                    const imageUrl = URL.createObjectURL(blob)
                    dom.querySelector('#llmresponse0 img').src = imageUrl
                    dom.querySelector('#llmresponse0 img').style.display = "block"
                }
                )
            } else { //이미지가 아닐 경우
                const blob = res.blob().then(blob => { //blob 호출 후 프로미스 종료 시 실행
                    const fileUrl = URL.createObjectURL(blob)
                    dom.querySelector('#llmresponse0 a').href = fileUrl
                    dom.querySelector('#llmresponse0 a').style.display = "block"
                    dom.querySelector('.response').style.minHeight= "100px"
                }
                )
            }
        })
        .catch(error => {
            console.error("수신 중 오류 발생", error)
            return false
        })
}
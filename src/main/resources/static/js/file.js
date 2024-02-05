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
                return response.blob()
            }
            throw new Error("파일 수신 중 오류")
        })
        .then(blob => {
            console.log("파일 수신 성공", blob)
            const imageUrl = URL.createObjectURL(blob)
            dom.querySelector('#llmresponse0').src = imageUrl
            return blob
        })
        .catch(error => {
            console.error("수신 중 오류 발생", error)
            return false
        })
}
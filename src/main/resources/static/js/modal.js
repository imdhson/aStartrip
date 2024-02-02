// 모달을 열고 닫는 함수
export function showModal(message) {
    document.getElementById("modal-text").textContent = message;
    document.getElementById("modal").style.display = "flex"; 
}

export function closeModal(){
    document.getElementById("modal").style.display = "none";
}
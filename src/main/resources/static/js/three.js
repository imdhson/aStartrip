import * as THREE from "https://unpkg.com/three@v0.160.1/build/three.module.js"

// Three.js 물체를 초기화하고 렌더링하는 함수
export function cube_three(dom) {
    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(75, dom.innerWidth / dom.innerHeight, 0.1, 1000);
    camera.lookAt(0, 0, 0)
    const renderer = new THREE.WebGLRenderer({ alpha: true });
    renderer.setSize(dom.innerWidth, dom.innerHeight);
    dom.querySelector('#threejs-container').appendChild(renderer.domElement);

    // Three.js 객체 추가 (예: 큐브)
    const geometry = new THREE.BoxGeometry();
    const material = new THREE.MeshStandardMaterial({ color: "white" });
    const light = new THREE.DirectionalLight("white")
    // scene.background = new THREE.Color("0xffff")
    light.position.set(5, 4, 3)
    scene.add(light)
    const cube = new THREE.Mesh(geometry, material);
    scene.add(cube);

    camera.position.z = 5;

    // 애니메이션 루프
    function animate() {
        requestAnimationFrame(animate);
        cube.rotation.x += 0.03;
        cube.rotation.y += 0.03;
        renderer.render(scene, camera);
    }
    animate();

    dom.addEventListener('resize', function (event) {
        camera.aspect = dom.innerWidth / dom.innerHeight
        camera.updateProjectionMatrix()
        renderer.setSize(dom.innerWidth, dom.innerHeight)
    })
}

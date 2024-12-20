import * as THREE from "https://unpkg.com/three@v0.160.1/build/three.module.js"

// Three.js 물체를 초기화하고 렌더링하는 함수
export function cube_three(dom) {
    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(70, dom.clientWidth / dom.clientHeight, 0.1, 1000);
    camera.lookAt(0, 0, 0);
    const renderer = new THREE.WebGLRenderer({ alpha: true });
    renderer.setSize(dom.clientWidth, dom.clientHeight);
    dom.querySelector('.threejs-container').appendChild(renderer.domElement);

    const geometry = new THREE.IcosahedronGeometry();
    // 빛 반사 효과를 강화합니다.
    const material = new THREE.MeshPhongMaterial({ color: "white", specular: 0xaaaaaa, shininess: 30 });

    // 광원의 강도를 높입니다.
    const light = new THREE.DirectionalLight("white", 2); // 강도를 1.5로 설정
    light.position.set(2, 2, 2);
    scene.add(light);

    const light2 = new THREE.DirectionalLight("white", 2); // 강도를 1.5로 설정
    light2.position.set(-2, -2, -2);
    scene.add(light2);

    const obj = new THREE.Mesh(geometry, material);
    scene.add(obj);

    camera.position.z = 5;

    // 애니메이션 루프
    function animate() {
        requestAnimationFrame(animate);
        obj.rotation.x += 0.03;
        obj.rotation.y += 0.03;
        renderer.render(scene, camera);
    }
    animate();

    window.addEventListener('resize', function (event) {
        camera.aspect = dom.clientWidth / dom.clientHeight
        camera.updateProjectionMatrix()
        renderer.setSize(dom.clientWidth, dom.clientHeight)
    })
}

import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';

import {
  CSS3DRenderer,
  CSS3DSprite,
  CSS3DObject,
} from 'three/examples/jsm/renderers/CSS3DRenderer';

// 发光边框
import { OutlinePass } from 'three/examples/jsm/postprocessing/OutlinePass';
import { EffectComposer } from 'three/examples/jsm/postprocessing/EffectComposer';
import { RenderPass } from 'three/examples/jsm/postprocessing/RenderPass';
import { ShaderPass } from 'three/examples/jsm/postprocessing/ShaderPass';
import { CopyShader } from 'three/examples/jsm/shaders/CopyShader';
import { FXAAShader } from 'three/examples/jsm/shaders/FXAAShader';

// 导入模型
import { OBJLoader } from 'three/examples/jsm/loaders/OBJLoader';

import { ModelOpts } from './type';

class Three {
    w = 0

    h = 0

    k = 0

    s = 200

    scene = new THREE.Scene()

    // groups = new THREE.Group()
    groups = new THREE.Object3D()

    el:HTMLElement

    camera!: THREE.PerspectiveCamera

    renderer!: THREE.WebGLRenderer

    controls!: OrbitControls

    // 点击选中效果
    mouse = new THREE.Vector3(10, 0, 0)

    raycaster = new THREE.Raycaster()

    perLabel: THREE.Sprite|undefined // 上一个选中的模型

    composer: EffectComposer|undefined

    outlinePass:OutlinePass|undefined

    requestAnimationFrameId!:number

    timeId!: NodeJS.Timeout

    loader = new OBJLoader();
    // 场景2
    // scene2!: THREE.Scene

    render3D!: CSS3DRenderer

    constructor(options?:ModelOpts) {
      let {
        w = window.innerWidth,
        h = window.innerHeight,
        el,
      } = options || {};
      if (el) {
        w = el.offsetWidth;
        h = el.offsetHeight;
      } else {
        el = document.body;
      }

      this.el = el;
      this.w = w;
      this.h = h;
      this.k = w / h;
      // 坐标系
      // const axes = new THREE.AxesHelper(w);
      // this.scene.add(axes);
      this.scene.add(this.groups);
      this.initCamera();
      this.initLight();
      this.initRenderer();
      this.initCss3DRenderer();
      this.initRenderPass();
      this.render();
    }

    initCamera() {
      // console.log(this.k, '-kkkkkkkkkkk');
      this.camera = new THREE.PerspectiveCamera(45, this.k, 10.1, 5000);
      // this.camera = new THREE.OrthographicCamera(-this.k*this.s,this.k*this.s,this.s,-this.s,0.1,1000)

      // this.camera.position.set(0, -200, 200);
      this.camera.position.set(0, 250, 200);
      this.camera.lookAt(0, 0, 0.01);
      // this.camera.lookAt(this.scene.position);
      return this;
    }

    initLight() {
      const point = new THREE.PointLight();
      point.position.set(60, 100, 200);
      this.scene.add(point);

      const ambient = new THREE.AmbientLight(0x444444);
      this.scene.add(ambient);
      return this;
    }

    initRenderer() {
      this.renderer = new THREE.WebGLRenderer({
        antialias: true,
      });

      this.renderer.setPixelRatio(window.devicePixelRatio);
      // 清除背景色，透明背景
      this.renderer.setClearColor(0xffffff, 0);
      this.renderer.setSize(this.w, this.h);
      this.el.appendChild(this.renderer.domElement);
      return this;
    }

    initCss3DRenderer() {
      // this.scene2 = new THREE.Scene();
      // 新开一个3D渲染器
      this.render3D = new CSS3DRenderer();
      // 设置渲染器大小
      this.render3D.setSize(this.w, this.h);
      // 需要设置位置
      this.render3D.domElement.style.position = 'absolute';
      this.render3D.domElement.style.top = '0';
      this.el.appendChild(this.render3D.domElement);
      // 该渲染器也要加上同样的控制器
      // const a = new OrbitControls(this.camera, this.render3D.domElement);
      return this;
    }

    initRenderPass() {
      this.composer = new EffectComposer(this.renderer);
      const renderPass = new RenderPass(this.scene, this.camera);
      const shaderPass = new ShaderPass(FXAAShader);
      shaderPass.uniforms.resolution.value.set(1 / window.innerWidth, 1 / window.innerHeight);
      this.composer?.setSize(window.innerWidth, window.innerHeight);
      this.composer?.addPass(shaderPass);
      this.composer?.addPass(renderPass);
    }

    loadObj(path:string, cb:(obj:THREE.Group)=>void) {
      this.loader.load(path, (obj) => {
        cb(obj);
        this.groups.add(obj);
        this.addControls();
        console.log('加载成功');
        setTimeout(() => {
          this.render();
        });
      }, undefined, () => {
        console.log('加载失败');
        setTimeout(() => {
          this.render();
        });
      });
      return this;
    }

    addOnClick(cb:(checkMesh:THREE.Mesh<THREE.BufferGeometry, THREE.Material | THREE.Material[]>)=>void) {
      this.el.addEventListener('click', (event: { clientX: number; clientY: number; }) => {
        // console.log(event, '------event');
        // 拿到鼠标在屏幕的坐标
        const mouseX = event.clientX;// 鼠标单击位置横坐标
        const mouseY = event.clientY;// 鼠标单击位置纵坐标
        const rect = this.renderer.domElement.getBoundingClientRect();// 拿到div相对屏幕坐标的偏移量
        // 屏幕坐标转标准设备坐标
        const x = ((mouseX - rect.left) / this.renderer.domElement.clientWidth) * 2 - 1;
        const y = -((mouseY - rect.top) / this.renderer.domElement.clientHeight) * 2 + 1;
        const standardVector = new THREE.Vector3(x, y);// 标准设备坐标

        // 标准设备坐标转世界坐标
        const worldVector = standardVector.unproject(this.camera);
        // 射线投射方向单位向量(worldVector坐标减相机位置坐标)
        const ray = worldVector.sub(this.camera.position).normalize();
        // 创建射线投射器对象
        const raycaster = new THREE.Raycaster(this.camera.position, ray);
        raycaster.camera = this.camera;// 设置一下相机

        // 返回射线选中的对象 //第一个参数是检测的目标对象 第二个参数是目标对象的子元素
        const intersects = raycaster.intersectObjects(this.scene.children, true);
        console.log(intersects, '----intersects');
        if (!intersects.length) return;
        // const text = this.getSprite(title, center)
        // setBgColor(this.preCheckIntersects, 'yellowgreen') // 先复原
        // setBgColor(intersects, 'pink')
        // console.log(this.perLabel, '-----this.perLabel');

        const meshs: THREE.Mesh<THREE.BufferGeometry>[] = [];
        intersects.forEach(({ object }) => {
          // console.log(object.isMesh,'-----object.isMesh')
          if ((object as any).isMesh) meshs.push(object as THREE.Mesh<THREE.BufferGeometry>);
        });
        // console.log(meshs, '----meshs');
        // 添加经标注
        // const checkMesh = meshs[0];
        if (!meshs[0]) return;
        cancelAnimationFrame(this.requestAnimationFrameId);
        cb(meshs[0]);

        this.render3D?.render(this.scene, this.camera);
        this.renderOutLine();
        // console.log('点击了------');
      });
      return this;
    }

    addControls() {
      this.controls = new OrbitControls(this.camera, this.render3D.domElement);
      this.controls.addEventListener('change', this.render.bind(this));
      return this;
    }

    addOutLine(meshs: THREE.Mesh<THREE.BufferGeometry>[]) {
      if (this.outlinePass) {
        this.composer?.removePass(this.outlinePass);
        this.outlinePass.dispose();
      }
      this.outlinePass = new OutlinePass(new THREE.Vector2(window.innerWidth, window.innerHeight), this.scene, this.camera, meshs);
      Object.assign(this.outlinePass, {
        renderToScreen: true,
        edgeStrength: 3, // 粗
        edgeGlow: 4, // 发光
        edgeThickness: 2, // 光晕粗（边缘浓度）
        pulsePeriod: 1, // 闪烁
        // edgeGlow: 2, // 发光强度
        // edgeStrength: 3, // // 边缘的强度，值越高边框范围越大

        usePatternTexture: false, // 是否使用贴图
      });
      this.outlinePass.visibleEdgeColor.set('#1EC2E3'); // 设置显示的颜色
      this.outlinePass.hiddenEdgeColor.set('white'); // 设置隐藏的颜色
      this.composer?.addPass(this.outlinePass);
      return this;
    }

    renderOutLine() {
      this.composer?.render(); // 组合渲染
      this.requestAnimationFrameId = requestAnimationFrame(this.renderOutLine.bind(this));
    }

    render() {
      // cancelAnimationFrame(this.requestAnimationFrameId);
      // clearTimeout(this.timeId);
      // 通过摄像机和鼠标位置更新射线
      this.renderer.render(this.scene, this.camera);
      this.render3D?.render(this.scene, this.camera);
      // debugger
      this.composer?.render(); // 组合渲染
      // this.timeId = setTimeout(() => {
      //   if (this.perLabel) {
      //     cancelAnimationFrame(this.requestAnimationFrameId);
      //     this.renderOutLine();
      //     // console.log('重启了');
      //   }
      // }, 300);
    }

    createdCSS3DRomeName(text:string, position: [number, number, number]) {
      const element = document.createElement('div');
      element.textContent = text;
      element.style.fontFamily = 'blod';
      element.style.color = 'black';
      // const element = document.getElementById('label')

      const css3DObject = new CSS3DObject(element);
      css3DObject.position.set(...position);
      css3DObject.scale.set(0.4, 0.4, 0.4);
      css3DObject.rotateX(-1.5);
      // group.add(css3DObject)

      return css3DObject;
    }

    getCenterPoint(checkMesh:THREE.Mesh<THREE.BufferGeometry>|null, amend?:[number, number, number]):[number, number, number] {
      if (!checkMesh) return [0, 0, 0];
      let box = checkMesh.geometry.boundingBox;
      if (!box) {
        box = new THREE.Box3().setFromObject(checkMesh);
      }
      const mx = (box.max.x + box.min.x) / 2; // 模型长度
      const mz = (box.max.z + box.min.z) / 2; // 模型宽度
      const my = (box.max.y + box.min.y) / 2; // 模型高度
      if (!amend) return [mx, my, mz];
      const [amendx, amendy, amendz] = amend;
      return [mx + amendx, my + amendy, mz + amendz];
    }

    clearObj() { // 内存回收
      cancelAnimationFrame(this.requestAnimationFrameId);
      const deleteMaterial = (material:THREE.Mesh['material']) => {
        const materials:THREE.Material[] = Array.isArray(material) ? material : [material];
        materials.forEach((item) => {
          item.dispose();
        });
      };
      const deleteGroup = (group:THREE.Object3D<THREE.Event>) => {
        group.traverse((item) => {
          if (item instanceof THREE.Mesh) {
            item.geometry.dispose();
            // console.log(item, '---item-mesh');
            // item.material.dispose();
            deleteMaterial(item.material);
            deleteCSS3DObject(item.children);
            // console.log(item);
          }
        });
      };
      const deleteCSS3DObject = (obj:THREE.Object3D<THREE.Event>[]) => {
        while (obj.length) {
          const item = obj[obj.length - 1];
          console.log(item, '----obj-3d');
          item.parent?.remove(item);
        }
      };

      const children = this.groups.children;
      while (children.length) {
        const group = children[children.length - 1];
        group.parent?.remove(group);
        if (group.type === 'Group') deleteGroup(group);
      }
      console.log(this.renderer.info, this.renderer, this.groups, '---this.render');
    }

    destoryed() {
      this.clearObj();
      this.scene.clear();
      this.renderer.dispose();
      this.composer = null as any;
      this.camera = null as any;
      // this.renderer.context = null as any;
      const gl = this.renderer.domElement.getContext('webgl');
      if (gl) gl.getExtension('WEBGL_lose_context')?.loseContext();
      console.log(this.renderer.info, this.scene, '--info');
    }
}

export {
  THREE,
  Three,
  CSS3DSprite,
  CSS3DObject,
};

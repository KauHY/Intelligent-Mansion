import {
  shallowRef, ref, onMounted, onUnmounted, watch,
} from 'vue';
import { message } from 'ant-design-vue';
// import {
//   Three, THREE, CSS3DSprite, CSS3DObject,
// } from '~utils/threejs';

import {
  Three, THREE, CSS3DSprite, CSS3DObject,
} from '@gc082/wisdom-park-threejs-sdk';

// import Floor from '~models/floor';
import Building from '~models/building';
import CommandBigScreen, { HooseDatas } from '~models/command-big-screen';
import { useHashMapDics } from '~composition/useHashMapDic';

const rent = require('assets/images/operation-screen/rent1.png');
const noRent = require('assets/images/operation-screen/rent2.png');
const bg = require('assets/images/operation-screen/bg.png');

const publicAreasIcon = { // 公共区域icon
  G1003: require('assets/images/operation-screen/G1003.png'),
  G1004: require('assets/images/operation-screen/G1004.png'),
  G1005: require('assets/images/operation-screen/G1005.png'),
};

export const useThree = () => {
  // const
  const {
    floor,
    building,
    // getRomeData,
    buildingList,
    totalFloor,
    room,
  } = useRoomData();
  const renderEl = shallowRef<HTMLElement>();
  const commandBigScreenInstance = new CommandBigScreen();

  const houseDatas = ref<HooseDatas>({}); // 选中楼层的房屋数据
  const options = useHashMapDics<['HOUSE_STATUS', 'HOUSE_FUNCTION', 'HOUSE_ORIENTATION', 'HOUSE_PROPERTY']>(['HOUSE_STATUS', 'HOUSE_FUNCTION', 'HOUSE_ORIENTATION', 'HOUSE_PROPERTY']);
  let three!:Three;
  // 加载文件
  const loadObj = (name:string) => {
    const path = `${compileConf.publicPath}static/three3d/objs/${name}.obj`;
    three.loadObj(path, (obj:THREE.Group) => {
      const children = obj.children as unknown as THREE.Mesh[];
      children.forEach((m) => {
        const [name] = m.name.split('_');
        const [buildingName, floorName, houseName] = name.split('-');

        switch (name) {
          case 'G1001': { // 基座
            m.material = [
              new THREE.MeshBasicMaterial({
                color: '#9DACC8',
              }),
              new THREE.MeshBasicMaterial({
                color: '#EEF3FF',
              }),
            ];
            break;
          }
          case 'G1002': { // 公共区域
            m.material = [
              new THREE.MeshBasicMaterial({
                color: '#D3DCF2',
              }),
              new THREE.MeshBasicMaterial({
                color: '#EBECF6 ',
              }),
            ];
            break;
          }
          case 'G1003': // 卫生间
          case 'G1004': // 电梯
          case 'G1005': { // 楼梯
            m.material = [
              new THREE.MeshBasicMaterial({
                color: '#BBD0FF',
              }),
              new THREE.MeshBasicMaterial({
                color: '#D5DFFD',
              }),
            ];
            const spriteIcon = addPublicAreasIcon(publicAreasIcon[name], three.getCenterPoint(m, [0, 8, 0]));
            console.log(spriteIcon, '---icon');
            m.add(spriteIcon);
            break;
          }
          default: {
            m.userData = {
              ...houseDatas.value[houseName],
              center: three.getCenterPoint(m),
              // center: three.getCenterPoint(m),
              name: houseName,
              // area: Math.ceil(Math.random() * 300),
            };
            // m.userData.houseStatus = 2;
            m.material = [
              new THREE.MeshBasicMaterial({
                color: m.userData.houseStatus === 2 ? '#8EC1FF' : '#B498B0',
              }),
              new THREE.MeshBasicMaterial({
                color: m.userData.houseStatus === 2 ? '#C1DDFF' : '#CABCC5',
              }),
            ];
            const roomNameObj = three.createdCSS3DRomeName(houseName, three.getCenterPoint(m as THREE.Mesh<THREE.BufferGeometry>, [0, 5, 0]));
            // const roomNameObj = test(m.userData as LabelData, three.getCenterPoint(m, [0, 0, 0]));
            m.add(roomNameObj);
          }
        }
      });
      console.log(obj, '加载成功');
    });
  };

  // 点击事件
  const handlerClick = async (checkMesh:THREE.Mesh<THREE.BufferGeometry>) => {
    const [name] = checkMesh.name.split('_');
    debugger;
    if (['G1001', 'G1002', 'G1003', 'G1004', 'G1005'].includes(name)) return;
    three.perLabel?.geometry.dispose();
    three.perLabel?.parent?.remove(three.perLabel);
    console.log(name, '----texture');
    room.value = name.split('-')[2];
    // three.perLabel = createLabel(checkMesh.userData as LabelData);
    three.perLabel = await createLabel(checkMesh.userData as HooseDatas['']);
    checkMesh.add(three.perLabel);
    three.addOutLine([checkMesh]);
  };

  const loadImage = (url:string) => new Promise<HTMLImageElement>((resolve, reject) => {
    const image = new Image();
    image.src = url;
    image.onload = () => {
      resolve(image);
    };
    setTimeout(() => {
      reject(image);
    }, 1000 * 10);
  });

  let bgImg!:HTMLImageElement;
  let rentImg!:HTMLImageElement;
  let noRentImg!:HTMLImageElement;
  const createLabel = async (userData: HooseDatas['']) => {
    if (!bgImg)bgImg = await loadImage(bg);
    if (!rentImg) rentImg = await loadImage(rent);
    if (!noRentImg) noRentImg = await loadImage(noRent);
    const currentRentImg = userData.houseStatus === 2 ? rentImg : noRentImg;
    console.log(userData, '----userData');
    // bgImg.src = bg;
    // rentImg.src = rent;
    const canvas = document.createElement('canvas');
    canvas.width = 686;
    canvas.height = 400;
    const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;

    console.log('加载成功', bgImg.width, bgImg.height);
    ctx.drawImage(bgImg, 60, 20, bgImg.width, bgImg.height, 0, 0, 686, 400);
    ctx.drawImage(currentRentImg, 0, 0, currentRentImg.width, currentRentImg.height, 480, 160, currentRentImg.width, currentRentImg.height);
    ctx.fillStyle = '#69FFFC';
    ctx.font = '28px PingFangSC-Medium, PingFang SC bold';
    ctx.fillText(userData.companyName || '空置', 190, 80);

    ctx.font = '22px PingFangSC-Medium, PingFang SC bold';
    ctx.fillText(`${userData.buildArea || 0}㎡`, 190, 150);
    ctx.fillText(options.HOUSE_FUNCTION[userData.houseFunction]?.label || '', 320, 150);
    ctx.fillText(options.HOUSE_ORIENTATION[userData.orientation]?.label || '', 190, 230);
    ctx.fillText(options.HOUSE_PROPERTY[userData.houseProperty]?.label || '', 320, 230);
    ctx.fillText(userData.name, 60, 180);

    const texture = new THREE.CanvasTexture(canvas);

    // 使用Sprite显示文字
    const material = new THREE.SpriteMaterial({ map: texture });
    const labelSprite = new THREE.Sprite(material);
    labelSprite.scale.set(171, 100, 0);
    labelSprite.center.set(0.05, 0.16);
    labelSprite.position.set(...userData.center);
    return labelSprite;
  };

  let obj:string;
  const switchObj = async (value:string|SelectOptions[0]) => { // 切换模型
    console.log(value, '---value');
    debugger;
    if (!commandBigScreenInstance.parkOrgId) {
      await commandBigScreenInstance.initPark();
    }

    if (typeof value === 'string') {
      floor.value = value;
    } else {
      if (value.label !== 'B1') {
        message.warning('其他楼栋暂不支持查看');
        return;
      }
      building.value = { ...value };
      totalFloor.value = value.extraInfo?.totalFloor || 0;
    }
    const objName = `${building.value.label}-${floor.value}f`;
    if (obj === objName || !three) return;
    houseDatas.value = await commandBigScreenInstance.listHouseFor3D(building.value.value, floor.value);
    three.clearObj();
    loadObj(objName);
    obj = objName;
  };
  const clearObjCache = () => {
    obj = '';
  };

  const addPublicAreasIcon = (img:any, center:[number, number, number]) => {
    const spriteMap = new THREE.TextureLoader().load(img);
    const spriteMaterial = new THREE.SpriteMaterial({ map: spriteMap });
    const sprite = new THREE.Sprite(spriteMaterial);
    sprite.scale.set(10, 10, 0);
    // sprite.center.set(0, 0);
    sprite.position.set(...center);
    return sprite;
  };

  const initThree = async () => {
    if (!renderEl.value) return;
    three = new Three({
      el: renderEl.value,
    });
    console.log(renderEl.value?.offsetHeight, renderEl.value?.offsetWidth);
    three.addOnClick(handlerClick);
    switchObj(String(1));
  };

  // onMounted(() => {
  //   setTimeout(() => {
  //     initThree();
  //   }, 1000);
  // });

  watch(renderEl, () => {
    initThree();
  });
  onUnmounted(() => {
    three?.destoryed();
  });
  return {
    renderEl,
    floor,
    building,
    room,
    switchObj,
    clearObjCache,
    buildingList,
    totalFloor,
  };
};

export const useRoomData = () => {
  // const floorInstance = new Floor();
  const buildingInstance = new Building();
  const buildingList = ref<SelectOptions>([]);
  const totalFloor = ref(0);
  const floor = ref('1');
  const building = ref<SelectOptions[0]>({
    label: '',
    value: '',
  });
  const room = ref('101');
  // const getRomeData = (roomName:string) => {
  //   try {
  //     return roomDatas[building.value?.label][floor.value][roomName];
  //   } catch (error) {
  //     return {};
  //   }
  // };
  const parkOrgId = sessionStorage.commandBigScreen_ParkOrgId;
  const init = async () => {
    buildingList.value = await buildingInstance.listBuildingDict(parkOrgId, true);
  };
  init();
  return {
    floor,
    building,
    // getRomeData,
    room,
    buildingList,
    totalFloor,
  };
};

export default function printHtml(html) {
  const style = getStyle();
  const container = getContainer(html);

  document.body.appendChild(style);
  document.body.appendChild(container);

  getLoadPromise(container).then(() => {
    window.print();
    document.body.removeChild(style);
    document.body.removeChild(container);
  });
}

// 设置打印样式
function getStyle() {
  const styleContent = `#print-container {
    display: none;
}
@media print {
    body > :not(.print-container) {
        display: none;
    }
    html,
    body {
        display: block !important;
    }
    #print-container {
        display: block;
    }
}`;
  const style = document.createElement('style');
  style.innerHTML = styleContent;
  console.log('styleContent', styleContent);
  return style;
}

// 清空打印内容
function cleanPrint() {
  const div = document.getElementById('print-container');
  if (div) {
    document.querySelector('body').removeChild(div);
  }
}

// 新建DOM，将需要打印的内容填充到DOM
function getContainer(html) {
  cleanPrint();
  const container = document.createElement('div');
  container.setAttribute('id', 'print-container');
  container.innerHTML = html;
  // container.style.zoom = 0.5 // 缩放打印内容
  return container;
}

// 图片完全加载后再调用打印方法
function getLoadPromise(dom) {
  let imgs = dom.querySelectorAll('img');
  imgs = [].slice.call(imgs);

  if (imgs.length === 0) {
    return Promise.resolve();
  }

  let finishedCount = 0;
  return new Promise((resolve) => {
    function check() {
      finishedCount += 1;
      if (finishedCount === imgs.length) {
        resolve();
      }
    }
    imgs.forEach((img) => {
      img.addEventListener('load', check);
      img.addEventListener('error', check);
    });
  });
}

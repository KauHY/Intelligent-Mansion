import { ConfigProvider } from 'ant-design-vue';
import { ref } from 'vue';

const DEFAULT_THEME_COLOR = '#1D7CF2';
const THEME_STORAGE_KEY = 'themeColor';

type RGB = {
  r: number,
  g: number,
  b: number,
};

const normalizeHex = (value = DEFAULT_THEME_COLOR) => {
  const normalizedValue = value.trim().replace('#', '');
  if (/^[\da-fA-F]{3}$/.test(normalizedValue)) {
    return `#${normalizedValue.split('').map((char) => `${char}${char}`).join('').toUpperCase()}`;
  }
  if (/^[\da-fA-F]{6}$/.test(normalizedValue)) {
    return `#${normalizedValue.toUpperCase()}`;
  }
  return DEFAULT_THEME_COLOR;
};

const hexToRgb = (hex:string):RGB => {
  const normalizedHex = normalizeHex(hex).slice(1);
  return {
    r: Number.parseInt(normalizedHex.slice(0, 2), 16),
    g: Number.parseInt(normalizedHex.slice(2, 4), 16),
    b: Number.parseInt(normalizedHex.slice(4, 6), 16),
  };
};

const mixChannel = (start:number, end:number, weight:number) => Math.round(start + ((end - start) * weight));

const mixRgb = (source:RGB, target:RGB, weight:number):RGB => ({
  r: mixChannel(source.r, target.r, weight),
  g: mixChannel(source.g, target.g, weight),
  b: mixChannel(source.b, target.b, weight),
});

const rgbToHex = ({ r, g, b }:RGB) => `#${[r, g, b].map((item) => item.toString(16).padStart(2, '0')).join('').toUpperCase()}`;

const rgba = ({ r, g, b }:RGB, alpha:number) => `rgba(${r}, ${g}, ${b}, ${alpha})`;

const applyThemeVariables = (themeColor:string) => {
  const root = document.documentElement;
  const primary = hexToRgb(themeColor);
  const primaryHover = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.16);
  const primaryActive = mixRgb(primary, { r: 5, g: 18, b: 34 }, 0.22);
  const primaryDeep = mixRgb(primary, { r: 8, g: 24, b: 40 }, 0.52);
  const primarySurface = mixRgb(primary, { r: 245, g: 250, b: 255 }, 0.9);
  const tableHeaderStart = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.88);
  const tableHeaderEnd = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.96);
  const tableHeaderHoverStart = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.84);
  const tableHeaderHoverEnd = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.93);
  const tableHoverSolidStart = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.9);
  const tableHoverSolidEnd = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.96);
  const tableSelectedSolidStart = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.84);
  const tableSelectedSolidEnd = mixRgb(primary, { r: 255, g: 255, b: 255 }, 0.92);

  root.dataset.theme = 'wisdom-building';

  const themeVars:Record<string, string> = {
    '--theme-primary': themeColor,
    '--theme-primary-rgb': `${primary.r}, ${primary.g}, ${primary.b}`,
    '--theme-primary-hover': rgbToHex(primaryHover),
    '--theme-primary-active': rgbToHex(primaryActive),
    '--theme-primary-deep': rgbToHex(primaryDeep),
    '--theme-primary-surface': rgbToHex(primarySurface),
    '--theme-primary-soft': rgba(primary, 0.08),
    '--theme-primary-border': rgba(primary, 0.2),
    '--theme-primary-glow': rgba(primary, 0.26),
    '--theme-primary-strong-glow': rgba(primary, 0.38),
    '--theme-table-header-bg-start': rgbToHex(tableHeaderStart),
    '--theme-table-header-bg-end': rgbToHex(tableHeaderEnd),
    '--theme-table-header-hover-start': rgbToHex(tableHeaderHoverStart),
    '--theme-table-header-hover-end': rgbToHex(tableHeaderHoverEnd),
    '--theme-table-row-hover-solid-start': rgbToHex(tableHoverSolidStart),
    '--theme-table-row-hover-solid-end': rgbToHex(tableHoverSolidEnd),
    '--theme-table-row-selected-solid-start': rgbToHex(tableSelectedSolidStart),
    '--theme-table-row-selected-solid-end': rgbToHex(tableSelectedSolidEnd),
  };

  Object.entries(themeVars).forEach(([key, value]) => {
    root.style.setProperty(key, value);
  });
};

export const useSwitchTheme = () => {
  const currentThemeColor = ref(normalizeHex(
    window.$localStorage.getItem(THEME_STORAGE_KEY) || DEFAULT_THEME_COLOR,
  ));

  const switchTheme = (key:string, isInit?:true) => {
    const nextThemeColor = normalizeHex(key);
    if (!isInit) window.$localStorage.setItem(THEME_STORAGE_KEY, nextThemeColor);
    currentThemeColor.value = nextThemeColor;
    ConfigProvider.config({
      theme: {
        primaryColor: nextThemeColor,
      },
    });
    applyThemeVariables(nextThemeColor);
  };

  return {
    currentThemeColor,
    switchTheme,
  };
};

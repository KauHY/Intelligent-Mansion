/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-18 09:33:31
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-13 10:23:45
 */
const path = require('path');

module.exports = {
  root: true,
  env: {
    node: true,
  },
  parser: 'vue-eslint-parser',
  // parser: '@typescript-eslint/parser',
  extends: [
    'plugin:vue/vue3-recommended',
    'airbnb-base',
  ],
  parserOptions: {
    // parser: 'babel-eslint',
    parser: '@typescript-eslint/parser',
    ecmaFeatures: {
      jsx: true, // 启用jsx
    },
  },
  plugins: [
    'vue',
  ],
  globals: {
    BaseSearchParams: false,
    SearchResult: false,
    MenuItem: false,
    NodeJS: false,
    AMap: false,
    AMapUI: false,
    defineProps: false,
    defineEmits: false,
    SelectOptions: false,
    SelectOptionsMap: false,
    compileConf: false,
    BlobPart: false,
    DownloadReturnData: false,
    FormFieldConf: false, // 表单字段配置
    $localStorage: false,
  },
  settings: {
    'import/resolver': {
      webpack: {
        config: path.resolve(__dirname, 'build/webpack.base.conf.js'),
      },
      typescript: {
        project: path.resolve(__dirname, './tsconfig.json'),
        alwaysTryTypes: true,
      },
    },
    'import/extensions': ['.js', '.jsx', '.ts', '.tsx', '.json'],
    'import/parsers': {
      '@typescript-eslint/parser': ['.ts', '.tsx'],
    },
  },
  rules: {
    'no-console': 'off',
    'no-debugger': 'off',
    'no-unused-vars': 'off',
    'no-param-reassign': 'off',
    'no-tabs': 'off',
    'global-require': 0,
    'import/extensions': ['error', 'always', {
      js: 'never',
      // vue: 'never',
      ts: 'never',
      tsx: 'never',
      jsx: 'never',
    }],
    'linebreak-style': ['off', 'windows'],
    'max-len': ['error', { code: 300 }], // 此处为具体添加代码
    'no-shadow': 'off',
    'class-methods-use-this': 'off',
    'import/prefer-default-export': 'off',
    'prefer-promise-reject-errors': 'off',
    'prefer-destructuring': 'off',
    'vue/no-v-html': 'off',
    camelcase: 'off',
    'no-use-before-define': 'off',
    'vue/no-mutating-props': 'off',
  },
};

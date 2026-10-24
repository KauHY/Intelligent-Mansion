FROM node:20-alpine AS builder
WORKDIR /app
# 固定 pnpm 10：lockfile 9.0 与宿主机一致；pnpm@latest(12+) 的依赖解析会丢 transitive deps（cross-spawn），导致 cross-env 起不来
RUN corepack enable && corepack prepare pnpm@10.14.0 --activate
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile --ignore-scripts
COPY . .
# ENV_MODE=prod：TerserPlugin drop_console 仅在 prod 模式生效（生产包不带 console.log）
RUN npx cross-env ENV_MODE=prod NODE_OPTIONS=--max-old-space-size=4096 webpack --config ./build/webpack.prod.conf

FROM nginx:alpine
# prod 模式产物目录是 config/index.js 里的 projectName=wisdom-park（dev 模式才是 dist）
COPY --from=builder /app/wisdom-park /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

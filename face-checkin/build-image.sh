#! /bin/bash
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

### color
cyan='\033[0;36m'
light_cyan='\033[1;36m'
color_end='\033[0m'

version=$1

make_images() {
    echo -e "${cyan}Start build${color_end} ${light_cyan}[park-web]${color_end} ${cyan}image...${color_end}"
    cat >Dockerfile <<EOF
FROM harbor.hakjkg.com/library/nginx:1.21
LABEL maintainer="Ma Haoqiang"

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

COPY default.conf /etc/nginx/conf.d/default.conf
COPY wisdom-park /usr/share/nginx/html

WORKDIR /usr/share/nginx/html
EOF
    docker build ./ -t harbor.hakjkg.com/park/park-web:${version}-prod
    docker push harbor.hakjkg.com/park/park-web:${version}-prod
    docker rmi harbor.hakjkg.com/park/park-web:${version}-prod
    rm -f Dockerfile
}

make_images

echo -e "---------------------------*********---------------------------"

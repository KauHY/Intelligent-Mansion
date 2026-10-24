#! /bin/bash
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

### color
cyan='\033[0;36m'
light_cyan='\033[1;36m'
color_end='\033[0m'

version=$1

make_images() {
    # 定义服务列表
    smart_park_services=('common' 'customer' 'device' 'fee' 'gateway' 'system' 'business' 'visitor' 'inspection' 'contract' 'meeting' 'attendance' 'industrial')

    for service in "${smart_park_services[@]}"; do
        echo -e "${cyan}Start build${color_end} ${light_cyan}[${service}]${color_end} ${cyan}image...${color_end}"
        cd park-"${service}" || exit
        cat >Dockerfile <<EOF
FROM harbor.hakjkg.com/library/openjdk:8-jre
LABEL maintainer="Ma Haoqiang"

ENV PARAMS="--server.port=8888 --spring.profiles.active=prod --spring.cloud.nacos.discovery.password=huaianpark2022# --spring.cloud.nacos.config.password=huaianpark2022#"

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

COPY target/*.jar /server/app.jar

WORKDIR /server

EXPOSE 8888

CMD ["/bin/sh","-c","java -Xmx1g -Xms1g -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom -jar app.jar \${PARAMS}"]
EOF
        docker build ./ -t harbor.hakjkg.com/park/park-"${service}":${version}-prod
        docker push harbor.hakjkg.com/park/park-"${service}":${version}-prod
        docker rmi harbor.hakjkg.com/park/park-"${service}":${version}-prod
        rm -f Dockerfile
        cd .. || exit
    done
}

make_images

echo -e "---------------------------*********---------------------------"


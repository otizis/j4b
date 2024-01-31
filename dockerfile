FROM adoptopenjdk/openjdk11:ubi
VOLUME /tmp
ADD blog-0.0.1-SNAPSHOT.jar app.jar
ADD ./application.properties /application.properties
RUN sh -c 'touch /app.jar'
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE 8091
ENV JAVA_OPTS=""
ENV APP_OPTS="--server.port=8091"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF-8 -jar /app.jar $APP_OPTS " ]

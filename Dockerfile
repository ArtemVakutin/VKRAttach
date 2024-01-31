# Нам требуется образ, содержащий maven, при помощи
# ключевого слова as мы указываем псевдоним для контейнера сборки,
# чтобы при его помощи в дальнейшем обращаться к контейнеру
FROM maven:3.9-eclipse-temurin-21-jammy as build

# Собирать проект будем в /build
WORKDIR /build

# Теперь необходимо скопировать необходимые для сборки проекта файлы в конейнер
COPY src src
COPY pom.xml pom.xml

ARG M2_LOCAL_PATH=../../../Users/AV/.m2
# И запустить сборку проекта. Загружаемые библиотеки желательно кэшировать между
# сборками,для этого нужно добавить --mount=type=cache,target=/root/.m2 к RUN
RUN --mount=type=cache,target=/root/.m2 mvn clean package

# При помощи ключевого слова FROM необходимо указать исходный образ,
# который мы будем использовать для создания своего.
# Для данного примера выбран образ на основе Debian с установленным
# Liberica OpenJDK 17 версии, поскольку нам он нужен для запуска приложения.
FROM bellsoft/liberica-openjdk-debian:17

# Желательно запускать приложения не от имени суперпользователя, который
# используется по умолчанию, поэтому нужно создать пользователя и группу
# для запуска приложения.
#RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group spring-boot
#USER spring-boot:spring-boot-group

# Иногда требуется получить доступ к файлам, генерирующимся в процессе выполнения,
# для этого зарегистрируем том /tmp
#VOLUME /tmp

# Со временем у проекта будет изменяться версия, и чтобы не изменять всякий раз
# этот Dockerfile имя jar-файла вынесем в аргумент. Альтернативно можно указать
# постоянное имя jar-файла в Maven при помощи finalName.
ARG JAR_FILE=vkrattach-1.0.jar

# Создадим рабочую директорию проекта
WORKDIR /application

# Скопируем в рабочую директорию проекта JAR-файл проекта и его зависимости
COPY --from=build /build/target/${JAR_FILE} application.jar
# Создадим два Volume в /application с логами и файлами базы данных (при необходимости)
# Директория для базы данных
RUN mkdir /application/data
VOLUME /application/data

# Директория для логов
RUN mkdir /application/logs
VOLUME /application/logs

ENV SPRING_SQL_INIT_MODE=never
ENV LOGGING_FILE_PATH=/application/logs
ENV LOGGING_FILE_NAME=logs
ENV SPRING_DATASOURCE_URL=jdbc:h2:file:/application/data/data
ENV SPRING_SQL_INIT_MODE=never
ENV SPRING_PROFILES_ACTIVE=dev-h2
# В конце укажем точку входа. Выбран вариант с использованием exec для того, чтобы
# можно было передать в строку запуска дополнительные параметры запуска - JAVA_OPTS, а так же
# ${0} и ${@} для передачи аргументов запуска.
ENTRYPOINT exec java ${JAVA_OPTS} -jar application.jar ${0} ${@}

#Команды для сохранения image и развертывания на сервере простейшего
#docker build -t vkrattach:0.1 . делаем image
#docker run -p 8080:8080 vkrattach:1.0 врубаем ручками если надо. Тег -v для volume-ов
#docker save -o image.tar your_image_name:tag -сохраняем
#docker load -i image.tar -загружаем
#docker tag vkrattach:0.1 artv86/vkrattach:0.1 -или захреначиваем на удаленный реп, перед этим docker login
#docker push artv86/vkrattach:0.1

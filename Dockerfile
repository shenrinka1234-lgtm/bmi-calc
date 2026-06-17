# 1. 建置階段
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# 複製當前目錄（根目錄）的所有檔案進去容器
COPY . .
# 直接在根目錄執行編譯，因為 pom.xml 就在這裡！
RUN mvn clean package -DskipTests

# 2. 執行階段
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# 從 target 複製打包好的 jar 檔
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
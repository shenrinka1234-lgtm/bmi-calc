# 1. 建置階段
FROM maven:3.8.5-openjdk-17 AS build

# 強制切換到容器內的 /app
WORKDIR /app

# 把外面所有的檔案（包含 bmi-calc-1 資料夾）複製進來
COPY . .

# 【最關鍵的一行】：切換進去 pom.xml 真正存在的子資料夾！
# (備註：請確認你的資料夾名稱到底是小寫 bmi-calc-1 還是大寫 BMI-CALC，必須完全一致)
WORKDIR /app/bmi-calc-1

# 在正確的有 pom.xml 的目錄下執行打包
RUN mvn clean package -DskipTests

# 2. 執行階段
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 從剛剛建置好的子資料夾 target 裡面把 jar 複製過來
COPY --from=build /app/bmi-calc-1/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
# BMI 計算器專案
這是一個使用 Spring Boot 開發的 BMI 計算程式。
- 功能：計算 BMI 值
- 部署平台：Render
### 專案目錄架構

```mermaid
graph TD
    A[bmi-calc/ - 專案根目錄] --> B[src/ - 原始碼]
    A --> C[pom.xml - Maven 設定]
    A --> D[Dockerfile - 容器設定]
    B --> E[main/java/com/example/demo/]
    E --> F[DemoApplication.java - 啟動檔]
    E --> G[HelloController.java - 網頁控制器]

# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 复制后端代码
COPY backend/ .

# 打包
RUN mvn clean package -DskipTests

# 第二阶段：运行
FROM eclipse-temurin:17
WORKDIR /app

# 复制 jar
COPY --from=build /app/target/*.jar app.jar

# 启动
CMD ["java", "-jar", "app.jar"]
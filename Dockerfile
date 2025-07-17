# Use uma imagem base Java. Escolha uma versao que corresponda a sua maquina local.
FROM openjdk:17-jdk-slim

# Define o diretorio de trabalho dentro do container
WORKDIR /app

# Copia o driver JDBC para o diretorio de libs
COPY lib/DriverPostgreSQL.jar /app/lib/DriverPostgreSQL.jar

# Copia os arquivos compilados da sua aplicacao
# Assumimos que 'bin' contem todos os seus .class e que a estrutura de pacotes eh mantida
COPY bin /app/bin

# Comando para executar a aplicacao
# O Class-Path deve incluir o diretorio 'bin' e o driver JDBC
# Note: 'bin' eh o diretorio, '.' significa o diretorio atual
# Para executar, o Main.java deve estar no pacote 'main', ou seja, main.Main
CMD ["java", "-cp", "bin:lib/DriverPostgreSQL.jar", "main.Main"]
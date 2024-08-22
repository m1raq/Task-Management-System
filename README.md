Менеджер задач
Локальный запуск проекта
Требования:
Docker
Docker Compose
Java 17
Подъем dev-среды:
Клонировать репозиторий с проектом.
Зайти в терминал и перейти в директорию проекта (для Windows команда cd [полный путь до директории]).
Собрать проект командой gradlew build.
Выполнить команду docker-compose up --build в директории проекта.
Подождать, пока контейнеры не будут запущены.
Проверка:
Открыть в браузере http://localhost:8081/swagger-ui/index.html?continue=#/ для проверки приложения. (Должен открыться Swagger)
Остановка dev-среды:
Выполнить команду docker-compose down в директории проекта.

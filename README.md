<h1 class="mb-3 mt-3 text-3xl font-normal first:mt-3">Менеджер задач</h1>
<h2 class="mb-2 mt-6 text-lg first:mt-3">Локальный запуск проекта</h2>
<h3 class="mb-2 mt-6 text-lg first:mt-3">Требования:</h3>
<ul class="list-disc pl-8">
<li index="0"><h4>Docker</h4></li>
<li index="1"><h4>Docker Compose</h4></li>
<li index="2"><h4>Java 17</h4></li>
</ul>
<h3 class="mb-2 mt-6 text-lg first:mt-3">Подъем dev-среды:</h3>
<ol class="list-decimal marker:font-mono marker:text-sm pl-11">
<li index="0"><h4>Клонировать репозиторий с проектом.</h4></li>
<li index="1"><h4>Зайти в cmd и перейти в директорию проекта (для Windows команда </h4><span><code>cd [полный путь до директории]</code></span><span>).</span></li>
<li index="2"><h4>Собрать проект командой </h4><span><code>gradlew build</code></span><span>.</span></li>
<li index="3"><h4>Выполнить команду </h4><span><code>docker-compose up --build</code></span><span> в директории проекта.</span></li>
<li index="4"><h4>Подождать, пока контейнеры не будут запущены.</h4></li>
</ol>
<h3 class="mb-2 mt-6 text-lg first:mt-3">Проверка:</h3>
<ol class="list-decimal marker:font-mono marker:text-sm pl-11">
<li index="0"><h4>Открыть в браузере </h4><span><code>http://localhost:8081/swagger-ui/index.html?continue=#/</code></span><span> для проверки приложения.</span><span><br></span><span>
</span><span>(Должен открыться Swagger)</span></li>
</ol>
<h3 class="mb-2 mt-6 text-lg first:mt-3">Остановка dev-среды:</h3>
<ol class="list-decimal marker:font-mono marker:text-sm pl-11">
<li index="0"><h4>Выполнить команду </h4><code>docker-compose down</code></span><span> в директории проекта.</span></li>
</ol>

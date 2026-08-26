# TankSimulator

## Запуск проекта

- Нужен доступ в интернет при первой сборке — Gradle сам скачает JDK 26 через toolchain resolver (`org.gradle.toolchains.foojay-resolver-convention`), даже если в IntelliJ выставлен другой Project SDK. На это можно не обращать внимания — реальная сборка/запуск идут через Gradle, а не через IDE SDK.
- Запуск игры — через Gradle-таск `:desktop:run` (есть готовый run config `Run Game`, лежит в `.idea/runConfigurations` и подтягивается автоматически при открытии проекта).
- `.idea/` (кроме `runConfigurations`) в репозиторий не кладём — это личные настройки IDE, а не часть проекта.

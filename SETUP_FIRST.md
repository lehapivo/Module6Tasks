# ⚠️ Первый шаг — настройка Gradle Wrapper

Android Studio и IntelliJ IDEA требуют файл `gradle-wrapper.jar` в папке `gradle/wrapper/`.
Этот бинарный файл нельзя включить в архив, но есть два способа его получить:

## Способ 1 — Автоматически (рекомендуется)

Установите Gradle:
```bash
# macOS
brew install gradle

# Windows (через Chocolatey)
choco install gradle

# Linux
sdk install gradle 8.7
```

Затем запустите из папки `Module6Tasks/`:
```bash
./setup.sh
```

## Способ 2 — Через Android Studio (самый простой)

1. Откройте Android Studio
2. `File → Open` → выберите папку проекта (например, `task1-photocatalog`)
3. Android Studio автоматически предложит настроить Gradle
4. Нажмите **OK** — Studio скачает всё сама

## Способ 3 — Вручную для каждого проекта

```bash
cd task1-photocatalog
gradle wrapper --gradle-version 8.7
```

Повторите для каждой папки задания.

---

После этого все проекты откроются в Android Studio без проблем.

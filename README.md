# Модуль 6 — Практические задания 1–7

Все задания написаны на **Kotlin** с использованием **Jetpack Compose** (Android) и **Ktor** (сервер).

---

## Структура репозитория

```
Module6Tasks/
├── task1-photocatalog/       # Задание 1 — Фотокаталог (Retrofit + Clean Architecture)
├── task2-nobelprizes/        # Задание 2 — Нобелевские лауреаты (Ktor Client + Clean Architecture)
├── task3-auth/               # Задание 3 — Авторизация (Ktor Client + DataStore)
├── task4-nobelserver/        # Задание 4 — Ktor-сервер Nobel Prize API (in-memory)
├── task5-nobelserver-db/     # Задание 5 — Ktor-сервер + PostgreSQL (neon.tech)
├── task6-nobelclient-own/    # Задание 6 — Android-клиент для собственного сервера (Task 5)
└── task7-ble/                # Задание 7 — BLE Монитор сердечного ритма
```

---

## Как импортировать в Android Studio

### Android-проекты (задания 1, 2, 3, 6, 7)

1. Откройте **Android Studio**
2. `File → Open`
3. Выберите папку нужного задания, например `task1-photocatalog/`
4. Нажмите **OK** и дождитесь Gradle sync
5. Подключите устройство или запустите эмулятор
6. Нажмите **Run** (Shift+F10)

> Каждое задание — это **отдельный Android-проект**. Открывайте их по отдельности.

### Ktor-серверы (задания 4, 5)

1. Откройте **IntelliJ IDEA** (или Android Studio)
2. `File → Open`
3. Выберите папку `task4-nobelserver/` или `task5-nobelserver-db/`
4. Дождитесь Gradle sync
5. Запустите `main()` в `Application.kt`
6. Сервер будет доступен на `http://localhost:8080`

---

## Описание заданий

### Задание 1 — Фотокаталог (`task1-photocatalog`)
- **API:** https://picsum.photos/v2/list
- **Стек:** Retrofit + Clean Architecture + Jetpack Compose
- **Функции:** список фото в 2-колоночной сетке, экран детализации, скачивание через MediaStore

### Задание 2 — Нобелевские лауреаты (`task2-nobelprizes`)
- **API:** https://api.nobelprize.org/2.1/nobelPrizes
- **Стек:** Ktor Client + Clean Architecture + Jetpack Compose
- **Функции:** список лауреатов, фильтр по году и категории, экран детализации

### Задание 3 — Авторизация (`task3-auth`)
- **API:** https://dummyjson.com
- **Стек:** Ktor Client + DataStore + Clean Architecture
- **Тестовые данные:** `emilys` / `emilyspass`
- **Функции:** вход, список пользователей, детали пользователя, выход (токен в DataStore)

### Задание 4 — Nobel Prize Server (`task4-nobelserver`)
- **Стек:** Ktor Server + JWT (данные в памяти)
- **Эндпоинты:**
  - `POST /auth/login` — логин (admin/admin123)
  - `GET /prizes` — список премий (защищённый)
  - `GET /prizes/{year}/{category}` — детали (защищённый)
  - `GET /prizes/{year}/{category}/laureates` — лауреаты (защищённый)

### Задание 5 — Nobel Prize Server + DB (`task5-nobelserver-db`)
- **Стек:** Ktor Server + Exposed + HikariCP + PostgreSQL (neon.tech)
- **⚠️ Перед запуском** замените в `Application.kt` строку подключения:
  ```kotlin
  val jdbcUrl = "jdbc:postgresql://ep-YOUR-PROJECT.us-east-2.aws.neon.tech/neondb?sslmode=require"
  val dbUser = "YOUR_USER"
  val dbPassword = "YOUR_PASSWORD"
  ```
  Или используйте переменные окружения: `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD`
- **Эндпоинты:**
  - `POST /login` — авторизация
  - `GET /prizes` — список из БД
  - `GET /users/me` 🔒 — профиль
  - `GET /users/me/prizes` 🔒 — избранные премии
  - `POST /users/me/prizes/{id}` 🔒 — добавить в избранное
  - `DELETE /users/me/prizes/{id}` 🔒 — удалить из избранного

### Задание 6 — Nobel Client (собственный сервер) (`task6-nobelclient-own`)
- Работает с сервером из Задания 5
- **⚠️ Перед запуском** замените в `NobelRepositoryImpl.kt`:
  ```kotlin
  const val BASE_URL = "http://10.0.2.2:8080"  // Эмулятор
  // или
  const val BASE_URL = "http://192.168.X.X:8080"  // Реальное устройство
  ```

### Задание 7 — BLE Монитор сердечного ритма (`task7-ble`)
- **Стек:** Android BLE API + Jetpack Compose + Accompanist Permissions
- **UUID:** Heart Rate Service `0000180d-...`, Measurement `00002a37-...`
- **Тестирование:** используйте LightBlue (Android) или nRF Connect для симуляции HR-устройства
- **Функции:** сканирование BLE, подключение, получение пульса в реальном времени через Notify

---

## Как загрузить на GitHub

```bash
# 1. Перейдите в папку проекта
cd Module6Tasks

# 2. Инициализируйте git
git init

# 3. Добавьте .gitignore
curl -o .gitignore https://raw.githubusercontent.com/github/gitignore/main/Android.gitignore

# 4. Добавьте все файлы
git add .

# 5. Создайте первый коммит
git commit -m "feat: Add Module 6 tasks 1-7"

# 6. Создайте репозиторий на GitHub, затем:
git remote add origin https://github.com/YOUR_USERNAME/module6-tasks.git
git branch -M main
git push -u origin main
```

---

## Архитектура (Clean Architecture)

```
Presentation (ViewModel + Screen)
       ↓
  Domain (UseCase)
       ↓
Domain Repository Interface
       ↓
Data (RepositoryImpl + DTO + Remote/DB)
```

Все Android-проекты следуют этой структуре пакетов:
```
com.example.appname/
├── data/
│   ├── remote/
│   │   └── dto/          # DTO + маппинг в Domain-модели
│   └── repository/       # Реализация репозиториев
├── domain/
│   ├── model/            # Чистые бизнес-объекты
│   ├── repository/       # Интерфейсы репозиториев
│   └── usecase/          # Use Cases (бизнес-логика)
└── presentation/
    ├── screen/           # Composable-экраны
    └── viewmodel/        # ViewModel + UI State
```

# Toodledo Organiser 📋

Android-органайзер с синхронизацией Toodledo, тёмной темой и виджетом горячего списка.

![Минимальная версия Android](https://img.shields.io/badge/minSdk-24-brightgreen)
![Целевая версия](https://img.shields.io/badge/targetSdk-34-blue)
![Язык](https://img.shields.io/badge/Kotlin-1.9.22-purple)
![CI](https://github.com/kozvits/ToodledoOrganiser/actions/workflows/build.yml/badge.svg)

---

## Возможности

| Функция | Статус |
|---|---|
| Список задач с фильтрацией по папке и контексту | ✅ |
| Создание и редактирование задач (все поля Toodledo) | ✅ |
| Горячий список с сортировкой по 3 параметрам | ✅ |
| Виджет рабочего стола (горячий список) | ✅ |
| Фоновая синхронизация (WorkManager) | ✅ |
| Ручная синхронизация | ✅ |
| Тёмная тема (чёрный фон, фиолетово-бирюзовые акценты) | ✅ |
| Свайп-жесты (завершить / удалить) | ✅ |
| Поиск по задачам | ✅ |
| Room (офлайн-база данных) | ✅ |
| Моковые данные для разработки | ✅ |
| Реальный Toodledo API | 🔜 v2 |

---

## Стек технологий

- **Язык:** Kotlin 1.9.22
- **UI:** XML Views + ViewBinding + DataBinding
- **Архитектура:** Clean Architecture + MVVM
- **DI:** Hilt 2.50
- **Навигация:** Jetpack Navigation Component
- **БД:** Room 2.6.1
- **Фон:** WorkManager 2.9.0
- **Асинхронность:** Coroutines + Flow
- **Сеть:** Retrofit 2 + OkHttp (подготовлено под v2)

---

## Структура проекта

```
app/src/main/java/com/kozvits/toodledo/
├── ToodledoApp.kt                  # Application class, WorkManager init
├── data/
│   ├── local/                      # Room DB, DAOs, Entities, Mappers
│   ├── mock/                       # MockDataSource (фейковые данные)
│   └── repository/                 # Реализации репозиториев + DatabaseSeeder
├── di/                             # Hilt модули (Database, Repository)
├── domain/
│   ├── model/                      # Task, Folder, TaskContext, SyncSettings...
│   ├── repository/                 # Интерфейсы репозиториев
│   └── usecase/                    # Use cases (GetTasks, AddTask, SyncNow...)
├── presentation/
│   ├── adapter/                    # TaskAdapter (RecyclerView)
│   ├── sync/                       # SyncWorker (WorkManager)
│   ├── ui/                         # MainActivity + Fragments
│   │   ├── tasklist/               # TaskListFragment
│   │   ├── edittask/               # EditTaskFragment
│   │   ├── hotlist/                # HotListFragment
│   │   └── settings/               # SettingsFragment
│   └── viewmodel/                  # ViewModels
├── util/                           # DateUtils
└── widget/                         # HotListWidgetProvider + Service
```

---

## Быстрый старт

### Требования

- Android Studio Hedgehog или новее
- JDK 17
- Android SDK 34

### Установка

```bash
git clone https://github.com/kozvits/ToodledoOrganiser.git
cd ToodledoOrganiser
```

Открой в Android Studio → **File → Open → выбери папку проекта**.

Нажми **Run ▶** или собери APK:

```bash
./gradlew assembleDebug
```

APK будет в:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## GitHub Actions (автосборка APK)

При каждом `push` в ветку `main` автоматически запускается сборка.

**Скачать APK:**
1. Перейди на вкладку **Actions** в репозитории
2. Выбери последний запуск **Build APK**
3. Скачай артефакт **ToodledoOrganiser-debug**

**Выпустить релиз:**
```bash
git tag v1.0.0
git push origin v1.0.0
```
Релизный APK будет прикреплён к GitHub Release автоматически.

---

## Подключение к реальному Toodledo API (v2)

Сейчас приложение работает на моковых данных. Для подключения к настоящему API:

1. Получи API-ключ на [toodledo.com/info/api.php](https://api.toodledo.com/3/account/doc.php)
2. Открой `SettingsFragment` → введи логин и пароль
3. В `data/repository/SyncRepositoryImpl` замени `delay(1500)` на реальный вызов Retrofit:
   ```kotlin
   // TODO: заменить на:
   val result = toodledoApi.getTasks(token = authToken)
   // ... сохранить в Room
   ```
4. Добавь `ToodledoApiService` в `di/AppModules.kt`

---

## Виджет горячего списка

1. Долгое нажатие на рабочем столе → **Виджеты**
2. Найди **Toodledo Hot List**
3. Перетащи на рабочий стол
4. Настройка сортировки — внутри приложения на вкладке **Hot List**

---

## Зависимости

| Библиотека | Версия |
|---|---|
| Kotlin | 1.9.22 |
| AndroidX Core KTX | 1.12.0 |
| Material Components | 1.11.0 |
| Jetpack Navigation | 2.7.7 |
| Room | 2.6.1 |
| Hilt | 2.50 |
| WorkManager | 2.9.0 |
| Coroutines | 1.7.3 |
| Retrofit | 2.9.0 |
| OkHttp | 4.12.0 |

---

## Лицензия

MIT License © 2024 kozvits

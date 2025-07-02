# 📚 Book Service

REST-сервис для управления книгами, написанный на Spring Boot и PostgreSQL.

## 🚀 Возможности

* Добавление, получение, обновление и удаление книг
* Добавление и получение авторов
* Ассоциация книги с автором
* Проверка на дублирующие книги — запрещает создание/обновление книги
* Валидация входных данных и обработка ошибок

## 🧩 Структура проекта

```
src/
├── main/
│   ├── java/com.example.book_service 
│   │    ├── controller/    // REST‑контроллеры
│   │    ├── service/       // Бизнес-логика
│   │    └── repository/    // Spring Data JPA / Сущности Book, Author
└── resources/
     └──application.properties   // Настройки БД
     
```

## ⚙️ Настройка и запуск

1. **Настройте подключение к PostgreSQL** в `application.properties`; пример:

   ```properties
   spring.application.name=book_service
   spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

2. Подготовьте бд:

   ```sql
   CREATE TABLE authors (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_year INT
   );
   
   CREATE TABLE books (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255),
    author_id BIGINT NOT NULL,
    year INT,
    genre VARCHAR(100),
    CONSTRAINT fk_author
        FOREIGN KEY (author_id) REFERENCES authors(id)
        ON UPDATE CASCADE
   );
   ```

3. **Запустите приложение**:

   ```bash
   ./gradlew bootRun
   ```

4. Приложение будет доступно по умолчанию на `http://localhost:8080/`.

## 🧪 Примеры API-запросов

### Добавить автора

```bash
curl -X 'POST' \
  'http://localhost:8080/authors' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Александр Сергеевич Пушкин",
  "birthYear": 1799
}'
```

### Получить автора по id
```bash
curl -X 'GET' \
  'http://localhost:8080/authors/1' \
  -H 'accept: */*'
```

### Получить список авторов (пагинация)

```bash
curl -X 'GET' \
  'http://localhost:8080/authors?page=0&size=10' \
  -H 'accept: */*'
```

### Добавить книгу

```bash
curl -X 'POST' \
  'http://localhost:8080/books' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "Руслан и Людмила",
  "author": {
    "id": 1
  },
  "year": 1820,
  "genre": "поэма"
}'
```

### Обновить книгу

```bash
curl -X 'PUT' \
  'http://localhost:8080/books/18?title=Новое%20название&authorId=1&year=1870' \
  -H 'accept: */*'
```

### Получить книгу

```bash
curl -X 'GET' \
  'http://localhost:8080/books/1' \
  -H 'accept: */*'
```

### Удалить книгу

```bash
curl -X 'DELETE' \
  'http://localhost:8080/books/1' \
  -H 'accept: */*'
```


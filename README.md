# En-Vi dictionary

Dictionary application (English to Vietnamese) for 2122H_INT2204_40 OOP Course written in Java

**Note**: This is the final GUI version of the dictionary. The CMD version is on branch `cmd`.

**Update**: We got second highest score of all the groups though.

## Table of contents

- [Authors](#authors)
- [Preview](#preview)
- [Features](#features)
- [Set up](#set-up)
    - [MYSQL Database](#mysql-database)
    - [Run](#run)
        - [Requirements](#requirements)
        - [Steps](#steps)

## Authors

Group N1_02:

- [Huỳnh Tiến Dũng](https://github.com/HynDuf7) - 21020007
- [Lê Hải Đăng](https://github.com/milomolly) - 21020065
- [Trần Mạnh Dũng](https://github.com/Maduro29) - 21020119

## Preview

![light-mode](https://user-images.githubusercontent.com/29995756/184926495-a87ab52a-d8d6-49a8-803d-9f2710aa000d.png)
![dark-mode](https://user-images.githubusercontent.com/29995756/184927257-e543b5e6-ce9e-4e39-9682-0e6c150da4f2.png)

## Features

- An English-to-Vietnamese dictionary with a full-fledged words' database and Google Translate API
  integrated.
- Many features:
    - Search Vietnamese meaning.
    - Words' meaning in HTML format for beautiful look.
    - Add words.
    - Insert words from file.
    - Export words to file.
    - Edit words.
    - Delete words.
    - Google Translate translate sentences (Both from English to Vietnamese and Vietnamese to
      English).
    - Google Translate Text to speech (Both in English and in Vietnamese).
    - Connect to MYSQL for a rich words' database.
    - Switching between Light Mode and Dark Mode (TokyoNight colorscheme).

## Set up

### MYSQL Database

- Install XAMPP.
- Start XAMPP (Start Apache and MYSQL).
- Ensure `localhost` port is `3306` (XAMPP default port).
- Add a new user:
    - Name: `en-vi-dictionary`.
    - Password: `n1-02-dictionary`.
    - Enable option `[o] Create database with same name and grant all privileges.`
- Import `dictionary.sql` into the
  database `en-vi-dictionary` (`src/main/resources/sql/dictionary.sql`).

**Note**: You can configure your own username, port, password, ... by changing those accordingly
in `src/main/java/dictionary/server/DatabaseDictionary.java`.

After uploading the `dictionary.sql` file, next time you only need to start XAMPP (Start Apache and
MYSQL) if you want to use the dictionary with MYSQL Database.

### Run

#### Requirements

- JDK 17 or higher. (Note for Windows user: Ensure that your `JAVA_HOME` environment variable is set
  to the correct folder of JDK17).
- Maven (Apache Maven).

#### Steps

- Clone this project down with git.
  ```
  git clone https://github.com/HynDuf7/dictionary.git
  ```
- Run with Maven.
  ```
  cd dictionary
  mvn clean javafx:run
  ```


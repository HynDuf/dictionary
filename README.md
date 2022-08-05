# En-Vi dictionary
Dictionary application (English to Vietnamese) for 2122H_INT2204_40 OOP Course written in Java

# Authors

Group N1_02:

- [Huỳnh Tiến Dũng](https://github.com/HynDuf7) - 21020007
- [Lê Hải Đăng](https://github.com/milomolly) - 21020065
- [Trần Mạnh Dũng](https://github.com/Maduro29) - 21020119


# Installation

## MYSQL Database

- Install XAMPP.
- Ensure `localhost` port is `3306`.
- Add a new user:
  - Name: `en-vi-dictionary`.
  - Password: `n1-02-dictionary`.
- Add a database named `en-vi-dictionary` (Ensure that the user `en-vi-dictionary` has access to the created database).
- Import `dictionary.sql` into the database (`src/main/resources/sql/dictionary.sql`).

**Note**: You can configure your own username, port, password, ... by changing those accordingly in `src/main/java/dictionary/server/database/Database.java`.
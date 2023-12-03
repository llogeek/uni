from getpass import getpass
from mysql.connector import connect, Error

try:
    with connect(
        host="localhost",
        user='Lolita',
        password='Lolita23102002',
        database="users_db",
    ) as connection:
        print(connection)
except Error as e:
    print(e)
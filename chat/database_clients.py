from getpass import getpass
from mysql.connector import connect, Error

try:
    with connect(
        host="localhost",
        user=,
        password=,
        database="users_db",
    ) as connection:
        print(connection)
except Error as e:
    print(e)
